package hu.aut.android.nearbymessagedemo

import android.Manifest
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import co.intentservice.chatui.models.ChatMessage
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var messageListener = object : MessageListener() {
        override fun onFound(message: com.google.android.gms.nearby.messages.Message) {
            chatView.addMessage(ChatMessage(String(message.content), System.currentTimeMillis(), ChatMessage.Type.RECEIVED ))
            playNotificationTone()
        }

        override fun onLost(message: com.google.android.gms.nearby.messages.Message) {
            Toast.makeText(this@MainActivity, "LOST ${String(message.content)}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatView.setOnSentMessageListener { chatMessage ->
            publish(chatMessage.message)
            true
        }
    }

    private fun playNotificationTone() {
        val uriNotif = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        val r = RingtoneManager.getRingtone(
            applicationContext, uriNotif
        )
        r.play()
    }



    override fun onStart() {
        super.onStart()

        requestNeededPermission()
    }

    override fun onStop() {
        Nearby.getMessagesClient(this).unsubscribe(messageListener)
        super.onStop()
    }


    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,
                    "I need it for nearby", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101)
        } else {
            // már van engedély
            Nearby.getMessagesClient(this).subscribe(messageListener)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION perm granted", Toast.LENGTH_SHORT).show()

                    Nearby.getMessagesClient(this).subscribe(messageListener);
                } else {
                    Toast.makeText(this,
                        "ACCESS_FINE_LOCATION perm NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun publish(message: String) {
        val nearByMessage = Message(message.toByteArray())
        Nearby.getMessagesClient(this).publish(nearByMessage)
    }
}
