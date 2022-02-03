package com.ssafy.groute.src.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.service.UserService

private const val TAG = "MyViewModel_groute"
class MyViewModel : ViewModel() {
    var user = ApplicationClass.sharedPreferencesUtil.getUser()
    private val _nickname = MutableLiveData<String>()
    private val _img = MutableLiveData<String?>()

    val nick: LiveData<String>
        get() = _nickname

    val img: LiveData<String?>
        get() = _img

    fun setNickName(nickname: String) {
        Log.d(TAG, "setNickName: $nickname")
        _nickname.value = nickname
    }
    fun setUserImg(img: String?) {
        _img.value = img
    }


    fun initData(owner: LifecycleOwner) {
        UserService().getUserInfo(user.id).observe(owner, Observer {
            setNickName(it.nickname)
            if(it.type == "sns") {
                setUserImg(it.img)
            } else {
                setUserImg("${ApplicationClass.IMGS_URL_USER}${it.img}")
            }
        })
    }

}

