package hu.bme.aut.audiorecorddemo

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.audiorecorddemo.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        private val LOG_TAG = "AudioRecorderExample"
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    lateinit var binding: ActivityMainBinding

    private var fileName = ""
    private var myPlayer: MediaPlayer? = null
    private var myRecorder: MediaRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = getExternalFilesDir(null)?.getAbsolutePath() + "/audiorecordtest.3gp"

        if (allPermissionsGranted()) {
            setViews()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
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
                setViews()
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

    private fun setViews() {
        binding.btnStartRecord.isEnabled = true
        binding.btnStopRecord.isEnabled = true
        binding.btnStartPlay.isEnabled = true
        binding.btnStopPlay.isEnabled = true

        binding.btnStartRecord.setOnClickListener {
            startRecording()
        }

        binding.btnStopRecord.setOnClickListener {
            stopRecording()
        }

        binding.btnStartPlay.setOnClickListener {
            startPlaying()
        }

        binding.btnStopPlay.setOnClickListener {
            stopPlaying()
        }
    }

    private fun startPlaying() {
        myPlayer = MediaPlayer()
        try {
            myPlayer?.setDataSource(fileName)
            myPlayer?.prepare()
            myPlayer?.start()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }
    }

    private fun stopPlaying() {
        myPlayer?.release()
    }

    private fun startRecording() {
        try {
            myRecorder = MediaRecorder()
            myRecorder?.setAudioSource(
                MediaRecorder.AudioSource.MIC
            )
            myRecorder?.setOutputFormat(
                MediaRecorder.OutputFormat.THREE_GPP
            )
            val outputFile = File(fileName)
            if (outputFile.exists())
                outputFile.delete()
            outputFile.createNewFile()
            myRecorder?.setOutputFile(fileName)

            myRecorder?.setAudioEncoder(
                MediaRecorder.AudioEncoder.AMR_NB
            )
            myRecorder?.prepare()
            myRecorder?.start()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(LOG_TAG, "prepare() failed")
        }
    }

    private fun stopRecording() {
        myRecorder?.stop()
        myRecorder?.release()
    }
}