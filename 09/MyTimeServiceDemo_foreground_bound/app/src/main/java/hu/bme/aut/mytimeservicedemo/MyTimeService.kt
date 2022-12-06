package hu.bme.aut.mytimeservicedemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*

class MyTimeService : Service() {

    private val NOTIFICATION_CHANNEL_ID = "time_service_notifications"
    private val NOTIFICATION_CHANNEL_NAME = "Time Service notifications"
    private val NOTIF_FOREGROUND_ID = 101

    var enabled = false

    private var interval : Long = 5000

    inner class TimeServiceBinder: Binder() {
        internal val service: MyTimeService
            get() = this@MyTimeService

        fun changeIntervalDemo(newInterval: Long) {
            interval = newInterval
        }
    }

    fun changeInterval(newInterval: Long) {
        interval = newInterval
    }

    private val timeServiceBinder = TimeServiceBinder()


    inner class MyTimeThread : Thread() {
        override fun run() {
            val handlerMain = Handler(Looper.getMainLooper())

            while (enabled) {
                val dateTime = Date(System.currentTimeMillis()).toString()

                handlerMain.post {
                    Toast.makeText(
                        this@MyTimeService, dateTime,
                        Toast.LENGTH_LONG
                    ).show()
                }

                updateNotification(dateTime)

                sleep(interval)
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return timeServiceBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            NOTIF_FOREGROUND_ID,
            getMyNotification("Hello foreground"))

        if (!enabled) {
            enabled = true
            MyTimeThread().start()
        }

        return START_STICKY_COMPATIBILITY
    }


    private fun updateNotification(text: String) {
        val notification = getMyNotification(text)
        val notifMan = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notifMan?.notify(NOTIF_FOREGROUND_ID, notification)
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
            .setContentTitle("This the MyTimeService")
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
        enabled = false
        stopForeground(false)
        super.onDestroy()
    }
}