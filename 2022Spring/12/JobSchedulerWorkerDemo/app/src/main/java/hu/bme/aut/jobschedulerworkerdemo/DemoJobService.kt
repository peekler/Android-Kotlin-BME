package hu.bme.aut.jobschedulerworkerdemo

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

class DemoJobService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d("TAG_DEMO","DEMO JOB SERVICE")
        Toast.makeText(this, "DEMO JOB SERVICE", Toast.LENGTH_LONG).show()

        MainActivity.scheduleJob(this)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

}