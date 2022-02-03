package com.ssafy.groute.src.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.launch

open class MainViewModel : ViewModel() {
    val userInfo = UserService().getUserInfo(ApplicationClass.sharedPreferencesUtil.getUser().id)

    private var users = MutableLiveData<LiveData<UserInfoResponse>>().apply {
        value = userInfo
    }

    fun getUser() :LiveData<UserInfoResponse>{
        return userInfo
    }

}