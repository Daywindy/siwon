package com.tu.siwon
import com.tu.siwon.R

import android.content.pm.PackageManager
import android.graphics.Color
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

        // 지도 프래그먼트 초기화 및 레이아웃에 추가
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapContainer = findViewById(R.id.mapContainer)
        placeListView = findViewById(R.id.placeListView)
        buttonsContainer = findViewById(R.id.buttonsContainer)

        // 가족 코스 추천 버튼 클릭 시
        val showFamilyCourseButton: Button = findViewById(R.id.showFamilyCourseButton)
        showFamilyCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(
                listOf(
                    Place("연꽃테마파크", "가족코스", 37.3836, 126.7224),
                    Place("갯골생태공원", "가족코스", 37.3895, 126.7808),
                    Place("오이도", "가족코스", 37.3791, 126.7375),
                    Place("오이도선사유적공원", "가족코스", 37.3793, 126.7456),
                    Place("하늘휴게소", "가족코스", 37.3847, 126.7505),
                    Place("시흥관광호텔핀", "가족코스", 37.3797, 126.7341)
                )
            )
        }

        // 연인 데이트 코스 추천 버튼 클릭 시
        val showDateCourseButton: Button = findViewById(R.id.showDateCourseButton)
        showDateCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(
                listOf(
                    Place("놀숲 시흥은행점", "연인데이트코스", 37.3734, 126.7339),
                    Place("월곶예술공판장 아트독", "연인데이트코스", 37.3877, 126.7429),
                    Place("시흥 프리미엄 아울렛", "연인데이트코스", 37.3803, 126.7293),
                    Place("오이도 빨강등대", "연인데이트코스", 37.3814, 126.7348)
                )
            )
        }

        // 아동 가족 코스 추천 버튼 클릭 시
        val showChildFamilyCourseButton: Button = findViewById(R.id.showChildFamilyCourseButton)
        showChildFamilyCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(
                listOf(
                    Place("갯골생태공원", "아동가족코스", 37.3895, 126.7808),
                    Place("용도 수목원", "아동가족코스", 37.3802, 126.7272),
                    Place("은계호수공원", "아동가족코스", 37.3911, 126.7716),
                    Place("오이도", "아동가족코스", 37.3791, 126.7375)
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

    // ... (your existing code for location permission, device location, etc.)

    private fun checkLocationPermission() {
        // Check for location permission
        // ...
    }

    private data class Place(val name: String, val type: String, val latitude: Double, val longitude: Double)
}
