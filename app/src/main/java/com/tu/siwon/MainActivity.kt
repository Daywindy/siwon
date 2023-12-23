@file:Suppress("UNREACHABLE_CODE")

package com.tu.siwon

import android.Manifest
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

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // 구글 지도 및 현재 위치 관련 변수
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocationMarker: Marker? = null

    // UI 관련 변수
    private lateinit var mapContainer: FrameLayout
    private lateinit var placeListView: ListView
    private lateinit var buttonsContainer: LinearLayout
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var backButton: Button
    private lateinit var recommendationsButton: Button
    private lateinit var showFamilyCourseButton: Button
    private lateinit var showDateCourseButton: Button
    private lateinit var showChildCourseButton: Button
    private lateinit var currentCategory: String

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
        Place("은행도서관", "문화시설", 41.3528, 126.744),
        Place("오이도선사유적공원", "자연명소", 38.3428, 126.6955),
        Place("하늘휴게소", "자연명소", 38.3839, 126.8554),
        Place("시흥관광호텔", "자연명소", 38.3379, 126.7499),
        Place("놀숲시흥은행점", "자연명소", 38.4451, 126.7988),
        Place("월곶예술공판장", "자연명소", 38.3862, 126.7406),
        Place("용도 수목원", "관광지", 38.4193, 126.8264)

    )

    private val familyCoursePlaces = listOf(
        Place("연꽃테마파크", "가족코스", 38.3836, 126.7224),
        Place("갯골생태공원", "가족코스", 38.3895, 126.7808),
        Place("오이도", "가족코스", 38.3791, 126.7375),
        Place("오이도선사유적공원", "가족코스", 38.3793, 126.7456),
        Place("하늘휴게소", "가족코스", 38.3847, 126.7505),
        Place("시흥관광호텔핀", "가족코스", 38.3797, 126.7341)
    )

    private val dateCoursePlaces = listOf(
        Place("놀숲 시흥은행점", "연인데이트코스", 38.3734, 126.7339),
        Place("월곶예술공판장 아트독", "연인데이트코스", 38.3877, 126.7429),
        Place("시흥 프리미엄 아울렛", "연인데이트코스", 38.3803, 126.7293),
        Place("오이도 빨강등대", "연인데이트코스", 38.3814, 126.7348)
    )

    private val childFamilyCoursePlaces = listOf(
        Place("갯골생태공원", "아동가족코스", 38.3895, 126.7808),
        Place("용도 수목원", "아동가족코스", 38.3802, 126.7272),
        Place("은계호수공원", "아동가족코스", 38.3911, 126.7716),
        Place("오이도", "아동가족코스", 38.3791, 126.7375)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 지도 및 위치 서비스 초기화
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // UI 요소 초기화
        mapContainer = findViewById(R.id.mapContainer)
        placeListView = findViewById(R.id.placeLitsView)
        buttonsContainer = findViewById(R.id.buttonsContainer)
        backButton = findViewById(R.id.backButton)
        recommendationsButton = findViewById(R.id.showRecommendationsButton)
        showFamilyCourseButton = findViewById(R.id.showFamilyCourseButton)
        showDateCourseButton = findViewById(R.id.showDateCourseButton)
        showChildCourseButton = findViewById(R.id.showChildCourseButton)

        // 지도 클릭 시 마커 추가
        mMap.setOnMapClickListener { point ->
            addMarker(point.latitude, point.longitude, "직접 추가한 장소")
        }

        // 뒤로 가기 버튼 클릭 시 메인 페이지로 돌아가기
        backButton.setOnClickListener {
            showMainPage()
        }

        // 관광지 버튼 클릭 시 관광지 페이지 보여주기
        findViewById<Button>(R.id.showTouristSpotsButton).setOnClickListener {
            showPlacesPage(placeList.filter { it.course == "관광지" }, "관광지")
        }

        // 문화시설 버튼 클릭 시 문화시설 페이지 보여주기
        findViewById<Button>(R.id.showCulturalFacilitiesButton).setOnClickListener {
            showPlacesPage(placeList.filter { it.course == "문화시설" }, "문화시설")
        }

        // 자연명소 버튼 클릭 시 자연명소 페이지 보여주기
        findViewById<Button>(R.id.showNaturalSpotsButton).setOnClickListener {
            showPlacesPage(placeList.filter { it.course == "자연명소" }, "자연명소")
        }

        // 가족 코스 버튼 클릭 시 가족 코스 페이지 보여주기
        showFamilyCourseButton.setOnClickListener {
            showFamilyCoursePage()
        }

        // 데이트 코스 버튼 클릭 시 데이트 코스 페이지 보여주기
        showDateCourseButton.setOnClickListener {
            showDateCoursePage()
        }

        // 아동 코스 버튼 클릭 시 아동 코스 페이지 보여주기
        showChildCourseButton.setOnClickListener {
            showChildCoursePage()
        }

        // 데이트 코스 버튼 클릭 시 데이트 코스 페이지 보여주기
        findViewById<Button>(R.id.showDateCourseButton).setOnClickListener {
            showPlacesPage(dateCoursePlaces, "데이트 코스")
        }

        // 추천 코스 버튼 클릭 시 추천 페이지 보여주기
        recommendationsButton.setOnClickListener {
            showRecommendationsPage()
        }

        // 현재 위치 버튼 클릭 시 현재 위치 보여주기
        findViewById<Button>(R.id.showCurrentLocationButton).setOnClickListener {
            showCurrentLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 권한이 허용되었는지 확인하고 현재 위치 설정
        if (checkLocationPermission()) {
            mMap.isMyLocationEnabled = true
            showCurrentLocation()
        }

        // 지도를 클릭하면 해당 위치에 마커 추가
        mMap.setOnMapClickListener { point ->
            addMarker(point.latitude, point.longitude, "직접 추가한 장소")
        }
    }

    // 메인 페이지로 전환하는 함수
    private fun showMainPage() {
        buttonsContainer.visibility = View.VISIBLE
        placeListView.visibility = View.GONE
    }

    // 추천 페이지로 전환하는 함수
    private fun showRecommendationsPage() {
        buttonsContainer.visibility = View.GONE
        placeListView.visibility = View.VISIBLE

        currentCategory = "추천 코스"

        // 추천 코스 목록 설정
        val recommendedPlaces = getRecommendedPlaces()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, recommendedPlaces.map { it.name })
        placeListView.adapter = adapter

        // 장소를 클릭하면 해당 위치로 이동 및 마커 추가
        placeListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = recommendedPlaces[position]
            val latLng = LatLng(selectedPlace.latitude, selectedPlace.longitude)
            addMarker(latLng.latitude, latLng.longitude, selectedPlace.name)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    // 가족 코스 페이지로 전환하는 함수
    private fun showFamilyCoursePage() {
        showPlacesPage(familyCoursePlaces, "가족 코스")
    }

    // 데이트 코스 페이지로 전환하는 함수
    private fun showDateCoursePage() {
        showPlacesPage(dateCoursePlaces, "데이트 코스")
    }

    // 아동 코스 페이지로 전환하는 함수
    private fun showChildCoursePage() {
        showPlacesPage(childFamilyCoursePlaces, "아동 가족 코스")
    }

    // 장소 페이지를 보여주는 함수
    private fun showPlacesPage(places: List<Place>, category: String) {
        buttonsContainer.visibility = View.GONE
        placeListView.visibility = View.VISIBLE

        currentCategory = category

        // 선택된 카테고리에 따라 장소 정렬
        val sortedPlaces = places.sortedBy { calculateDistance(it.latitude, it.longitude) }

        // 어댑터 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, sortedPlaces.map { it.name })
        placeListView.adapter = adapter

        // 장소를 클릭하면 해당 위치로 이동 및 마커 추가
        placeListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = sortedPlaces[position]
            val latLng = LatLng(selectedPlace.latitude, selectedPlace.longitude)
            addMarker(latLng.latitude, latLng.longitude, selectedPlace.name)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    // 거리 계산 함수

    private fun calculateDistance(latitude: Double, longitude: Double): Float {
        val currentLocation = mMap.myLocation
        val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val placeLatLng = LatLng(latitude, longitude)
        return FloatArray(2).apply {
            Location.distanceBetween(
                currentLatLng.latitude,
                currentLatLng.longitude,
                placeLatLng.latitude,
                placeLatLng.longitude,
                this
            )
        }[0]
    }

    // 마커 추가 함수
    private fun addMarker(latitude: Double, longitude: Double, title: String, isCurrentLocation: Boolean = false) {
        val markerOptions = MarkerOptions()
            .position(LatLng(latitude, longitude))
            .title(title)

        // 현재 위치인 경우 파란색 마커로 표시
        if (isCurrentLocation) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            currentLocationMarker?.remove()
            currentLocationMarker = mMap.addMarker(markerOptions)
        } else {
            // 그 외의 경우 기본 마커로 표시
            mMap.addMarker(markerOptions)
        }
    }

    // 현재 위치 보여주는 함수
    private fun showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    addMarker(currentLatLng.latitude, currentLatLng.longitude, "현재 위치", true)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                }
            }
    }
    // 권한 확인 함수
    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                2
            )
            return false
        }
        return true
    }

    // 추천 장소 반환 함수
    private fun getRecommendedPlaces(): List<Place> {
        // 간단한 추천 로직 (여기서는 가족 코스 추천)
        return familyCoursePlaces
    }

    // 장소 데이터 클래스
    data class Place(val name: String, val course: String, val latitude: Double, val longitude: Double)
}
