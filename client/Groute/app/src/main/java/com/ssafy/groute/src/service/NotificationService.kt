package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.dto.Notification
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationService {

    /**
     * notification insert
     * @param notification
     * @param callback
     */
    fun insertNotification(notification: Notification, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.notificationService.insertNotification(notification).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
                    if(res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    /**
     * notification Id로 notification 조회
     * @param id
     * @param callback
     */
    fun getNotificationById(id: Int, callback: RetrofitCallback<Notification>) {
        RetrofitUtil.notificationService.getNotificationById(id).enqueue(object : Callback<Notification> {
            override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Notification>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    /**
     * userId에 해당하는 사용자의 Notification list 조회
     * @param userId
     * @return Response
     */
    suspend fun getNotificationByUserId(userId: String) : Response<MutableList<Notification>> {
        return RetrofitUtil.notificationService.getNotificationByUserId(userId)
    }

    /**
     * notification Id에 해당하는 Notification을 삭제한다.
     * @param id
     * @param callback
     */
    fun deleteNotification(id: Int, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.notificationService.deleteNotification(id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }


    /**
     * notification Id에 해당하는 Notification을 수정한다.
     * @param notification
     * @param callback
     */
    fun updateNotification(notification: Notification, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.notificationService.updateNotification(notification).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

}