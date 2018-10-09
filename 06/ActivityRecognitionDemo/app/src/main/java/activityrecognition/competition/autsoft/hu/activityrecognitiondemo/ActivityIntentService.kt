package activityrecognition.competition.autsoft.hu.activityrecognitiondemo

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityTransitionResult

class ActivityIntentService : IntentService("TAG") {
    companion object {
        const val ACTION_TRANSITION_UPDATED = "ACTION_TRANSITION_UPDATED"
        const val EXTRA_ACTIVITY_TYPE = "EXTRA_ACTIVITY_TYPE"
    }

    override fun onHandleIntent(intent: Intent) {
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
