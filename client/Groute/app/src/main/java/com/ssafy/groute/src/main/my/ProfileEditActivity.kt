package com.ssafy.groute.src.main.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.groute.R
import com.ssafy.groute.databinding.ActivityMainBinding
import com.ssafy.groute.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditFinish.setOnClickListener {
            finish()
        }


    }
}