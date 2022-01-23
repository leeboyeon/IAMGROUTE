package com.ssafy.groute.src.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.util.Utility
import com.ssafy.groute.R
import com.ssafy.groute.databinding.ActivityLoginBinding
import com.ssafy.groute.src.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        }
        transaction.commit()
    }
}