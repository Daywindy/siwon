package com.tu.siwon

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.SupportMapFragment

data class Place(
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
)

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var placeListLayout: LinearLayout
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        placeListLayout = findViewById(R.id.placeListLayout)

        initMap()

        // Initial Map View (Show entire country)
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds(LatLng(33.0, 124.0), LatLng(38.0, 131.0)), 0))

        val touristAttractionsButton: Button = findViewById(R.id.touristAttractionsButton)
        touristAttractionsButton.setOnClickListener {
            showPlacesOnMap(getPlacesByCategory("관광지"))
        }

        val naturalAttractionsButton: Button = findViewById(R.id.naturalAttractionsButton)
        naturalAttractionsButton.setOnClickListener {
            showPlacesOnMap(getPlacesByCategory("자연명소"))
        }

        val culturalPlacesButton: Button = findViewById(R.id.culturalPlacesButton)
        culturalPlacesButton.setOnClickListener {
            showPlacesOnMap(getPlacesByCategory("문화시설"))
        }

        val zoomInButton: Button = findViewById(R.id.zoomInButton)
        zoomInButton.setOnClickListener {
            map.animateCamera(CameraUpdateFactory.zoomIn())
        }

        val zoomOutButton: Button = findViewById(R.id.zoomOutButton)
        zoomOutButton.setOnClickListener {
            map.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            // You can customize the map initialization here
        }
    }

    private fun showPlacesOnMap(places: List<Place>) {
        map.clear()
        placeListLayout.removeAllViews()

        for (place in places) {
            val markerOptions = MarkerOptions()
                .position(LatLng(place.latitude, place.longitude))
                .title(place.name)
                .snippet(place.category)

            val marker = map.addMarker(markerOptions)
            marker?.tag = place

            val placeButton = Button(this)
            placeButton.text = place.name
            placeButton.setOnClickListener {
                // Handle place button click
                // You can customize this part based on your requirements
            }

            placeListLayout.addView(placeButton)
        }
    }

    private fun getPlacesByCategory(category: String): List<Place> {
        return getAllPlaces().filter { it.category == category }
    }

    private fun getAllPlaces(): List<Place> {
        return listOf(

            Place("시흥갯골생태공원", "자연명소", 37.3895, 126.7808),
            Place("은계호수공원", "문화시설", 37.4454, 126.8065),
            Place("시흥프리미엄아울렛", "문화시설", 37.3807, 126.7389),
            Place("오이도", "관광지", 37.3456, 126.6874),
            Place("시흥시중앙도서관", "문화시설", 37.3483, 126.7356),
            Place("월곶포구", "자연명소", 37.389, 126.7419),
            Place("물왕호수", "자연명소", 37.3827, 126.8335),
            Place("시흥시 연꽃테마파크", "자연명소", 37.4024, 126.8066),
            Place("소래산산림욕장", "자연명소", 37.3413, 126.7785),
            Place("시흥 오이도 선사유적공원", "자연명소", 37.3429, 126.6959),
            Place("옥구공원", "자연명소", 37.3557, 126.7128),
            Place("시흥오이도박물관", "문화시설", 37.3366, 126.6908),
            Place("창조자연사박물관", "문화시설", 37.4336, 126.7861),
            Place("온동물체험농장", "문화시설", 37.4147, 126.7637),
            Place("따요딸기감귤농장", "관광지", 37.3902, 126.8183),
            Place("용도수목원", "관광지", 37.4192, 126.8264),
            Place("거북섬", "관광지", 37.3208, 126.6792),
            Place("관곡지", "자연명소", 37.4025, 126.8048),
            Place("계수밤농장", "문화시설", 37.4418, 126.8183),
            Place("배곧한울공원 해수체험장", "관광지", 37.354, 126.7011),
            Place("소래산산림욕장", "자연명소", 37.4512, 126.7793),
            Place("달월숲", "관광지", 37.3815, 126.7488),
            Place("시흥승마힐링체험장", "관광지", 37.3698, 126.7722),
            Place("파라다이브", "관광지", 37.3264, 126.6831),
            Place("시흥 오이도 선사유적공원 전망대", "자연명소", 37.3475, 126.6938),
            Place("과림저수지", "자연명소", 37.4527, 126.826),
            Place("워너두 칠드런스 뮤지엄", "관광지", 37.446, 126.8022),
            Place("배곧생명공원", "자연명소", 37.3727, 126.722),
            Place("창조자연사박물관", "관광지", 37.4337, 126.7867),
            Place("비둘기공원", "자연명소", 37.4427, 126.9072),
            Place("산들공원", "자연명소", 37.3475, 126.7807)
            // Add the rest of the places here
        )
    }
}
