
package com.tu.swion

pacakage com.tu.swion
ipmport android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocationMarker: Marker? = null

    private lateinit var mapContainer: FrameLayout
    private lateinit var placeListView: ListView
    private lateinit var buttonsContainer: LinearLayout
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapContainer = findViewById(R.id.mapContainer)
        placeListView = findViewById(R.id.placeListView)
        buttonsContainer = findViewById(R.id.buttonsContainer)

        val showFamilyCourseButton: Button = findViewById(R.id.showFamilyCourseButton)
        showFamilyCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(
                listOf(
                    Place("시흥갯골생태공원", "자연명소", 37.3895, 126.7808),
                    // ... (add more places as needed)
                )
            )
        }

        val showDateCourseButton: Button = findViewById(R.id.showDateCourseButton)
        showDateCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(
                listOf(
                    Place("놀숲 시흥은행점", "연인데이트코스", 37.3734, 126.7339),
                    // ... (add more places as needed)
                )
            )
        }

        val showChildFamilyCourseButton: Button = findViewById(R.id.showChildFamilyCourseButton)
        showChildFamilyCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(
                listOf(
                    Place("갯골생태공원", "아동가족코스", 37.3895, 126.7808),
                    // ... (add more places as needed)
                )
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission()
    }

    private fun showPlacesOnMapWithLines(places: List<Place>) {
        mapContainer.visibility = View.VISIBLE
        placeListView.visibility = View.GONE
        buttonsContainer.visibility = View.VISIBLE
        mMap.clear()

        val boundsBuilder = LatLngBounds.builder()

        for (i in 0 until places.size) {
            val place = places[i]
            val location = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(location)

            mMap.addMarker(MarkerOptions().position(location).title(place.name))

            if (i < places.size - 1) {
                val nextPlace = places[i + 1]
                val nextLocation = LatLng(nextPlace.latitude, nextPlace.longitude)
                mMap.addPolyline(PolylineOptions().add(location, nextLocation).color(Color.BLUE))
            }
        }

        val bounds = boundsBuilder.build()
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100, 400, 0))
    }

    private fun checkLocationPermission() {
        // Check for location permission
        // ...
    }

    private data class Place(val name: String, val type: String, val latitude: Double, val longitude: Double)
}
