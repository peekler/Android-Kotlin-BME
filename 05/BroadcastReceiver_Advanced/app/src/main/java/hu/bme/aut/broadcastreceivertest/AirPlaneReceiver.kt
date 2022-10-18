package hu.bme.aut.broadcastreceivertest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirPlaneReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "AIRPLANE MODE CHANGE!!!!",
            Toast.LENGTH_LONG).show()
    }
}