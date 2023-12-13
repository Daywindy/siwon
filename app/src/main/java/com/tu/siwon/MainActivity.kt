package com.tu.siwon

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

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
        // ... (rest of the places)
        Place("은행도서관", "문화시설", 37.3528, 126.744),
    )

    private lateinit var btnBackToMainMapFromList: Button
    private lateinit var btnTouristAttractionsFromList: Button
    private lateinit var btnCulturalFacilitiesFromList: Button
    private lateinit var btnNaturalSitesFromList: Button

    private lateinit var btnFamilyCourse: Button
    private lateinit var btnCoupleCourse: Button
    private lateinit var btnChildFamilyCourse: Button

    private var screenHeight: Int = 0
    private var listViewHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapContainer = findViewById(R.id.mapContainer)
        placeListView = findViewById(R.id.placeListView)
        buttonsContainer = findViewById(R.id.buttonsContainer)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnBackToMainMapFromList = findViewById(R.id.btnBackToMainMapFromList)
        btnTouristAttractionsFromList = findViewById(R.id.btnTouristAttractionsFromList)
        btnCulturalFacilitiesFromList = findViewById(R.id.btnCulturalFacilitiesFromList)
        btnNaturalSitesFromList = findViewById(R.id.btnNaturalSitesFromList)

        btnFamilyCourse = findViewById(R.id.btnFamilyCourse)
        btnCoupleCourse = findViewById(R.id.btnCoupleCourse)
        btnChildFamilyCourse = findViewById(R.id.btnChildFamilyCourse)

        btnFamilyCourse.setOnClickListener {
            showRecommendationMap("가족코스")
        }

        btnCoupleCourse.setOnClickListener {
            showRecommendationMap("연인코스")
        }

        btnChildFamilyCourse.setOnClickListener {
            showRecommendationMap("아동가족코스")
        }

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
            showPlacesList()
        }

        findViewById<Button>(R.id.btnBackToMainMap).setOnClickListener {
            showMainMap()
        }

        btnBackToMainMapFromList.setOnClickListener {
            showMainMap()
        }

        btnTouristAttractionsFromList.setOnClickListener {
            showPlacesOnMap("관광지")
        }

        btnCulturalFacilitiesFromList.setOnClickListener {
            showPlacesOnMap("문화시설")
        }

        btnNaturalSitesFromList.setOnClickListener {
            showPlacesOnMap("자연명소")
        }

        screenHeight = resources.displayMetrics.heightPixels
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val koreaLocation = LatLng(35.9078, 127.7669)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koreaLocation, 7f))

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun showMainMap() {
        mapContainer.visibility = View.VISIBLE
        placeListView.visibility = View.GONE
        buttonsContainer.visibility = View.VISIBLE
        findViewById<Button>(R.id.btnBackToMainMap).visibility = View.GONE

        mMap.clear()

        val koreaLocation = LatLng(35.9078, 127.7669)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koreaLocation, 7f))

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun showPlacesOnMap(category: String) {
        mapContainer.visibility = View.VISIBLE
        placeListView.visibility = View.VISIBLE
        buttonsContainer.visibility = View.VISIBLE
        findViewById<Button>(R.id.btnBackToMainMap).visibility = View.VISIBLE
        mMap.clear()

        val filteredPlaces = places.filter { it.category == category }

        if (filteredPlaces.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        val sortedPlaces = filteredPlaces.sortedBy {
                            val distance = FloatArray(1)
                            Location.distanceBetween(
                                it.latitude,
                                it.longitude,
                                location.latitude,
                                location.longitude,
                                distance
                            )
                            distance[0].toDouble()
                        }

                        for (place in sortedPlaces) {
                            val placeLocation = LatLng(place.latitude, place.longitude)
                            boundsBuilder.include(placeLocation)
                            mMap.addMarker(MarkerOptions().position(placeLocation).title(place.name))
                        }

                        val bounds = boundsBuilder.build()
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100, 400, 0))

                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sortedPlaces.map { it.name })
                        placeListView.adapter = adapter

                        placeListView.setOnItemClickListener { _, _, position, _ ->
                            val selectedPlace = sortedPlaces[position]
                            showPlacesOnMap(selectedPlace.category)
                        }

                        listViewHeight = placeListView.height

                        if (listViewHeight > screenHeight * 2 / 3) {
                            placeListView.layoutParams.height = (screenHeight * 2 / 3)
                            placeListView.layoutParams = placeListView.layoutParams
                            placeListView.requestLayout()
                            placeListView.setOnTouchListener { _, event ->
                                if (event.action == MotionEvent.ACTION_MOVE) {
                                    return@setOnTouchListener true
                                }
                                false
                            }
                        } else {
                            placeListView.setOnTouchListener(null)
                        }
                    } else {
                        Toast.makeText(this, "Unable to get current location.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "해당 카테고리에 장소가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecommendationMap(courseType: String) {
        mapContainer.visibility = View.VISIBLE
        placeListView.visibility = View.VISIBLE
        buttonsContainer.visibility = View.VISIBLE
        findViewById<Button>(R.id.btnBackToMainMap).visibility = View.VISIBLE
        mMap.clear()

        val filteredPlaces = places.filter { it.category == courseType }

        if (filteredPlaces.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        val sortedPlaces = filteredPlaces.sortedBy {
                            val distance = FloatArray(1)
                            Location.distanceBetween(
                                it.latitude,
                                it.longitude,
                                location.latitude,
                                location.longitude,
                                distance
                            )
                            distance[0].toDouble()
                        }

                        for (place in sortedPlaces) {
                            val placeLocation = LatLng(place.latitude, place.longitude)
                            boundsBuilder.include(placeLocation)
                            mMap.addMarker(MarkerOptions().position(placeLocation).title(place.name))
                        }

                        val bounds = boundsBuilder.build()
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100, 400, 0))

                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sortedPlaces.map { it.name })
                        placeListView.adapter = adapter

                        placeListView.setOnItemClickListener { _, _, position, _ ->
                            val selectedPlace = sortedPlaces[position]
                            showRecommendationMap(selectedPlace.category)
                        }

                        listViewHeight = placeListView.height

                        if (listViewHeight > screenHeight * 2 / 3) {
                            placeListView.layoutParams.height = (screenHeight * 2 / 3)
                            placeListView.layoutParams = placeListView.layoutParams
                            placeListView.requestLayout()
                            placeListView.setOnTouchListener { _, event ->
                                if (event.action == MotionEvent.ACTION_MOVE) {
                                    return@setOnTouchListener true
                                }
                                false
                            }
                        } else {
                            placeListView.setOnTouchListener(null)
                        }
                    } else {
                        Toast.makeText(this, "Unable to get current location.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "해당 카테고리에 장소가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPlacesList() {
        mapContainer.visibility = View.GONE
        placeListView.visibility = View.VISIBLE
        buttonsContainer.visibility = View.GONE
        findViewById<Button>(R.id.btnBackToMainMap).visibility = View.VISIBLE

        val filteredPlaces = places.filter { it.category == "선호 장소 카테고리" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredPlaces.map { it.name })
        placeListView.adapter = adapter

        placeListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = filteredPlaces[position]
            showRecommendationMap(selectedPlace.category)
        }
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

                        currentLocationMarker?.showInfoWindow()
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
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
