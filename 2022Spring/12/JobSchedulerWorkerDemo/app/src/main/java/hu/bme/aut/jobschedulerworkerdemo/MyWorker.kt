package hu.bme.aut.jobschedulerworkerdemo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams){

    override fun doWork(): Result {
        val handlerMain = Handler(Looper.getMainLooper())

        handlerMain.post {
            Toast.makeText(applicationContext, "WORKER - DOWORK", Toast.LENGTH_LONG).show()
        }

        return Result.success()
    }

}