package hu.bme.aut.broadcastreceiverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirModeReceiever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "AIRPLANE MODE CHANGE!!!!",
            Toast.LENGTH_LONG).show()
    }

}