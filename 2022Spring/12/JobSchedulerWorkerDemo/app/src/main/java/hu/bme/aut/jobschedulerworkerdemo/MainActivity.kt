package hu.bme.aut.jobschedulerworkerdemo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.work.*
import hu.bme.aut.jobschedulerworkerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        fun scheduleJob(context: Context) {
            val serviceComponent = ComponentName(context, DemoJobService::class.java)
            val builder = JobInfo.Builder(0, serviceComponent)
            builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
            builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
            //builder.setPeriodic(5000)
            //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            //builder.setRequiresDeviceIdle(true); // device should be idle
            //builder.setRequiresCharging(false); // we don't care if the device is charging or not
            val jobScheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            jobScheduler.schedule(builder.build())
        }
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnJobSchedulerStart.setOnClickListener {
            scheduleJob(this)
        }

        binding.btnWorkerStart.setOnClickListener {
            val constraints = Constraints.Builder()
                    //.setRequiresCharging(true)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            val workRequest =
                    OneTimeWorkRequest.Builder(MyWorker::class.java).
                    setConstraints(constraints).build()
            WorkManager.getInstance().enqueue(workRequest)

            /*val workRequest = androidx.work.PeriodicWorkRequest.Builder(
                    MyWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                    TimeUnit.MILLISECONDS).setConstraints(constraints).build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                    "demorepeate", ExistingPeriodicWorkPolicy.REPLACE, workRequest
            )*/
        }
    }
}