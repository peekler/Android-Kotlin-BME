package geofencing.competition.autsoft.hu.geofencingdemo

import android.Manifest
import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE_LOCATION = 1
    }

    private var geofencingClient: GeofencingClient? = null

    // Hős street
    private val region = Triple(47.49671, 19.11177, 5000f)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceIntentService::class.java)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (PERMISSION_REQUEST_CODE_LOCATION == requestCode) {
            geofencingClient = LocationServices.getGeofencingClient(this)
        }
    }

    fun initGeofence(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return
        }
        geofencingClient?.addGeofences(createGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Geofence set", Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createGeofencingRequest(): GeofencingRequest {
        val geofenceList = listOf(Geofence.Builder()
                .setRequestId("ID")
                .setCircularRegion(
                        region.first,
                        region.second,
                        region.third)
                .setExpirationDuration(120_000L)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

}

class GeofenceIntentService(private val tag: String = "TAG") : IntentService(tag) {

    companion object {
        const val CHANNEL_ID = "DEFAULT"
    }

    override fun onHandleIntent(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e(tag, geofencingEvent.errorCode.toString())
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> sendNotification("Watch out!")
            Geofence.GEOFENCE_TRANSITION_EXIT -> sendNotification("You can calm down.")
        }
    }

    private fun sendNotification(message: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Alert")
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())
    }

}