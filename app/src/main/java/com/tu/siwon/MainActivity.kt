package com.tu.siwon

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

data class Place(val name: String, val category: String, val latitude: Double, val longitude: Double)

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocationMarker: Marker? = null

    private lateinit var mapContainer: FrameLayout
    private lateinit var placeListView: ListView
    private lateinit var buttonsContainer: LinearLayout
    private lateinit var mapFragment: SupportMapFragment

    private val places = listOf(
        Place("시흥갯골생태공원", "자연명소", 37.3895, 126.7808),
        Place("은계호수공원", "문화시설", 37.4454, 126.8065),
        // ... (other places)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapContainer = findViewById(R.id.mapContainer)
        placeListView = findViewById(R.id.placeListView)
        buttonsContainer = findViewById(R.id.buttonsContainer)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.btnTouristAttractions).setOnClickListener {
            showPlacesOnMap("관광지")
        }

        findViewById<Button>(R.id.btnCulturalFacilities).setOnClickListener {
            showPlacesOnMap("문화시설")
        }

        findViewById<Button>(R.id.btnNaturalSites).setOnClickListener {
            showPlacesOnMap("자연명소")
        }

        findViewById<Button>(R.id.btnRealTimeLocation).setOnClickListener {
            checkLocationPermission()
        }

        findViewById<Button>(R.id.btnRecommendCourse).setOnClickListener {
            showRecommendCourseButtons()
        }

        findViewById<Button>(R.id.btnFamilyCourse).setOnClickListener { showCourseMap("가족코스") }
        findViewById<Button>(R.id.btnCoupleCourse).setOnClickListener { showCourseMap("연인코스") }
        findViewById<Button>(R.id.btnChildFamilyCourse).setOnClickListener { showCourseMap("아동가족코스") }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val koreaLocation = LatLng(35.9078, 127.7669)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koreaLocation, 7f))

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun showPlacesOnMap(category: String) {
        mapContainer.visibility = View.VISIBLE
        placeListView.visibility = View.GONE
        buttonsContainer.visibility = View.VISIBLE
        mMap.clear()

        val filteredPlaces = places.filter { it.category == category }

        if (filteredPlaces.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()

            for (place in filteredPlaces) {
                val location = LatLng(place.latitude, place.longitude)
                boundsBuilder.include(location)
                mMap.addMarker(MarkerOptions().position(location).title(place.name))
            }

            val bounds = boundsBuilder.build()
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100, 400, 0))
        } else {
            Toast.makeText(this, "해당 카테고리에 장소가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPlacesList() {
        mapContainer.visibility = View.GONE
        placeListView.visibility = View.VISIBLE
        buttonsContainer.visibility = View.GONE

        val filteredPlaces = places.filter { it.category == "선호 장소 카테고리" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredPlaces.map { it.name })
        placeListView.adapter = adapter
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun getDeviceLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.clear()
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                        currentLocationMarker?.remove()
                        currentLocationMarker = mMap.addMarker(
                            MarkerOptions().position(currentLatLng).title("Your Location")
                                .snippet("You are here.")
                        )
                    } else {
                        Toast.makeText(
                            this,
                            "Unable to get location information.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceLocation()
                } else {
                    Toast.makeText(
                        this,
                        "Location permission denied. Unable to show current location.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun showRecommendCourseButtons() {
        findViewById<LinearLayout>(R.id.topButtons).visibility = View.GONE
        findViewById<LinearLayout>(R.id.bottomButtons).visibility = View.GONE
        findViewById<LinearLayout>(R.id.buttonsContainer).visibility = View.VISIBLE
    }

    private fun showCourseMap(courseType: String) {
        Toast.makeText(this, "Showing map for $courseType", Toast.LENGTH_SHORT).show()
        // Implement the logic to show the map for the selected course type
        // You can start a new activity or fragment to show the map for the selected course
        // and add a back button to return to the main map
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
