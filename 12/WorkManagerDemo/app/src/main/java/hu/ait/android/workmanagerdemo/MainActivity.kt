package hu.ait.android.workmanagerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraints = Constraints.Builder()
                //.setRequiresDeviceIdle(true)
                //.setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java).setConstraints(constraints).build()
        WorkManager.getInstance().enqueue(workRequest)

//        val workRequest = androidx.work.PeriodicWorkRequest.Builder(
//            MyWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
//            TimeUnit.MILLISECONDS).setConstraints(constraints).build()
//        WorkManager.getInstance().enqueueUniquePeriodicWork(
//            "demorepeate", ExistingPeriodicWorkPolicy.REPLACE, workRequest
//        )
    }
}
