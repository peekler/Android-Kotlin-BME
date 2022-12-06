package hu.bme.aut.mytimeservicedemo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.mytimeservicedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.FOREGROUND_SERVICE)
    }

    lateinit var binding: ActivityMainBinding

    private var timeService : MyTimeService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentMyTimeService = Intent(this,
            MyTimeService::class.java)
        binding.btnStart.setOnClickListener {
            startService(intentMyTimeService)

//            bindService(
//                Intent(this, MyTimeService::class.java),
//                serviceConnection,
//                Context.BIND_AUTO_CREATE
//            )
        }
        binding.btnStop.setOnClickListener {
            stopService(intentMyTimeService)
            unbindService(serviceConnection)
        }
        binding.btnSetTime.setOnClickListener {
            if (!TextUtils.isEmpty(binding.etInterval.text)) {
                timeService?.changeInterval(binding.etInterval.text.toString().toLong())
            } else {
                binding.etInterval.error = "This field can not be empty"
            }
        }


        if (allPermissionsGranted()) {
            setViews()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setViews()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setViews() {
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = true

        bindService(
            Intent(this, MyTimeService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            //
        }

        override fun onServiceConnected(
            name: ComponentName, serviceBinder: IBinder) {
            timeService = (serviceBinder as MyTimeService.TimeServiceBinder).service

            Toast.makeText(this@MainActivity, "Bind ready", Toast.LENGTH_LONG).show()

            binding.btnSetTime.isEnabled = true
        }
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }
}