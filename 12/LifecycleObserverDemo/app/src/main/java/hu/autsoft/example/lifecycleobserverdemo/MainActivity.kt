package hu.autsoft.example.lifecycleobserverdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult


class MainActivity : AppCompatActivity() {

    companion object {
        private const val RC_LOCATION = 156
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initLocation()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RC_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RC_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    private fun initLocation() {
        lifecycle.addObserver(LifecycleAwareLocationService(this, LoggingCallback()))
    }

    private class LoggingCallback : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val lastLocation = result.lastLocation
            Log.d("LoggingCallback", "new location: $lastLocation")
        }
    }

}
