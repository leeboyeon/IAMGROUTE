package com.ssafy.groute.src.login

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
import com.ssafy.groute.src.main.MainActivity


class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * #S06P12D109-11
         * Auto Login -> 로그인 했던 상태이면 login 화면 pass
         */
        //로그인 된 상태인지 확인
        val user = sharedPreferencesUtil.getUser()

//        //로그인 상태 확인. id가 있다면 로그인 된 상태 -> 가장 첫 화면은 홈 화면의 Fragment로 지정

        if (user.id != ""){
            openFragment(1)
        } else {

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
}