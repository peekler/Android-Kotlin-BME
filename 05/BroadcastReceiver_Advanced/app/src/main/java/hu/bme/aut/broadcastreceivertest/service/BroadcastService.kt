package hu.bme.aut.broadcastreceivertest.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import hu.bme.aut.broadcastreceivertest.*

class BroadcastService: Service() {

    private val NOTIFICATION_CHANNEL_ID = "time_service_notifications"
    private val NOTIFICATION_CHANNEL_NAME = "Time Service notifications"
    private val NOTIF_FOREGROUND_ID = 101

    private lateinit var airPlaneReceiver: AirPlaneReceiver
    private lateinit var screenReceiver: ScreenReceiver
    private lateinit var bootReceiver: BootReceiver
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver


    override fun onBind(intent: Intent): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(NOTIF_FOREGROUND_ID,
            getMyNotification("Running..."))

        airPlaneReceiver = AirPlaneReceiver()
        screenReceiver = ScreenReceiver()
        bootReceiver = BootReceiver()
        myBroadcastReceiver = MyBroadcastReceiver()

        registerReceiver(
            airPlaneReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        registerReceiver(
            screenReceiver,
            IntentFilter(Intent.ACTION_SCREEN_ON)
        )
        registerReceiver(
            screenReceiver,
            IntentFilter(Intent.ACTION_SCREEN_OFF)
        )

        registerReceiver(
            bootReceiver,
            IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        )

        registerReceiver(
            myBroadcastReceiver,
            IntentFilter("hu.bme.aut.NOTIFY")
        )

        return START_STICKY
    }


    private fun getMyNotification(text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this,
            NOTIF_FOREGROUND_ID,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(
            this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("This the BroadcastService")
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVibrate(longArrayOf(1000, 2000, 1000))
            .setContentIntent(contentIntent).build()
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

    override fun onDestroy() {
        unregisterReceiver(
            airPlaneReceiver
        )

        unregisterReceiver(
            screenReceiver
        )

        unregisterReceiver(
            bootReceiver
        )

        unregisterReceiver(
            myBroadcastReceiver
        )

        stopForeground(false)
        super.onDestroy()
    }

}