package hu.bme.aut.locationandmapdemo.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlin.jvm.Throws

class MainLocationManager(context: Context,
  private val onNewLocationHandler: OnNewLocationAvailable) {

    interface OnNewLocationAvailable {
        fun onNewLocation(location: Location)
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onNewLocationHandler.onNewLocation(locationResult.lastLocation)
        }
    }

    @Throws(SecurityException::class)
    fun getLastLocation(callback: (Location) -> Unit)  {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    callback(location)
                }
            }
    }

    @Throws(SecurityException::class)
    fun startLocationMonitoring() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback, Looper.myLooper())

    }

    @Throws(SecurityException::class)
    fun stopLocationMonitoring() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}