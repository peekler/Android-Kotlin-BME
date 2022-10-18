package hu.bme.aut.broadcastreceiverdemo

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var airPlaneReceiver: AirModeReceiever

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        airPlaneReceiver = AirModeReceiever()

        registerReceiver(
                airPlaneReceiver,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        requestNeededPermission()
    }

    override fun onDestroy() {
        unregisterReceiver(
                airPlaneReceiver
        )

        super.onDestroy()
    }


    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED),
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
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "RECEIVE_SMS and call and boot perm granted", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this@MainActivity,
                            "RECEIVE_SMS and call and boot perm NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}