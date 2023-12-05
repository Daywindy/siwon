package com.tu.siwon


import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
            getDeviceLocation()
        }

        // Add listeners for other buttons
        // ... (버튼들의 리스너들 추가)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Default location (Seoul)
        val defaultLocation = LatLng(37.5665, 126.9780)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 7f))

        // Enable zoom controls
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    @SuppressLint("MissingPermission")
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
}