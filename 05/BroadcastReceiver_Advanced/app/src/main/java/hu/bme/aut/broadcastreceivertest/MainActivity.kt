package hu.bme.aut.broadcastreceivertest

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.broadcastreceivertest.databinding.ActivityMainBinding
import hu.bme.aut.broadcastreceivertest.service.BroadcastService

class MainActivity : AppCompatActivity() {


    private lateinit var airPlaneReceiver: AirPlaneReceiver
    private lateinit var screenReceiver: ScreenReceiver
    private lateinit var bootReceiver: BootReceiver
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            startService(Intent(this, BroadcastService::class.java))
        }
        binding.btnStop.setOnClickListener {
            stopService(Intent(this, BroadcastService::class.java))
        }

        airPlaneReceiver = AirPlaneReceiver()
        screenReceiver = ScreenReceiver()
        bootReceiver = BootReceiver()
        myBroadcastReceiver = MyBroadcastReceiver()

        registerReceiver(
                airPlaneReceiver,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        registerReceiver(
                screenReceiver,
                IntentFilter(Intent.ACTION_SCREEN_ON)
        )
        registerReceiver(
                screenReceiver,
                IntentFilter(Intent.ACTION_SCREEN_OFF)
        )

        registerReceiver(
                bootReceiver,
                IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        )

        registerReceiver(
                myBroadcastReceiver,
                IntentFilter("hu.bme.aut.NOTIFY")
        )

        requestNeededPermission()
    }



    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.FOREGROUND_SERVICE
                    ),
                    101)
        } else {
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "All perm granted", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this@MainActivity,
                            "Permissions NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onDestroy() {
/*        unregisterReceiver(
                airPlaneReceiver
        )

        unregisterReceiver(
                screenReceiver
        )

        unregisterReceiver(
                bootReceiver
        )

        unregisterReceiver(
                myBroadcastReceiver
        )*/

        super.onDestroy()
    }
}