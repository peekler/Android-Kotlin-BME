package hu.bme.aut.broadcastreceivertest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
            Toast.makeText(context, "Screen off", Toast.LENGTH_LONG).show()
        } else if (intent.action.equals(Intent.ACTION_SCREEN_ON)) {
            Toast.makeText(context, "Screen on", Toast.LENGTH_LONG).show()
        }
    }
}