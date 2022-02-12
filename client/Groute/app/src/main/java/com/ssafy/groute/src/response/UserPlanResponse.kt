package com.ssafy.groute.src.response

import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class UserPlanResponse (
    @SerializedName("userPlan") var userPlan: UserPlan,
    @SerializedName("routeList") var routeList: MutableList<Route>,
    @SerializedName("routeDetailList") var routeDetailList: List<RouteDetail>,
) : Serializable