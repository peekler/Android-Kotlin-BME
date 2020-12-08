package hu.bme.aut.mapdemo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_conf)
        mMap.setMapStyle(mapStyleOptions)


        mMap.isTrafficEnabled = true

        switchMapType.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            } else  {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        val hungaryMarker = LatLng(47.0, 19.0)
        mMap.addMarker(MarkerOptions().position(hungaryMarker).title("Marker in Hungary"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hungaryMarker))

        mMap.setOnMapClickListener {
            var newMarker = mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("My marker")
                    .snippet("long text of the marker")
            )

            newMarker.isDraggable = true

            //mMap.animateCamera(CameraUpdateFactory.newLatLng(it))

            val random = Random(System.currentTimeMillis())
            val cameraPostion = CameraPosition.Builder()
                    .target(it)
                    .zoom(5f + random.nextInt(10))
                    .tilt(30f + random.nextInt(15))
                    .bearing(45f + random.nextInt(45))
                    .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPostion))
        }



        val polyRect: PolygonOptions = PolygonOptions().add(
            LatLng(44.0, 19.0),
            LatLng(44.0, 26.0),
            LatLng(48.0, 26.0),
            LatLng(48.0, 19.0)
        )
        val polygon: Polygon = mMap.addPolygon(polyRect)
        polygon.fillColor = Color.argb(25, 0, 255, 0)

        val polyLineOpts = PolylineOptions().add(
            LatLng(54.0, 19.0),
            LatLng(54.0, 26.0),
            LatLng(44.0, 12.0),
            LatLng(58.0, 26.0)
        )
        val polyline = mMap.addPolyline(polyLineOpts)

        polyline.color = Color.GREEN

    }
}