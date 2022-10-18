package hu.bme.aut.broadcastreceiverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras ?: return
        val pdus = extras.get("pdus") as Array<ByteArray>
        for (pdu in pdus) {
            val msg = SmsMessage.createFromPdu(pdu)
            val origin = msg.originatingAddress
            val body = msg.messageBody
            Toast.makeText(context,
                "SMS caught, number: $origin body: $body",
                Toast.LENGTH_LONG).show()
        }

    }
}