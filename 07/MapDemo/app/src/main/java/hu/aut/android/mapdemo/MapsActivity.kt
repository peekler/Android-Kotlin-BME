package hu.aut.android.mapdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val myMarker = LatLng(47.0, 19.0)
        mMap.addMarker(MarkerOptions().position(myMarker).title("Marker in Hungary"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myMarker))

        //mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        //mMap.isTrafficEnabled = true


        mMap.setOnMapClickListener {
            val markerHU = mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Hello")
                    .snippet("Lakosság: 9.700.000"))
            markerHU.isDraggable = true

            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
        }



    }
}
