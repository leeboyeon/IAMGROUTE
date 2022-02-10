package com.ssafy.groute.src.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserService
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel_Groute"
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



    // set user
    private val _loginUserInfo = MutableLiveData<UserInfoResponse>()

    val loginUserInfo : LiveData<UserInfoResponse>
        get() = _loginUserInfo

    fun setLoginUserInfo(user : UserInfoResponse) = viewModelScope.launch {
        _loginUserInfo.value = user
    }

    private val _userInfo = MutableLiveData<UserInfoResponse>()

    val userInformation : LiveData<UserInfoResponse>
        get() = _userInfo

    fun setUserInfo(userInfo: UserInfoResponse) = viewModelScope.launch {
        _userInfo.value = userInfo
    }

    suspend fun getUserInformation(userId: String, loginChk : Boolean) {
        val response = UserService().getUser(userId)
        viewModelScope.launch {
            val res = response.body()
            if(response.code() == 200) {
                if(res != null) {
                    if(loginChk == true) {    // 로그인 user이면
                        setLoginUserInfo(res)
                    } else {
                        setUserInfo(res)
                    }
                    Log.d(TAG, "getUserInfoSuccess: ${response.message()}")
                } else {
                    Log.d(TAG, "getUserInfoError: ${response.message()}")
                }
            }
        }
    }

}