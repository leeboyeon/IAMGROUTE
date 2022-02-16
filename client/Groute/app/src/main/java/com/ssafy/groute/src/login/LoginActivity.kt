package com.ssafy.groute.src.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.util.Utility
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.groute.config.BaseActivity
import com.ssafy.groute.databinding.ActivityLoginBinding
import com.ssafy.groute.databinding.ActivityMainBinding
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import okhttp3.Cookie

private const val TAG = "LoginActivity_Groute"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * #S06P12D109-11
         * Auto Login -> 로그인 했던 상태이면 login 화면 pass
         */
        var userId = ""
        if(sharedPreferencesUtil.getAutoLogin() != null) {
            userId = sharedPreferencesUtil.getAutoLogin()!!
        }
        //로그인 상태 확인. id가 있다면 로그인 된 상태 -> 가장 첫 화면은 홈 화면의 Fragment로 지정
        if (userId != null || userId != ""){
            Log.d(TAG, "onCreate: ")
            UserService().isUsedId(userId, IsUsedIdCallback())
        } else {
            Log.d(TAG, "onCreate: 자동로그인된거")
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_login_layout, LoginFragment())
                .commit()
        }


        // kakao 플랫폼 키 해시 등록
//        var keyHash = Utility.getKeyHash(this)
//        Log.d("kakaoKeyHash", "onCreate: $keyHash")
    }

    fun openFragment(int: Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(int){
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }
            2 -> transaction.replace(R.id.frame_login_layout, SignFragment())
                .addToBackStack(null)

            3 -> transaction.replace(R.id.frame_login_layout, LoginFragment())
                .addToBackStack(null)

            4-> transaction.replace(R.id.frame_login_layout, UserInfoFindFragment())
                .addToBackStack(null)
        }
        transaction.commit()
    }

    // 자동 로그인 id가 db에 있는지 체크
    inner class IsUsedIdCallback() : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            Log.d(TAG, "onSuccess IsUsedId: $responseData")  // 0 : 중복 X, 사용가능 <-> 1 : 중복되는 ID, 사용불가능
            if(responseData){   // 중복되는 id가 있으면 이미 가입된 회원 -> 로그인 수행
                openFragment(1)
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_login_layout, LoginFragment())
                    .commit()
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }
}