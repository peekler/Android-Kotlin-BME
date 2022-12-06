package hu.bme.aut.mytracker

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import java.util.*

class MyLocationTrackerService : LifecycleService() {

    private val NOTIFICATION_CHANNEL_ID = "location_service_notifications"
    private val NOTIFICATION_CHANNEL_NAME = "Location Service notifications"
    private val NOTIF_FOREGROUND_ID = 101

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startForeground(NOTIF_FOREGROUND_ID, getMyNotification(47.toDouble(), 19.toDouble()))

        val locationLiveData = MainLocationLiveData(this)
        locationLiveData.observe(this, object: Observer<Location>{
            override fun onChanged(t: Location) {
                updateNotification(t.latitude, t.longitude)
            }
        })

        return START_STICKY_COMPATIBILITY
    }


    private fun updateNotification(lat: Double, lng: Double) {
        val notification = getMyNotification(lat, lng)
        val notifMan = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notifMan?.notify(NOTIF_FOREGROUND_ID, notification)
    }

    private fun getMyNotification(lat: Double, lng: Double): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val gmmIntentUri = Uri.parse("geo:$lat,$lng")
        val notificationIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        notificationIntent.setPackage("com.google.android.apps.maps")
        val contentIntent = PendingIntent.getActivity(
            this,
            NOTIF_FOREGROUND_ID,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        return NotificationCompat.Builder(
            this, NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle("This the MyTimeService")
            .setContentText("$lat, $lng")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVibrate(longArrayOf(1000, 2000, 1000))
            .setContentIntent(contentIntent).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }


    override fun onDestroy() {
        stopForeground(false)
        super.onDestroy()
    }
}