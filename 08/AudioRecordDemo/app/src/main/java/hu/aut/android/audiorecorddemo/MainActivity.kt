package hu.aut.android.audiorecorddemo

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaRecorder
import android.media.MediaPlayer
import android.widget.Toast
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "AudioRecorderExample"
    private val fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.3gp"
    private var myPlayer: MediaPlayer? = null
    private var myRecorder: MediaRecorder? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()

        requestNeededPermission()
    }

    fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this@MainActivity, "Permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Permissions are NOT granted", Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun setViews() {
        btnStartRecord.setOnClickListener {
            startRecording()
        }

        btnStopRecord.setOnClickListener {
            stopRecording()
        }

        btnStartPlay.setOnClickListener {
            startPlaying()
        }

        btnStopPlay.setOnClickListener {
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
            Log.e(LOG_TAG, "prepare() failed")
        }
    }

    private fun stopRecording() {
        myRecorder?.stop()
        myRecorder?.release()
    }
}
