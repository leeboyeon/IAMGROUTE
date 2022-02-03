package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPlanService {

    suspend fun getBestUserPlan() = RetrofitUtil.userPlanService.getBestUserPlan()
}