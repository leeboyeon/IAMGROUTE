package com.ssafy.groute.src.splash

import android.content.Intent
import android.os.Bundle
import com.ssafy.groute.config.BaseActivity
import com.ssafy.groute.databinding.ActivityLoadingBinding
import android.os.Handler
import com.ssafy.groute.src.login.LoginActivity

class LoadingActivity : BaseActivity<ActivityLoadingBinding>(ActivityLoadingBinding::inflate) {
    private val SPLASH_TIME:Long = 6000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        },SPLASH_TIME)
    }
}