package com.ssafy.groute.src.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.nhn.android.naverlogin.OAuthLogin
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseActivity
import com.ssafy.groute.databinding.ActivityMainBinding
import com.ssafy.groute.src.api.FirebaseTokenService
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.login.LoginActivity
import com.ssafy.groute.src.main.board.*
import com.ssafy.groute.src.main.home.HomeFragment
import com.ssafy.groute.src.main.home.PlaceDetailFragment
import com.ssafy.groute.src.main.home.PlaceFragment
import com.ssafy.groute.src.main.home.ReviewWriteFragment
import com.ssafy.groute.src.main.my.MyFragment
import com.ssafy.groute.src.main.my.NotificationFragment
import com.ssafy.groute.src.main.route.RouteCreateFragment
import com.ssafy.groute.src.main.route.RouteDetailFragment
import com.ssafy.groute.src.main.route.RouteFragment
import com.ssafy.groute.src.main.route.RouteReviewWriteFragment
import com.ssafy.groute.src.main.travel.*
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.LocationPermissionManager
import com.ssafy.groute.util.LocationServiceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*


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

    // Naver Logout 인증 변수
    lateinit var mOAuthLoginInstance : OAuthLogin

    //viewModel
    private val planViewModel: PlanViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initProfileBar()
        showEventDialog()
        // Naver Logout init
        mOAuthLoginInstance = OAuthLogin.getInstance()
        mOAuthLoginInstance.init(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))


        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_layout, HomeFragment())
            .commit()

        bottomNavigation = binding.bottomNavi
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.Home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, HomeFragment())
                        .commit()
                    true
                }
                R.id.Route -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, RouteFragment())
                        .commit()
                    true
                }
                R.id.Board -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_main_layout, BoardFragment())
                        .commit()
                    true
                }
                R.id.My -> {
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
//        binding.viewModeluser = viewModel


        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            uploadToken(task.result!!, ApplicationClass.sharedPreferencesUtil.getUser().id)
//            viewModel.token = task.result!!
        })

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channel_id, "ssafy")
        }
//        createNotificationChannel(channel_id, "ssafy")
    }

    fun showEventDialog(){
        var dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_event,null)
        var dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)
        dialog.show()

        dialogView.findViewById<ImageButton>(R.id.cancleBtn).setOnClickListener {
            dialog.dismiss()
        }

    }

    fun openFragment(index:Int, key1:String, value1:Int, key2:String, value2:Int){
        val transaction = supportFragmentManager.beginTransaction()
        val fm = supportFragmentManager
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
                transaction.replace(R.id.frame_main_layout, PlaceFragment.newInstance(key1,value1))
                    .addToBackStack(null)
            }
            4 -> {  // 하나의 장소에 대한 상세 정보 출력 화면
                if(fm.findFragmentByTag("ReviewWriteFragment") != null) {
                    fm.popBackStack("TO_REVIEWWRITE_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    transaction.replace(R.id.frame_main_layout, PlaceDetailFragment.newInstance(key1, value1, key2, value2))
                        .addToBackStack(null)
                }
            }
            5->{    // 자유 or 질문 게시판 화면
                if(fm.findFragmentByTag("BoardWriteFragment") != null) {
                    fm.popBackStack("TO_BOARDWRITE_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    transaction.replace(R.id.frame_main_layout, BoardDetailFragment.newInstance(key1, value1))
                        .addToBackStack(null)
                }

            }
            6->{    // 게시글 1개에 대한 화면
                transaction.replace(R.id.frame_main_layout,BoardPostDetailFragment.newInstance(key1, value1, key2, value2))
                    .addToBackStack(null)
            }
            7->{    // 사용자 여행 일정 계획 화면
                transaction.replace(R.id.frame_main_layout,TravelPlanFragment.newInstance(key1,value1))
                    .addToBackStack(null)
            }
            8->{    // 게시판 글쓰기 화면
                transaction.replace(R.id.frame_main_layout,BoardWriteFragment.newInstance(key1, value1), "BoardWriteFragment")
                    .addToBackStack("TO_BOARDWRITE_TAG")
            }
//            9->{    // 장소 검색 화면
//                transaction.replace(R.id.frame_main_layout, SearchFragment.newInstance(key1, value1))
//                    .addToBackStack(null)
//            }
            10 ->{
                logout()
            }
            11->{
                transaction.replace(R.id.frame_main_layout, ReviewWriteFragment.newInstance(key1,value1,key2,value2), "ReviewWriteFragment")
                    .addToBackStack("TO_REVIEWWRITE_TAG")
            }

            12-> {
                if(fm.findFragmentByTag("RouteReviewWriteFragment") != null) {
                    fm.popBackStack("TO_ROUTEREVIEWWRITE_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    transaction.replace(R.id.frame_main_layout, RouteDetailFragment.newInstance(key1,value1,key2,value2))
                        .addToBackStack(null)
                }
            }
            13 ->{
                fm.popBackStack()
                bottomNavigation.selectedItemId = R.id.My
                supportFragmentManager.beginTransaction().replace(R.id.frame_main_layout, MyFragment())
                    .commit()
            }
            14 -> {
                transaction.replace(
                    R.id.frame_main_layout,
                    RouteReviewWriteFragment.newInstance(key1, value1, key2, value2),"RouteReviewWriteFragment"
                ).addToBackStack("TO_ROUTEREVIEWWRITE_TAG")
            }
            15 -> {
                transaction.replace(R.id.frame_main_layout, PlaceTmpFragment.newInstance(key1,value1))
                    .addToBackStack(null)
            }
            16 ->{
                fm.popBackStack()
                bottomNavigation.selectedItemId = R.id.Route
                supportFragmentManager.beginTransaction().replace(R.id.frame_main_layout, RouteFragment.newInstance(key1,value1, key2, value2))
                    .commit()
            }
            17 -> {
                transaction.replace(R.id.frame_main_layout, SharedMemberFragment.newInstance(key1,value1))
                    .addToBackStack(null)
            }
            18 -> {
                transaction.replace(R.id.frame_main_layout, SharePlanWriteFragment.newInstance(key1,value1))
            }
            19 -> {
                if(fm.findFragmentByTag("AccountWriteFragment") != null) {
                    fm.popBackStack("TO_ACCOUNTWRITE_TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    transaction.replace(R.id.frame_main_layout, AccountFragment.newInstance(key1, value1))
                        .addToBackStack(null)
                }
            }
            20->{
                transaction.replace(R.id.frame_main_layout, AccountWriteFragment.newInstance(key1,value1), "AccountWriteFragment")
                    .addToBackStack("TO_ACCOUNTWRITE_TAG")
            }
            21->{
                fm.popBackStack()
                bottomNavigation.selectedItemId = R.id.Board
                supportFragmentManager.beginTransaction().replace(R.id.frame_main_layout, BoardFragment())
                    .commit()
            }
            22 -> {
                // Notification List
                transaction.replace(R.id.frame_main_layout, NotificationFragment())
                    .addToBackStack(null)
            }

        }
        transaction.commit()
    }

    fun moveFragment(index:Int, key:String, value:Int){
        openFragment(index, key, value,"",0)
    }
    fun moveFragment(index:Int, key1:String, value1:Int, key2:String, value2:Int){
        openFragment(index, key1, value1, key2, value2)
    }
    fun moveFragment(index: Int){
        openFragment(index,"",0,"",0)
    }

    // 프로필바 사용자 정보 갱신
    @RequiresApi(Build.VERSION_CODES.O)
    fun initProfileBar() {
        val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

        runBlocking {
            viewModel.getUserInformation(userId, true)
            planViewModel.getMyNotendPlan(ApplicationClass.sharedPreferencesUtil.getUser().id)
        }
        viewModel.loginUserInfo.observe(this, {
            val user = User(it.id, it.nickname, it.img.toString())
            binding.user = user
        })

        if(planViewModel.planNotEndList.value?.isEmpty() == true){
            binding.mainTvDday.text = "추가하신 일정이 없습니다"
            binding.progressBar.progress = 0
        }else{
            planViewModel.planNotEndList.observe(this, Observer {
                var format = SimpleDateFormat("yyyy-MM-dd")

                var date = format.parse(it[0].startDate).time

                val today = Calendar.getInstance().time.time
                Log.d(TAG, "initProfileBar: ${today}")
                var calcur = (date-today) / (24*60*60*1000)

                Log.d(TAG, "initProfileBar: ${calcur}")
                if(calcur>= 0){
                    binding.mainTvDday.text = "여행까지 D-${calcur+1}"
                    binding.progressBar.max = 30
                    var myprogress = kotlin.math.abs(30 - calcur)
                    Log.d(TAG, "initProfileBar: ${myprogress}")
                    binding.progressBar.progress = myprogress.toInt()
                }else{
                    binding.mainTvDday.text = "현재 여행중입니다"
                    binding.progressBar.progress = 30
                }

            })
        }


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

    private fun logout(){

        viewModel.loginUserInfo.observe(this, {
            val type = it.type
            Log.d(TAG, "logout: type : $type")
            if(type == "google") {
                //google Logout
                FirebaseAuth.getInstance().signOut()
            } else if(type == "kakao") {
                //kakao Logout
                val disposables = CompositeDisposable()

                UserApiClient.rx.logout()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제 됨")
                    }, { error ->
                        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제 됨", error)
                    }).addTo(disposables)
            }
        })

        ApplicationClass.sharedPreferencesUtil.deleteUser()
        ApplicationClass.sharedPreferencesUtil.deleteUserCookie()
        ApplicationClass.sharedPreferencesUtil.deleteAutoLogin()
        //화면이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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

    /**
     * #S06P12D109-151
     * SNS Naver Logout delete Token
     */

    fun naverTokenDelete() {
        DeleteTokenTask(this, mOAuthLoginInstance).execute()
    }

    inner class DeleteTokenTask(private val mContext: Context, private val mOAuthLoginModule: OAuthLogin) : AsyncTask<Void?, Void?, Boolean>() {
        override fun onPostExecute(isSuccessDeleteToken: Boolean) {}
        override fun doInBackground(vararg params: Void?): Boolean {
            val isSuccessDeleteToken = mOAuthLoginModule.logoutAndDeleteToken(mContext)
            if (!isSuccessDeleteToken) {
                Log.d(TAG, "errorCode:" + mOAuthLoginModule.getLastErrorCode(mContext))
                Log.d(TAG, "errorDesc:" + mOAuthLoginModule.getLastErrorDesc(mContext))
            }
            return isSuccessDeleteToken
        }
    }


    /**
     * Fcm Notification 수신을 위한 채널 추가
     */
    @RequiresApi(Build.VERSION_CODES.O)
    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT // or IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager
                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



    companion object {
        const val channel_id = "ssafy_channel"
        fun uploadToken(token:String, userId: String) {
            val storeService = ApplicationClass.retrofit.create(FirebaseTokenService::class.java)
            storeService.uploadToken(token, userId).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        val res = response.body()
                        if(res == true) {
                            Log.d(TAG, "onResponse: $res")
                        } else {
                            Log.d(TAG, "onResponse Fail: $res")

                        }
                    } else {
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }
    }
}