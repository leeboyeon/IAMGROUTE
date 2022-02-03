package com.ssafy.groute.src.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService

open class MainViewModel : ViewModel() {
    val userInfo = UserService().getUserInfo(ApplicationClass.sharedPreferencesUtil.getUser().id)

    private var users = MutableLiveData<LiveData<UserInfoResponse>>().apply {
        value = userInfo
    }

    fun getUser() :LiveData<UserInfoResponse>{
        return userInfo
    }

}