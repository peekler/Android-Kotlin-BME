package hu.aut.android.servicedemo.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import hu.aut.android.servicedemo.MainActivity
import hu.aut.android.servicedemo.R
import java.util.Date

class TimeService: Service() {

    private var enabled = false
    private val NOTIFICATION_CHANNEL_ID = "time_service_notifications"
    private val NOTIFICATION_CHANNEL_NAME = "Time Service notifications"
    private val NOTIF_FOREGROUND_ID = 101

    private var interval : Long = 5000

    inner class TimeServiceBinder: Binder() {
        internal val service: TimeService
            get() = this@TimeService
    }

    private val timeServiceBinder = TimeServiceBinder()

    override fun onBind(intent: Intent): IBinder? {
        return timeServiceBinder
    }

    fun changeInterval(newInterval: Long) {
        interval = newInterval
    }

    private inner class MyTimeThread : Thread() {
        override fun run() {
            val handlerMain = Handler(Looper.getMainLooper())
            while (enabled) {
                handlerMain.post {
                    val dateString = Date(System.currentTimeMillis()).toString()

                    Toast.makeText(
                            this@TimeService,
                            dateString,
                            Toast.LENGTH_LONG).show()

                    updateNotification(dateString)
                }

                Thread.sleep(interval)
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(NOTIF_FOREGROUND_ID,
                getMyNotification("Starting..."))

        if (!enabled) {
            enabled = true
            MyTimeThread().start()
        }

        return START_STICKY
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
        stopForeground(true)
        enabled = false
        super.onDestroy()
    }
}