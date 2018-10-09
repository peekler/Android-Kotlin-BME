package activityrecognition.competition.autsoft.hu.activityrecognitiondemo

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var service: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                val activityType = it.extras.getInt(ActivityIntentService.EXTRA_ACTIVITY_TYPE, -1)
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

    private fun LocalBroadcastManager.registerReceiver(intentFilter: IntentFilter, onReceive: (intent: Intent) -> Unit) {
        this.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                onReceive.invoke(intent)
            }
        }, intentFilter)
    }

}
