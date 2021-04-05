package hu.bme.aut.locationandmapdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import hu.bme.aut.locationandmapdemo.databinding.ActivityMapsBinding
import hu.bme.aut.locationandmapdemo.location.MainLocationManager
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MainLocationManager.OnNewLocationAvailable {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<MyMarkerClusterItem>

    private lateinit var mainLocatoinManager: MainLocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainLocatoinManager = MainLocationManager(this, this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        requestNeededPermission()
    }

    private var markerCurrentPosition : Marker? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        markerCurrentPosition = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(47.0, 19.0))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
        )


        //initMap()
        //initPlaceSelect()
        //initMapAndMarkerClick()
        //drawPolygonAndLine()
        //setUpClusterer()
    }

    private fun initPlaceSelect() {
        binding.btnSelectPlace.setOnClickListener {
            val intent = PlacePicker.IntentBuilder()
                .setLatLong(47.0, 19.0)  // Initial Latitude and Longitude the Map will load into
                .showLatLong(true)  // Show Coordinates in the Activity
                .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
                .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                //.setMarkerDrawable(R.drawable.marker) // Change the default Marker Image
                .setMarkerImageImageColor(R.color.colorPrimary)
                //.setFabColor(R.color.fabColor)
                //.setPrimaryTextColor(R.color.primaryTextColor) // Change text color of Shortened Address
                //.setSecondaryTextColor(R.color.secondaryTextColor) // Change text color of full Address
                //.setBottomViewColor(R.color.bottomViewColor) // Change Address View Background Color (Default: White)
                //.setMapRawResourceStyle(R.raw.map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
                .setMapType(MapType.NORMAL)
                //.setPlaceSearchBar(true, getString(R.string.google_maps_key)) //Activate GooglePlace Search Bar. Default is false/not activated. SearchBar is a chargeable feature by Google
                .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                .hideLocationButton(true)   //Hide Location Button (Default: false)
                .disableMarkerAnimation(true)   //Disable Marker Animation (Default: false)
                .build(this)
            startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val addressData = data?.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
                Toast.makeText(
                    this,
                    "${addressData?.latitude}, ${addressData?.longitude}",
                    Toast.LENGTH_LONG
                ).show()

                mMap.animateCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            addressData!!.latitude,
                            addressData!!.longitude
                        )
                    )
                )
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun drawPolygonAndLine() {
        val polyRect: PolygonOptions = PolygonOptions().add(
            LatLng(44.0, 19.0),
            LatLng(44.0, 26.0),
            LatLng(48.0, 26.0),
            LatLng(48.0, 19.0)
        )
        val polygon: Polygon = mMap.addPolygon(polyRect)
        polygon.fillColor = Color.argb(100, 0, 255, 0)

        val polyLineOpts = PolylineOptions().add(
            LatLng(34.0, 19.0),
            LatLng(34.0, 26.0),
            LatLng(38.0, 26.0)
        )
        val polyline = mMap.addPolyline(polyLineOpts)
        polyline.color = Color.GREEN
    }

    private fun initMapAndMarkerClick() {
        mMap.setOnMapClickListener {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Marker demo")
                    .snippet("Marker details text")
            )
            marker.isDraggable = true

            val random = Random(System.currentTimeMillis())
            val cameraPostion = CameraPosition.Builder()
                .target(it)
                .zoom(5f + random.nextInt(15))
                .tilt(30f + random.nextInt(15))
                .bearing(-45f + random.nextInt(90))
                .build()

            //mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPostion))
        }

        mMap.setOnMarkerClickListener { marker ->
            Toast.makeText(
                this@MapsActivity,
                "${marker!!.position.latitude}, ${marker!!.position.longitude}",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    private fun initMap() {
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle)
        mMap.setMapStyle(mapStyleOptions)

        mMap.isTrafficEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        val markerHungary = LatLng(47.0, 19.0)
        mMap.addMarker(MarkerOptions().position(markerHungary).title("Marker in Hungary"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerHungary))

        binding.btnMapType.setOnClickListener {
            if (binding.btnMapType.isChecked) {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            } else {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
    }


    private fun setUpClusterer() {
        clusterManager = ClusterManager(this, mMap)
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
        addMarkerClusterItems()
    }


    private fun addMarkerClusterItems() {
        // Set some lat/lng coordinates to start with.
        var lat = 47.5
        var lng = 19.5

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..10) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem =
                MyMarkerClusterItem(lat, lng, "Title $i", "Snippet $i")
            clusterManager.addItem(offsetItem)
        }
    }

    override fun onNewLocation(location: Location) {
        val markerPosition = LatLng(location.latitude, location.longitude)
        markerCurrentPosition?.setPosition(markerPosition)
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(markerPosition))

        val cameraPosition = CameraPosition.Builder()
            .target(markerPosition)
            .bearing(location.bearing)
            .zoom(zoomToSpeed(location.speed))
            .tilt(tiltToSpeed(location.speed))
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        } else {
            // we have the permission
            handleLocationStart()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "ACCESS_FINE_LOCATION perm granted", Toast.LENGTH_SHORT)
                        .show()

                    handleLocationStart()
                } else {
                    Toast.makeText(
                        this,
                        "ACCESS_FINE_LOCATION perm NOT granted", Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun handleLocationStart() {
        mainLocatoinManager.startLocationMonitoring()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainLocatoinManager.stopLocationMonitoring()
    }

    private fun zoomToSpeed(speed: Float): Float {
        val maxZoom = 7.0f
        val minZoom = 10.0f
        var zoom = 0f
        zoom = if (speed > 12.5f) {
            maxZoom
        } else if (speed > 0f) {
            speed * maxZoom / 12.5f
        } else {
            0f
        }

        //Toast.makeText(this, speed+", "+(minZoom+zoom), Toast.LENGTH_LONG).show();
        return minZoom + zoom
    }

    private fun tiltToSpeed(speed: Float): Float {
        val maxTilt = 90f
        val tilt: Float
        tilt = if (speed > 12.5f) {
            maxTilt
        } else if (speed > 0f) {
            speed * maxTilt / 12.5f
        } else {
            0f
        }
        return tilt
    }
}