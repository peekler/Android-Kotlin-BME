package hu.aut.android.kotlinwearnotifdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.RemoteInput
import android.widget.Toast
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Task
import android.support.annotation.WorkerThread
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity(), DataClient.OnDataChangedListener, MessageClient.OnMessageReceivedListener {

    private val NOTIFICATION_CHANNEL_ID = "my_wear_notifications"
    private val NOTIFICATION_CHANNEL_NAME = "My Wear notifications"

    companion object {
        public val EXTRA_VOICE_REPLY = "extra_voice_reply"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNotif.setOnClickListener {
            //showBaiscNotifTime()
            //showNotifOrderMap()
            showNotifOrderMapWithChoices()
        }

        btnMessage.setOnClickListener {
            getNodes()
        }
    }


    private fun showBaiscNotifTime() {
        val notificationId = 1

        // Build intent for notification content
        val viewPendingIntent = Intent(this, SecondActivity::class.java).let { viewIntent ->
            viewIntent.putExtra("EXTRA_EVENT_ID", 101)
            PendingIntent.getActivity(this, 0, viewIntent, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        // Notification channel ID is ignored for Android 7.1.1
        // (API level 25) and lower.
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.mydroid)
            .setContentTitle("ContentTitle")
            .setContentText(Date(System.currentTimeMillis()).toString())
            .setContentIntent(viewPendingIntent)

        NotificationManagerCompat.from(this).apply {
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun showNotifOrderMap() {
        val notificationId = 1

        val mapIntent = Intent(Intent.ACTION_VIEW)
        val mapPendingIntent = Intent(Intent.ACTION_VIEW).let { mapIntent ->
            //mapIntent.data = Uri.parse("geo:0,0?q=Budapest")
            mapIntent.data = Uri.parse("waze://?q=BME&navigate=yes")
            PendingIntent.getActivity(this, 0, mapIntent, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val bigStyle = NotificationCompat.BigTextStyle()
        bigStyle.bigText("You have a new order from our company. Please decide how you would like to pick up the item.")

        // Notification channel ID is ignored for Android 7.1.1
        // (API level 25) and lower.
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.mydroid)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.truck))
            .setContentTitle("Order details")
            .setContentText(Date(System.currentTimeMillis()).toString())
            .addAction(R.drawable.ic_flight_takeoff, "Show store", mapPendingIntent)
            .setColor(Color.GREEN)
            .setStyle(bigStyle)

        NotificationManagerCompat.from(this).apply {
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun showNotifOrderMapWithChoices() {
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val bigStyle = NotificationCompat.BigTextStyle()
        bigStyle.bigText("You have a new order from our company. Please decide how you would like to pick up the item.")


        //-- Expanded notification choices
        val orderPendingIntent = Intent(this, SecondActivity::class.java).let { orderPendingIntent ->
            orderPendingIntent.putExtra("EXTRA_ORDER_ID", 101)
            PendingIntent.getActivity(this, 0, orderPendingIntent, 0)
        }

        val mapIntent = Intent(Intent.ACTION_VIEW)
        val mapPendingIntent = Intent(Intent.ACTION_VIEW).let { mapIntent ->
            //mapIntent.data = Uri.parse("geo:0,0?q=Budapest")
            mapIntent.data = Uri.parse("waze://?q=BME&navigate=yes")
            PendingIntent.getActivity(this, 0, mapIntent, 0)
        }

        val replyLabel = "Please reply"
        val replyChoices = resources.getStringArray(R.array.reply_choices)

        val remoteInput = RemoteInput.Builder(EXTRA_VOICE_REPLY)
            .setLabel(replyLabel)
            .setChoices(replyChoices)
            .build()

        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_flight_takeoff,
            "Delivery", orderPendingIntent
        ).addRemoteInput(remoteInput).build()

        val actionMap = NotificationCompat.Action.Builder(
            R.drawable.mydroid,
            "Store location", mapPendingIntent
        ).build()

        val wearableExtender = NotificationCompat.WearableExtender()
            .addAction(action)
            .addAction(actionMap)
        //---

        // Notification channel ID is ignored for Android 7.1.1
        // (API level 25) and lower.
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.mydroid)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.truck))
            .setContentTitle("Order details")
            .setContentText(Date(System.currentTimeMillis()).toString())
            .setColor(Color.GREEN)
            .setStyle(bigStyle)
            .extend(wearableExtender)

        NotificationManagerCompat.from(this).apply {
            notify(notificationId, notificationBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    override fun onResume() {
        super.onResume()

        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
    }

    override fun onPause() {
        super.onPause()

        Wearable.getDataClient(this).removeListener(this)
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onDataChanged(p0: DataEventBuffer) {
        Toast.makeText(this, "message received", Toast.LENGTH_LONG).show()
    }

    override fun onMessageReceived(p0: MessageEvent) {
        Toast.makeText(this, "message received", Toast.LENGTH_LONG).show()

        if (p0.path.contains("ring")) {
            playNotificationTone()
        }
    }

    private fun getNodes() {
        Thread {
            val nodeListTask = Wearable.getNodeClient(applicationContext).connectedNodes
            val nodes = Tasks.await(nodeListTask)

            runOnUiThread {
                nodes.forEach {
                    Toast.makeText(this@MainActivity, it.displayName, Toast.LENGTH_LONG).show()

                    Wearable.getMessageClient(this).sendMessage(it.id, "/msg", "Data".toByteArray())
                }
            }
        }.start()
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

}
