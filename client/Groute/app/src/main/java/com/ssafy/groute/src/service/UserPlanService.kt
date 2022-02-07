package com.ssafy.groute.src.service

import android.util.Log
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.dto.PlanReview
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserPlanService_groute"
class UserPlanService {
    suspend fun getUserPlanById(id:Int) : Response<Map<String,Any>>{
        return RetrofitUtil.userPlanService.getUserPlanById(id)
    }
    suspend fun getBestUserPlan() = RetrofitUtil.userPlanService.getBestUserPlan()

    fun insertUserPlan(planId:Int, userPlan: UserPlan, userIds:ArrayList<String>, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userPlanService.insertUserPlan(planId,userPlan,userIds).enqueue(object :Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res==true){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    suspend fun getMyUserPlan(userId:String) : Response<MutableList<UserPlan>>{
        return RetrofitUtil.userPlanService.getMyUserPlan(userId)
    }

    suspend fun getMyPlanNotEnd(userId:String) : Response<MutableList<UserPlan>>{
        return RetrofitUtil.userPlanService.getMyPlanNotEnd(userId)
    }

    suspend fun getMyPlanEnd(userId:String) : Response<MutableList<UserPlan>>{
        return RetrofitUtil.userPlanService.getMyPlanEnd(userId)
    }

    suspend fun getPlanReviewbyId(planId:Int) : Response<MutableList<PlanReview>>{
        return RetrofitUtil.userPlanService.getPlanReviewListbyId(planId)
    }

    fun insertPlanReview(review:PlanReview, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.userPlanService.insertPlanReview(review).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res == true) {
                        callback.onSuccess(response.code(), res)
                        Log.d(TAG, "onResponse: insert Success!")
                    } else {
                        Log.d(TAG, "onResponse: insert fail")
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }

    fun insertPlaceToUserPlan(routeDetail: RouteDetail, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userPlanService.insertPlaceToUserPlan(routeDetail).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res == true) {
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

    fun updatePlanReview(review: PlanReview, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userPlanService.updatePlanReview(review).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res == true) {
                        callback.onSuccess(response.code(), res)
                        Log.d(TAG, "onResponse: update Success!")
                    } else {
                        Log.d(TAG, "onResponse: update fail")
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }

    fun deletePlanReview(id: Int, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userPlanService.deletePlanReview(id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res == true) {
                        callback.onSuccess(response.code(), res)
                        Log.d(TAG, "onResponse: delete Success!")
                    } else {
                        Log.d(TAG, "onResponse: delete fail")
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }

    suspend fun getReviewById(id: Int): Response<PlanReview> {
        return RetrofitUtil.userPlanService.getPlanReviewbyId(id)
    }
}