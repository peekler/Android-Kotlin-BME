package hu.aut.android.broadcastreceiverdemo

import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.widget.Toast
import android.content.BroadcastReceiver
import android.content.Context


class AirPlaneReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "AIRPLANE MODE CHANGE!!!!",
                Toast.LENGTH_LONG).show()
    }



}