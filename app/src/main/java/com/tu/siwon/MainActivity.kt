package com.tu.siwon

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
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
    private var category: String = ""
    private val placeList = listOf(
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
        Place("소래산산림욕장", "관광지", 37.3369, 126.7443),
        Place("호반샘 공원", "자연명소", 37.3786, 126.7281),
        Place("시화호수공원", "자연명소", 37.3892, 126.7565),
        Place("이사벨 목장", "문화시설", 37.4375, 126.7972),
        Place("중부도서관", "문화시설", 37.3812, 126.7342),
        Place("교동도서관", "문화시설", 37.4461, 126.7846),
        Place("은행도서관", "문화시설", 37.3528, 126.744),
        Place("물왕호수", "자연명소", 37.3827, 126.8335),
        Place("시흥시 연꽃테마파크", "자연명소", 37.4024, 126.8066),
        Place("소래산산림욕장", "자연명소", 37.3413, 126.7785),
        Place("시흥 오이도 선사유적공원", "자연명소", 37.3429, 126.6959),
        Place("옥구공원", "자연명소", 37.3557, 126.7128),
        Place("시흥 오이도 선사유적공원", "자연명소", 37.3366, 126.6908),
        Place("창조자연사박물관", "문화시설", 37.4336, 126.7861),
        Place("온동물체험농장", "문화시설", 37.4147, 126.7637),
        Place("따요딸기감귤농장", "관광지", 37.3902, 126.8183),
        Place("용도수목원", "관광지", 37.4192, 126.8264),
        Place("거북섬", "관광지", 37.3208, 126.6792),
        Place("관곡지", "자연명소", 37.4025, 126.8048),
        Place("계수밤농장", "문화시설", 37.4418, 126.8183),
        Place("배곧한울공원 해수체험장", "관광지", 37.354, 126.7011),
        Place("소래산산림욕장", "자연명소", 37.4512, 126.7793),
        Place("달월숲", "관광지", 37.38153959, 126.74884376),
        Place("시흥승마힐링체험장", "관광지", 37.3697988, 126.7722087),
        Place("파라다이브", "관광지", 37.3264, 126.6831),
        Place("시흥 오이도 선사유적공원", "관광지", 37.3427, 126.6955),
        Place("소전미술관", "문화시설", 37.4521, 126.7855),
        Place("벅스월드 곤충 체험 농장", "문화시설", 37.3534, 126.7114),
        Place("어린이안전체험관 KIDSEPIA", "문화시설", 37.466, 126.8022),
        Place("시흥시농업기술센터천문관", "관광지", 37.4008, 126.8084),
        Place("시흥 오이도 선사유적공원 전망대", "자연명소", 37.3475, 126.6938),
        Place("워너두 칠드런스 뮤지엄", "관광지", 37.446, 126.8022),
        Place("배곧생명공원", "자연명소", 37.3727, 126.722),
        Place("창조자연사박물관", "관광지", 37.4337, 126.7867),
        Place("비둘기공원", "자연명소", 37.4427, 126.9072),
        Place("산들공원", "자연명소", 37.3475, 126.7807),
        Place("정왕동제4호체육공원", "문화시설", 37.3668, 126.7326),
        Place("웨이브파크", "문화시설", 37.323, 126.6818),
        Place("다이나믹온 시흥플랑드르", "문화시설", 37.371, 126.808),
        Place("물왕숲캠핑파크", "관광지", 37.3809, 126.8273),
        Place("성담스퀘어", "문화시설", 37.3459, 126.7369),
        Place("솔내아트센터", "문화시설", 37.4345, 126.786),
        Place("벅스와인드링 제트보트체험장", "관광지", 37.3765, 126.8032),
        Place("화문섬", "자연명소", 37.3356, 126.6908),
        Place("천체관측대", "문화시설", 37.3964, 126.8093),
        Place("오이도선사유적공원", "자연명소", 37.3428, 126.6955),
        Place("하늘휴게소", "자연명소", 37.3839, 126.8554),
        Place("시흥관광호텔", "자연명소", 37.3379, 126.7499),
        Place("놀숲시흥은행점", "자연명소", 37.4451, 126.7988),
        Place("월곶예술공판장", "자연명소", 37.3862, 126.7406),
        Place("용도 수목원", "관광지", 37.4193, 126.8264),
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
        Place("소래산산림욕장", "관광지", 37.3369, 126.7443),
        Place("호반샘 공원", "자연명소", 37.3786, 126.7281),
        Place("시화호수공원", "자연명소", 37.3892, 126.7565),
        Place("이사벨 목장", "문화시설", 37.4375, 126.7972),
        Place("중부도서관", "문화시설", 37.3812, 126.7342),
        Place("교동도서관", "문화시설", 37.4461, 126.7846),
        Place("은행도서관", "문화시설", 37.3528, 126.744),
        Place("오이도선사유적공원", "자연명소", 37.3428, 126.6955),
        Place("하늘휴게소", "자연명소", 37.3839, 126.8554),
        Place("시흥관광호텔", "자연명소", 37.3379, 126.7499),
        Place("놀숲시흥은행점", "자연명소", 37.4451, 126.7988),
        Place("월곶예술공판장", "자연명소", 37.3862, 126.7406),
        Place("용도 수목원", "관광지", 37.4193, 126.8264)

    )

    private val familyCoursePlaces = listOf(
        Place("연꽃테마파크", "가족코스", 37.3836, 126.7224),
        Place("갯골생태공원", "가족코스", 37.3895, 126.7808),
        Place("오이도", "가족코스", 37.3791, 126.7375),
        Place("오이도선사유적공원", "가족코스", 37.3793, 126.7456),
        Place("하늘휴게소", "가족코스", 37.3847, 126.7505),
        Place("시흥관광호텔핀", "가족코스", 37.3797, 126.7341)
    )

    private val dateCoursePlaces = listOf(
        Place("놀숲 시흥은행점", "연인데이트코스", 37.3734, 126.7339),
        Place("월곶예술공판장 아트독", "연인데이트코스", 37.3877, 126.7429),
        Place("시흥 프리미엄 아울렛", "연인데이트코스", 37.3803, 126.7293),
        Place("오이도 빨강등대", "연인데이트코스", 37.3814, 126.7348)
    )

    private val childFamilyCoursePlaces = listOf(
        Place("갯골생태공원", "아동가족코스", 37.3895, 126.7808),
        Place("용도 수목원", "아동가족코스", 37.3802, 126.7272),
        Place("은계호수공원", "아동가족코스", 37.3911, 126.7716),
        Place("오이도", "아동가족코스", 37.3791, 126.7375)
    )

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

        // 현재 위치 버튼 클릭 시
        val showCurrentLocationButton: Button = findViewById(R.id.showCurrentLocationButton)
        showCurrentLocationButton.setOnClickListener {
            showCurrentLocation()
        }

        // 가족 코스 추천 버튼 클릭 시
        val showFamilyCourseButton: Button = findViewById(R.id.showFamilyCourseButton)
        showFamilyCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(familyCoursePlaces)
        }

        // 연인 데이트 코스 추천 버튼 클릭 시
        val showDateCourseButton: Button = findViewById(R.id.showDateCourseButton)
        showDateCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(dateCoursePlaces)
        }

        // 아동 가족 코스 추천 버튼 클릭 시
        val showChildFamilyCourseButton: Button = findViewById(R.id.showChildFamilyCourseButton)
        showChildFamilyCourseButton.setOnClickListener {
            showPlacesOnMapWithLines(childFamilyCoursePlaces)
        }

        // 관광지 버튼 클릭 시
        val showTouristSpotsButton: Button = findViewById(R.id.showTouristSpotsButton)
        showTouristSpotsButton.setOnClickListener {
            showPlacesPage("관광지")
        }

        // 자연명소 버튼 클릭 시
        val showNaturalSpotsButton: Button = findViewById(R.id.showNaturalSpotsButton)
        showNaturalSpotsButton.setOnClickListener {
            showPlacesPage("자연명소")
        }

        // 문화시설 버튼 클릭 시
        val showCulturalFacilitiesButton: Button = findViewById(R.id.showCulturalFacilitiesButton)
        showCulturalFacilitiesButton.setOnClickListener {
            showPlacesPage("문화시설")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 사용자 위치 권한 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        }

        // 현재 위치 버튼 활성화
        mMap.isMyLocationEnabled = true

        // 지도 클릭 시 마커 추가
        mMap.setOnMapClickListener { point ->
            addMarker(point.latitude, point.longitude, "직접 추가한 장소")
        }

        // 초기 위치 설정
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
    }

    // 장소 목록을 맵에 표시하고, 선으로 연결하는 함수
    private fun showPlacesOnMapWithLines(places: List<Place>) {
        mMap.clear() // 기존 마커 및 선 제거

        // 현재 위치 마커 추가
        showCurrentLocation()

        // 장소 목록에 있는 각 장소에 대해 마커 및 선 추가
        for (i in places.indices) {
            val place = places[i]
            val latLng = LatLng(place.latitude, place.longitude)
            addMarker(latLng.latitude, latLng.longitude, place.name)

            if (i > 0) {
                // 이전 장소와 현재 장소를 선으로 연결
                val prevPlace = places[i - 1]
                val prevLatLng = LatLng(prevPlace.latitude, prevPlace.longitude)
                addLine(prevLatLng, latLng)
            }
        }

        // 마지막 장소와 첫 번째 장소를 선으로 연결
        val firstPlace = places.first()
        val firstLatLng = LatLng(firstPlace.latitude, firstPlace.longitude)
        val lastPlace = places.last()
        val lastLatLng = LatLng(lastPlace.latitude, lastPlace.longitude)
        addLine(lastLatLng, firstLatLng)
    }

    // 마커를 추가하는 함수
    private fun addMarker(latitude: Double, longitude: Double, title: String, isCurrentLocation: Boolean = false) {
        val markerOptions = MarkerOptions()
            .position(LatLng(latitude, longitude))
            .title(title)

        if (isCurrentLocation) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        mMap.addMarker(markerOptions)
    }

    private fun addLine(start: LatLng, end: LatLng) {
        mMap.addPolyline(
            PolylineOptions()
                .add(start, end)
                .width(5f)
                .color(Color.RED)
        )
    }

    // 현재 위치를 표시하는 함수
    private fun showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // 이전 현재 위치 마커가 있으면 제거
                    currentLocationMarker?.remove()

                    // 현재 위치 마커 추가
                    val latLng = LatLng(it.latitude, it.longitude)
                    addMarker(latLng.latitude, latLng.longitude, "현재 위치", true)

                    // 지도 중심을 현재 위치로 이동
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
    }

    private fun showPlacesPage(category: String) {
        // 'category' 멤버 변수에 값을 할당
        this.category = category

        // 장소 목록을 필터링하여 특정 카테고리의 장소만 보여줌
        val filteredPlaces = placeList.filter { it.category == this.category }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredPlaces.map { it.name })
        placeListView.adapter = adapter

        // 장소 목록 중 첫 번째 장소의 위치로 지도 이동
        if (filteredPlaces.isNotEmpty()) {
            val firstPlace = filteredPlaces.first()
            val latLng = LatLng(firstPlace.latitude, firstPlace.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }
    data class Place(val name: String, val course: String, val latitude: Double, val longitude: Double)
}
