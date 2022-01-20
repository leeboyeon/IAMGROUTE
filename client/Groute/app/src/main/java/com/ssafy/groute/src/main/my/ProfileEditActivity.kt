package com.ssafy.groute.src.main.my

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.ActivityProfileEditBinding
import com.ssafy.groute.src.response.UserInfoResponse

private const val TAG = "ProfileEditActivity_groute"
class ProfileEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileEditBinding
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditFinish.setOnClickListener {
            finish()
        }

        val userData = intent.getSerializableExtra("userData") as UserInfoResponse
        Log.d(TAG, "onCreate: $userData")
        initData(userData)


    }

    fun initData(user: UserInfoResponse) {
        Glide.with(this)
            .load("${ApplicationClass.IMGS_URL}${user.img}")
            .circleCrop()
            .into(binding.profileEditImg)
        binding.profileEditIdEt.setText(user.id)
        binding.profileEditPasswordEt.setText(user.password)
        binding.profileEditNicknameEt.setText(user.nickname)
        binding.profileEditPhoneEt.setText(user.phone)
        if(user.email == "") {
            binding.profileEditEmailEt.hint = "이메일을 입력해주세요."
        } else {
            binding.profileEditEmailEt.setText(user.email)
        }
    }
}