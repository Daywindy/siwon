package com.tu.siwon

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.currentLocationButton).setOnClickListener {
            checkLocationPermission()
        }

        // Add listeners for other buttons
        findViewById<Button>(R.id.touristAttractionsButton).setOnClickListener {
            // Implement the action for the tourist attractions button
            // For example: showTouristAttractionsOnMap()
        }

        findViewById<Button>(R.id.culturalSitesButton).setOnClickListener {
            // Implement the action for the cultural sites button
            // For example: showCulturalSitesOnMap()
        }

        findViewById<Button>(R.id.naturalAttractionsButton).setOnClickListener {
            // Implement the action for the natural attractions button
            // For example: showNaturalAttractionsOnMap()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Default location (Seoul)
        val defaultLocation = LatLng(37.5665, 126.9780)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 7f))

        // Enable zoom controls
        mMap.uiSettings.isZoomControlsEnabled = true
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
                        mMap.clear() // Clear previous markers
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                        // Create a new marker
                        currentLocationMarker?.remove() // Remove the previous marker if exists
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
                    // Handle the case where the user denied the permission
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

