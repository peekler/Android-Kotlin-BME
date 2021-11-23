package hu.aut.android.broadcastreceiverdemo

import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


class OutCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val outNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
        Toast.makeText(context, outNumber, Toast.LENGTH_LONG).show()

        //this.resultData = "55566"
    }
}