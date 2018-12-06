package hu.aut.android.kotlinnfc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.PendingIntent
import android.nfc.NfcAdapter
import android.content.Intent
import android.nfc.NdefRecord
import android.nfc.NdefMessage
import kotlinx.android.synthetic.main.activity_main.*
import android.content.IntentFilter
import android.nfc.Tag
import android.nfc.tech.NdefFormatable
import android.nfc.tech.Ndef
import android.widget.Toast
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var nfcPendingIntent: PendingIntent
    private var writeTagFilters: Array<IntentFilter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0, Intent(this,
                javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        btnEnableWrite.setOnClickListener {
            enableTagWriteMode()
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val parcelableArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            parcelableArray?.forEach {
                val ndefMsg = it as NdefMessage
                ndefMsg?.records.forEach {
                    tvStatus.append("${String(it.payload)}\n")
                }
            }
        }
    }


    private fun enableTagWriteMode() {
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        writeTagFilters = arrayOf(tagDetected)
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
                writeTagFilters, null)
    }


    override fun onNewIntent(intent: Intent) {
        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val detectedTag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val record1 = createTextRecord(etNfcToWrite.text.toString())
            val record2 = NdefRecord.createUri("sms:+36301234567?body=Hello NFC HWSW")
            val record3 = NdefRecord.createUri("tel:+36301234567")
            val record4 = NdefRecord.createUri("http://www.hwsw.hu")

            val msg = NdefMessage(
                    arrayOf(record1, record2, record3, record4))
            if (writeTag(msg, detectedTag)) {
                Toast.makeText(this, "Success write operation!", Toast.LENGTH_LONG)
                        .show()
            } else {
                Toast.makeText(this, "Failed to write!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createTextRecord(payload: String): NdefRecord {
        val textBytes = payload.toByteArray()
        val data = ByteArray(1 + textBytes.size)
        data[0] = 0.toByte()
        System.arraycopy(textBytes, 0, data, 1, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

    private fun writeTag(message: NdefMessage, tag: Tag): Boolean {
        val size = message.toByteArray().size
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                if (!ndef.isWritable) {
                    return false
                }
                if (ndef.maxSize < size) {
                    return false
                }
                ndef.writeNdefMessage(message)
                return true
            } else {
                val format = NdefFormatable.get(tag)
                return if (format != null) {
                    try {
                        format.connect()
                        format.format(message)
                        true
                    } catch (e: IOException) {
                        false
                    }
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            return false
        }
    }

}
