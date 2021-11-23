package hu.bme.aut.speecrecogandttsdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.speecrecogandttsdemo.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    companion object {
        private const val TAG = "TAG_SPEECH"
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO)
    }

    lateinit var binding : ActivityMainBinding
    private lateinit var speechRecognizer: android.speech.SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.btnDetect.isEnabled = true

        speechRecognizer = android.speech.SpeechRecognizer
                .createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(MySpeechRecognizer())

        textToSpeech = TextToSpeech(this, this)

        binding.btnDetect.setOnClickListener {
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
            speechRecognizer.startListening(intent)
        }
        binding.btnRead.setOnClickListener {
            speak(binding.etData.text.toString())
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            //val result = textToSpeech.setLanguage(Locale.forLanguageTag("hu-HU"))
            val result = textToSpeech.setLanguage(Locale.ENGLISH)

            // textToSpeach.setSpeechRate((float) 0.8)
            // textToSpeach.setPitch(1.0f); tts.setPitch(1.1f)

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
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            speechRecognizer.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            textToSpeech.stop()
            textToSpeech.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    internal inner class MySpeechRecognizer : RecognitionListener {
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
            Log.d(TAG, "onResults $results")
            val data = results
                    .getStringArrayList(
                            android.speech.SpeechRecognizer.RESULTS_RECOGNITION
                    )
            binding.tvDetectedText.text = ""
            var timeDetected = false
            var sureDetected = false

            for (text in data!!.iterator()) {
                binding.tvDetectedText!!.append(text + "\n")

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