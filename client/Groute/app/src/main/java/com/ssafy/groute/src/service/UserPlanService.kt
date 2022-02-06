package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.dto.PlanReview
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
}