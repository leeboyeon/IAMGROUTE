package com.ssafy.groute.src.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.groute.src.dto.SharedUser
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserService_groute"
class UserService {
    fun login(user: User, callback: RetrofitCallback<User>) {
        RetrofitUtil.userService.login(user.id, user.password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onError(t)
            }
        })
    }


    /**
     * 서버로부터 입력된 id가 사용 중인지 확인한다.
     * @param inputId
     * @param callback
     */
    fun isUsedId(inputId: String, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userService.isUsedId(inputId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
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
     * 사용자 아이디에 해당하는 사용자 정보를 불러온다.
     * @param userId
     */
    fun getUserInfo(userId: String): LiveData<UserInfoResponse>{
        val responseLiveData : MutableLiveData<UserInfoResponse> = MutableLiveData()
        val userInfoRequest: Call<UserInfoResponse> = RetrofitUtil.userService.getUserInfo(userId)
        Log.d(TAG, "getUserInfo: $userId")
        userInfoRequest.enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d(TAG, t.message ?: "사용자 정보 받아오는 중 통신오류")
            }
        })
        return responseLiveData
    }

    /**
     * #S06P12D109-24 회원가입
     * 사용자로 부터 입력받은 사용자 데이터를 서버로 전송한다.
     * @param user
     * @return callback
     */
    fun signUp(user: User, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userService.signUp(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
//                Log.d("UserService_싸싸피", "onResponse: $response.")
                if(response.code() == 200) {
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
     * 사용자의 정보를 수정한다.
     * @param userId
     * @param user
     */
    fun updateUserInfo(user: RequestBody, img: MultipartBody.Part?, callback: RetrofitCallback<Boolean>) {
//        Log.d(TAG, "사용자 정보 수정: $user 이미지: ${user}  ${img.body()}")
        RetrofitUtil.userService.updateUser(user, img).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                    Log.d(TAG, "사용자 정보 수정 onResponse: Error Code : ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "사용자 정보 수정 onFailure: $t")
                callback.onError(t)
            }
        })
    }


    /**
     * 사용자의 정보를 수정한다.
     * @param userId
     * @param user
     */
//    fun updateUserInfo(user: RequestBody, callback: RetrofitCallback<Boolean>) {
//        Log.d(TAG, "사용자 정보 수정: $user 이미지: ${user}")
//        RetrofitUtil.userService.updateUser(user).enqueue(object : Callback<Boolean> {
//            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
//                val res = response.body()
//                if(res!!) {
//                    callback.onSuccess(response.code(), true)
//                } else {
//                    callback.onFailure(response.code())
//                }
//            }
//            override fun onFailure(call: Call<Boolean>, t: Throwable) {
//                callback.onError(t)
//            }
//        })
//    }

    fun deleteUser(userId:String, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.userService.deleteUser(userId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
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
     * 사용자의 공유유저의 정보를 추가한다.
     * @param shareUser
     */
    fun insertSharedUser(shareUser:SharedUser, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userService.insertSharedUser(shareUser).enqueue(object: Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
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
     * 사용자의 공유유저의 정보를 수정한다.
     * @param shareUser
     */
    fun updateSharedUser(shareUser: SharedUser,callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userService.updateSharedUser(shareUser).enqueue(object: Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
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
     * 사용자의 공유유저의 정보를 삭제한다.
     * @param shareUser
     */
    fun deleteSharedUser(shareUser: SharedUser, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userService.deleteSharedUser(shareUser).enqueue(object: Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
                    if (res!=null) {
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
     * 사용자의 공유유저의 정보를 불러온다
     * @param shareUser
     */
    suspend fun getShareUserbyPlanId(planId:Int) : Response<MutableList<User>>{
        return RetrofitUtil.userService.getShareUserList(planId)
    }
}