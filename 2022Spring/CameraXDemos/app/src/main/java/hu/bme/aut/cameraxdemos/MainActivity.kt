package hu.bme.aut.cameraxdemos

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.core.R
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import hu.bme.aut.cameraxdemos.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture

    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Request camera permissions
        if (allPermissionsGranted()) {
            setUpCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.toggleSwitchCamera.setOnClickListener {
            lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            // Re-bind use cases to update selected camera
            bindCameraUseCases()
        }

        binding.btnTakePhoto.setOnClickListener {
            imageCapture.takePicture(
                    cameraExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            runOnUiThread {
                                binding.ivPhoto.setImageBitmap(
                                        imageProxyToBitmap(
                                                image
                                        )
                                )
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            super.onError(exception)
                        }
                    })
        }
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        val planeProxy = image.planes[0]
        val buffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer[bytes]
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setUpCamera()
            } else {
                Toast.makeText(
                        this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            try {
                binding.toggleSwitchCamera.isEnabled = hasBackCamera() && hasFrontCamera()
            } catch (exception: CameraInfoUnavailableException) {
                binding.toggleSwitchCamera.isEnabled = false
            }

            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun bindCameraUseCases() {
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { binding.cameraView.display.getRealMetrics(it) }
        Log.d("TAG_CAMERA", "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(
            metrics.widthPixels, metrics.heightPixels)
        Log.d("TAG_CAMERA", "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.cameraView.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview - use case
        val preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

        // Image capture - use case
        imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

        // ImageAnalysis - use case
        imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor!!, MyTextReaderImageAnalyzer())
                }

        cameraProvider.unbindAll()
        try {
            cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
            )
            preview.setSurfaceProvider(binding.cameraView.createSurfaceProvider())
        } catch (exc: Exception) {
            Log.e("TAG_LOG", "Use case binding failed", exc)
        }

    }

    private inner class MyTextReaderImageAnalyzer : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            extractText(imageProxy)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = Integer.max(width, height).toDouble() / Math.min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private val blockListener: BlockClickListener = {
        Toast.makeText(
                this,
                "chose the block with text= ${it.text}",
                Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun extractText(imageProxy: ImageProxy) {
        if (imageProxy.image == null) {
            return
        }

        runBlocking {
            try {
                val mediaImage = imageProxy.image
                val textImage =
                        InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                val visionText = TextRecognition.getClient().process(textImage).await()
                val blocks = visionText.textBlocks.toMutableList()

                withContext(Dispatchers.Main) {
                    BlocksRenderer.drawBlocks(
                            binding.blocksContainer,
                            Size(imageProxy.width, imageProxy.height),
                            imageProxy.imageInfo.rotationDegrees,
                            blocks,
                            blockListener
                    )

                    var text = ""
                    blocks.forEach {

                        text += it.text + "\n"
                    }

                    binding.tvData.text = text
                }

                Log.d("image recognizer", "reading image success= $blocks")

            } catch (e: Exception) {
                e.printStackTrace()
            }
            imageProxy.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}