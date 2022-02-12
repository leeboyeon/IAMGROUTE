package com.ssafy.groute.src.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.src.dto.Notification
import com.ssafy.groute.src.service.NotificationService
import kotlinx.coroutines.launch

private const val TAG = "NotiViewModel_Groute"
class NotificationViewModel : ViewModel() {

    private val _notificationList = MutableLiveData<MutableList<Notification>>()

    val notificationList : LiveData<MutableList<Notification>>
        get() = _notificationList

    fun setNotificationList(list : MutableList<Notification>) = viewModelScope.launch {
        _notificationList.value = list
    }

    suspend fun getNotificationList(userId : String) {
        val response = NotificationService().getNotificationByUserId(userId)
        viewModelScope.launch {
            val res = response.body()
            if (response.code() == 200) {
                if (res != null)  {
                    setNotificationList(res)
                    Log.d(TAG, "getNotificationList: onSuccess")
                } else {
                    Log.d(TAG, "getNotificationList: onFailure")
                }
            } else {
                Log.d(TAG, "getNotificationList: onError")
            }
        }
    }


}