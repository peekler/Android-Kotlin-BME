package hu.autsoft.example.lifecycleobserverdemo

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.support.annotation.RequiresPermission
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LifecycleAwareLocationService(
        private val context: Context,
        private val callback: LocationCallback)
    : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        val request = LocationRequest().apply {
            interval = 1000L
            fastestInterval = 100L
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(context)
                    .requestLocationUpdates(request, callback, Looper.getMainLooper())
            Log.d("LifecycleAwareLocation", "Started location updates")
        } else {
            Log.d("LifecycleAwareLocation", "Couldn't location updates (no permission)")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        LocationServices.getFusedLocationProviderClient(context)
                .removeLocationUpdates(callback)
        Log.d("LifecycleAwareLocation", "Stopped location updates")
    }

}
