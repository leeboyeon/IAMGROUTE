package com.ssafy.groute.src.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService

open class MainViewModel : ViewModel() {
    val userInfo = UserService().getUserInfo(ApplicationClass.sharedPreferencesUtil.getUser().id)
    private val _bannerItemList: MutableLiveData<List<Int>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()

    val bannerItemList: LiveData<List<Int>>
        get() = _bannerItemList

    val currentPosition: LiveData<Int>
        get() = _currentPosition

    init{
        _currentPosition.value = 0
    }

    private var users = MutableLiveData<LiveData<UserInfoResponse>>().apply {
        value = userInfo
    }

    fun getUser() :LiveData<UserInfoResponse>{
        return userInfo
    }

    fun setBannerItems(list: List<Int>) {
        _bannerItemList.value = list
    }

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }


    fun getcurrentPosition() = currentPosition.value

}