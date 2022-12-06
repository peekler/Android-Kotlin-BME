package hu.bme.aut.mytracker

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import kotlin.jvm.Throws

class MainLocationLiveData(context: Context) : LiveData<Location>() {

    interface OnNewLocationAvailable {
        fun onNewLocation(location: Location)
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            value = locationResult.lastLocation
        }
    }

    override fun onActive() {
        super.onActive()
        startLocationMonitoring()
    }

    override fun onInactive() {
        super.onInactive()
        stopLocationMonitoring()
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