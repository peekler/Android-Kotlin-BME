package hu.ait.android.workmanagerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequest
import androidx.work.NetworkType


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java).setConstraints(constraints).build()
        WorkManager.getInstance().enqueue(workRequest)
    }
}
