package activityrecognition.competition.autsoft.hu.activityrecognitiondemo

import android.Manifest
import android.app.IntentService
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var service: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNeededPermission()
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    101
            )
        } else {
            // we have the permission
        }
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "ACTIVITY_RECOGNITION perm granted", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Toast.makeText(
                            this,
                            "ACTIVITY_RECOGNITION perm NOT granted", Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    fun requestTransitionUpdates(view: View) {
        val client = ActivityRecognition.getClient(this)
        val intent = Intent(this, ActivityIntentService::class.java)
        service = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val task = client.requestActivityTransitionUpdates(createTransitionRequest(), service)
        task.addOnSuccessListener {
            appendToLog("Tracking Started")
            val statusIntentFilter = IntentFilter(ActivityIntentService.ACTION_TRANSITION_UPDATED)
            LocalBroadcastManager.getInstance(this).registerReceiver(statusIntentFilter) {
                val activityType = it.extras?.getInt(ActivityIntentService.EXTRA_ACTIVITY_TYPE, -1)
                when (activityType) {
                    DetectedActivity.STILL -> appendToLog("Still Started")
                    DetectedActivity.ON_FOOT -> appendToLog("Standing Started")
                    DetectedActivity.WALKING -> appendToLog("Walking Started")
                    DetectedActivity.ON_BICYCLE -> appendToLog("Cycling Started")
                    DetectedActivity.RUNNING -> appendToLog("Running Started")
                    DetectedActivity.IN_VEHICLE -> appendToLog("In Vehicle Started")
                }
            }
        }
        task.addOnFailureListener {
            appendToLog("Tracking could not start: ${it.message}")
        }
    }

    private fun appendToLog(message: String) {
        activityLog.append("\n $message")
    }

    private fun createTransitionRequest(): ActivityTransitionRequest {
        val transitions = listOf(
                ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build(),
                ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_FOOT)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build(),
                ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build(),
                ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_BICYCLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build(),
                ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build())
        return ActivityTransitionRequest(transitions)
    }

    override fun onPause() {
        super.onPause()
        if (service != null) {
            ActivityRecognition.getClient(this).removeActivityTransitionUpdates(service)
        }
    }
}

class ActivityIntentService : IntentService("TAG") {
    companion object {
        const val ACTION_TRANSITION_UPDATED = "ACTION_TRANSITION_UPDATED"
        const val EXTRA_ACTIVITY_TYPE = "EXTRA_ACTIVITY_TYPE"
    }

    override fun onHandleIntent(intent: Intent?) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent) ?: return
            result.transitionEvents.forEach {
                val activityType = it.activityType
                val localIntent = Intent(ACTION_TRANSITION_UPDATED).putExtra(EXTRA_ACTIVITY_TYPE, activityType)
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
            }
        }
    }

}

private fun LocalBroadcastManager.registerReceiver(intentFilter: IntentFilter, onReceive: (intent: Intent) -> Unit) {
    this.registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onReceive.invoke(intent)
        }
    }, intentFilter)
}
