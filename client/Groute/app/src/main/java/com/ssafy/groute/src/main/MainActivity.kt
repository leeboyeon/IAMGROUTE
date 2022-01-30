package com.ssafy.groute.src.main

import android.Manifest
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseActivity
import com.ssafy.groute.databinding.ActivityMainBinding
import com.ssafy.groute.src.main.board.*
import com.ssafy.groute.src.main.home.HomeFragment
import com.ssafy.groute.src.main.home.PlaceDetailFragment
import com.ssafy.groute.src.main.home.PlaceFragment
import com.ssafy.groute.src.main.my.MyFragment
import com.ssafy.groute.src.main.route.RouteCreateFragment
import com.ssafy.groute.src.main.route.RouteFragment
import com.ssafy.groute.src.main.travel.TravelPlanFragment
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.LocationPermissionManager
import com.ssafy.groute.util.LocationServiceManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


private const val TAG = "MainActivity_Groute"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    //    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    // 모든 퍼미션 관련 배열
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    lateinit var locationServiceManager: LocationServiceManager
    lateinit var locationPermissionManager: LocationPermissionManager
    private val PERMISSIONS_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_layout, HomeFragment())
            .commit()

        bottomNavigation = binding.bottomNavi
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.Home -> {
//                    binding.mainProfileBar.visibility = true
                    binding.mainProfileBar.isVisible = true
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, HomeFragment())
                        .commit()
                    true
                }
                R.id.Route -> {
                    binding.mainProfileBar.isVisible = true
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, RouteFragment())
                        .commit()
                    true
                }
                R.id.Board -> {
                    binding.mainProfileBar.isVisible = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, BoardFragment())
                        .commit()
                    true
                }
                R.id.My -> {
                    binding.mainProfileBar.isVisible = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, MyFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            // 재선택시 다시 랜더링 하지 않기 위해 수정
            if(bottomNavigation.selectedItemId != item.itemId){
                bottomNavigation.selectedItemId = item.itemId
            }
        }

        initProfileBar()

        // kakao map api key hash
//        getHashKey()

        // Location
        locationPermissionManager = LocationPermissionManager(this)
        locationServiceManager = LocationServiceManager(this)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                requiredPermissions,
                PERMISSIONS_CODE
            )
        }

        binding.lifecycleOwner = this
        binding.viewModeluser = viewModel
    }
    fun openFragment(index:Int, key:String, value:Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            1 -> {
                //루트생성화면
                transaction.replace(R.id.frame_main_layout, RouteCreateFragment())
                    .addToBackStack(null)
            }
            2 -> {
                //현재 생성 중인 나의 day별 일정을 볼 수 있는 화면
                transaction.replace(R.id.frame_main_layout, TravelPlanFragment())
                    .addToBackStack(null)
            }
            3->{    // 지역별 장소 리스트 화면
                transaction.replace(R.id.frame_main_layout, PlaceFragment.newInstance(key,value))
                    .addToBackStack(null)
            }
            4 -> {  // 하나의 장소에 대한 상세 정보 출력 화면
                transaction.replace(R.id.frame_main_layout, PlaceDetailFragment.newInstance(key, value))
                    .addToBackStack(null)
            }
            5->{    // 자유 or 질문 게시판 화면
                transaction.replace(R.id.frame_main_layout, BoardDetailFragment.newInstance(key, value))
                    .addToBackStack(null)
            }
            6->{    // 게시글 1개에 대한 화면
                transaction.replace(R.id.frame_main_layout,BoardDetailDetailFragment.newInstance(key, value))
                    .addToBackStack(null)
            }
            7->{    // 사용자 여행 일정 계획 화면
                transaction.replace(R.id.frame_main_layout,TravelPlanFragment())
                    .addToBackStack(null)
            }
            8->{    // 게시판 글쓰기 화면
                transaction.replace(R.id.frame_main_layout,BoardWriteFragment.newInstance(key, value))
                    .addToBackStack(null)
            }
            9->{    // 장소 검색 화면
                transaction.replace(R.id.frame_main_layout, SearchFragment.newInstance(key, value))
                    .addToBackStack(null)
            }
        }
        transaction.commit()
    }
    fun moveFragment(index:Int, key:String, value:Int){
        openFragment(index, key, value)
    }
    fun moveFragment(index: Int){
        openFragment(index,"",0)
    }
    // 프로필바 사용자 정보 갱신
    fun initProfileBar() {
        var user = ApplicationClass.sharedPreferencesUtil.getUser()

        viewModel.getUser().observe(this, Observer {
            binding.mainTvUsername.text = it.nickname.toString()
            if(it.type.equals("sns")){
                Log.d(TAG, "initProfileBar_SNS: ${it.img}")
                Glide.with(this)
                    .load(it.img)
                    .circleCrop()
                    .into(binding.mainIvUserimg)
            } else{
                Log.d(TAG, "initProfileBar: ${it.img}")
                Glide.with(this)
                    .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                    .circleCrop()
                    .into(binding.mainIvUserimg)
            }
        })


    }

    // 메인에 상단 프로필 바를 숨기고 싶은 경우
    fun hideMainProfileBar(state : Boolean) {
        if(state) binding.mainProfileBar.visibility = View.GONE
        else binding.mainProfileBar.visibility = View.VISIBLE

    }

    // BottomNavigation을 숨기고싶은 경우
    fun hideBottomNav(state : Boolean){
        if(state) bottomNavigation.visibility =  View.GONE
        else bottomNavigation.visibility = View.VISIBLE
    }
    fun getHashKey(){
        var packageInfo :PackageInfo = PackageInfo()
        try{
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }catch(e:PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        for(signature:Signature in packageInfo.signatures){
            try{
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(TAG, "getHashKey: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
                Log.e(TAG, "getHashKey: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
            }catch (e:NoSuchAlgorithmException){
                Log.d(TAG, "getHashKey: ${signature},  ${e}")
                Log.e(TAG, "getHashKey: ${signature},  ${e}")
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE -> {
                if(grantResults.isNotEmpty()) {
                    for((i, permission) in permissions.withIndex()) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //권한 획득 실패
                            Log.i(TAG, "$permission 권한 획득에 실패하였습니다.")
                            finish()
                        }
                    }
                }
            }
        }
    }
}