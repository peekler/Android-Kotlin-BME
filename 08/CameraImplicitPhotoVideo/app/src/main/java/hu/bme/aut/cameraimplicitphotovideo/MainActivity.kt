package hu.bme.aut.cameraimplicitphotovideo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.cameraimplicitphotovideo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        const val REQUEST_CODE_CAMERA = 2001
        const val REQUEST_VIDEO_CAPTURE = 2002
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        } else {
            binding.btnPhoto.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnPhoto.setOnClickListener {
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intentCamera, REQUEST_CODE_CAMERA)
        }

        binding.btnVideo.setOnClickListener {
            val intentVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            intentVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5)
            startActivityForResult(intentVideo, REQUEST_VIDEO_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                val imageBitmap = data?.extras!!.get("data") as Bitmap
                binding.imageView.setImageBitmap(imageBitmap)
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                val videoUri: Uri = data!!.data!!
                binding.videoView.setOnPreparedListener { mp -> mp.isLooping = true }
                binding.videoView.setVideoURI(videoUri)
                binding.videoView.start()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                binding.btnPhoto.isEnabled = true
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
}