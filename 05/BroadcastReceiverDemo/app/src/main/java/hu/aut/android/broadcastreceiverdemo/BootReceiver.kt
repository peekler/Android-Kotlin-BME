package hu.aut.android.broadcastreceiverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "BOOT COMPLETED", Toast.LENGTH_LONG).show()
        Log.d("TAG_BOOT", "BOOT COMPLETED")
    }
}