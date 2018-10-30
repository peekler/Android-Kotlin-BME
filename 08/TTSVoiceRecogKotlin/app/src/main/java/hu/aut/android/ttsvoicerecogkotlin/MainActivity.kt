package hu.aut.android.ttsvoicerecogkotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var sr: android.speech.SpeechRecognizer

    private val TAG = "TAG_SPEACH"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)

        btnRead.setOnClickListener { speak(etData.text.toString()) }

        val btnDetect = findViewById<View>(R.id.btnDetect) as Button
        btnDetect.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            //        "hu-HU");
            intent.putExtra(
                RecognizerIntent.EXTRA_CALLING_PACKAGE,
                "hu.aut.android.ttsvoicerecogkotlin"
            )

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            sr.startListening(intent)
        }


        requestNeededPermission()
    }

    fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "I need it for recognition", Toast.LENGTH_SHORT
                ).show()
            }

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                101
            )
        } else {
            sr = android.speech.SpeechRecognizer
                .createSpeechRecognizer(this)
            sr.setRecognitionListener(SpeechRecognizer())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this@MainActivity, "RECORD_AUDIO perm granted", Toast.LENGTH_SHORT).show()

                    sr = android.speech.SpeechRecognizer
                        .createSpeechRecognizer(this)
                    sr.setRecognitionListener(SpeechRecognizer())

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "RECORD_AUDIO perm NOT granted", Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            //int result = tts.setLanguage(Locale.forLanguageTag("hu-HU"));
            val result = tts.setLanguage(Locale.forLanguageTag("en-EN"))

            // tts.setSpeechRate((float) 0.8);
            // tts.setPitch(1.0f); tts.setPitch(1.1f);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                val installIntent = Intent()
                installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(installIntent)
            } else {
                speak("Speech system works perfectly!")
            }

        } else {
            val installIntent = Intent()
            installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            startActivity(installIntent)
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            tts.stop()
            tts.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            sr.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    internal inner class SpeechRecognizer : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle) {
            Log.d(TAG, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d(TAG, "onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray) {
            Log.d(TAG, "onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech")
        }

        override fun onError(error: Int) {
            Log.d(TAG, "error $error")
        }

        override fun onResults(results: Bundle) {
            val str = String()
            Log.d(TAG, "onResults $results")
            val data = results
                .getStringArrayList(
                    android.speech.SpeechRecognizer.RESULTS_RECOGNITION
                )
            tvDetectedText.text = ""
            var timeDetected = false
            var sureDetected = false
            for (text in data) {
                tvDetectedText!!.append(text + "\n")
                if (!timeDetected && text.contains("time")) {
                    val dateFormat = SimpleDateFormat(
                        "HH:mm:ss"
                    )
                    speak(dateFormat.format(Date(System.currentTimeMillis())))
                    timeDetected = true
                }

                if (!sureDetected && text.contains("sure")) {
                    speak("Yes, I'm sure, I'm perfect")
                    sureDetected = true
                }
            }
        }

        override fun onPartialResults(partialResults: Bundle) {
            Log.d(TAG, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle) {
            Log.d(TAG, "onEvent $eventType")
        }
    }
}
