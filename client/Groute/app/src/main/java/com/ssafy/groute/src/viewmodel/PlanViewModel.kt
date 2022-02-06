package com.ssafy.groute.src.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.service.UserPlanService
import kotlinx.coroutines.launch
import org.json.JSONObject

private const val TAG = "PlanViewModel_Groute"
class PlanViewModel : ViewModel(){
    private val _planBestResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planMyResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planResponse = MutableLiveData<UserPlan>()
    private val _routeResponse = MutableLiveData<MutableList<Route>>()
    private val _routeDetailResponse = MutableLiveData<MutableList<RouteDetail>>()
    private val _planNotEndResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planEndResponse = MutableLiveData<MutableList<UserPlan>>()

//    private val _routeResponse = MutableLiveData<MutableList<>>
//    private val _routeDetailResponse = MutableLiveData<MutableList<>>
    val planBestList : LiveData<MutableList<UserPlan>>
        get() = _planBestResponse
    val planMyList : LiveData<MutableList<UserPlan>>
        get() = _planMyResponse
    val planList : LiveData<UserPlan>
        get() = _planResponse
    val routeList : LiveData<MutableList<Route>>
        get() = _routeResponse
    val routeDetailList : LiveData<MutableList<RouteDetail>>
        get() = _routeDetailResponse
    val planNotEndList : LiveData<MutableList<UserPlan>>
        get() =_planNotEndResponse
    val planEndList : LiveData<MutableList<UserPlan>>
        get() =_planEndResponse

    fun setPlanBestList(plan: MutableList<UserPlan>) = viewModelScope.launch {
        _planBestResponse.value = plan
    }
    fun setPlanMyList(plan: MutableList<UserPlan>) = viewModelScope.launch { 
        _planMyResponse.value = plan
    }
    fun setPlanList(plan: UserPlan) = viewModelScope.launch {
        _planResponse.value = plan
    }
    fun setRouteList(route: MutableList<Route>) = viewModelScope.launch {
        _routeResponse.value = route
    }
    fun setRouteDetailList(routeDetail: MutableList<RouteDetail>) = viewModelScope.launch {
        _routeDetailResponse.value = routeDetail
    }
    fun setPlanNotEndList(plan:MutableList<UserPlan>) = viewModelScope.launch {
        _planNotEndResponse.value = plan
    }
    fun setPlanEndList(plan:MutableList<UserPlan>) = viewModelScope.launch {
        _planEndResponse.value = plan
    }

    suspend fun getPlanBestList(){
        val response = UserPlanService().getBestUserPlan()
        viewModelScope.launch { 
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    setPlanBestList(res)
                    Log.d(TAG, "getPlanBestList: ")
                }
            }else{
                Log.d(TAG, "getPlanBestList: ")
            }
        }
    }
    suspend fun getPlanMyList(userId:String){
        val response = UserPlanService().getMyUserPlan(userId)
        viewModelScope.launch { 
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    setPlanMyList(res)
                    Log.d(TAG, "getPlanMyList: ")
                }
            }else{
                Log.d(TAG, "getPlanMyList: ")
            }
        }
    }
    suspend fun getPlanById(id:Int){
        val response = UserPlanService().getUserPlanById(id)
        viewModelScope.launch { 
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    Log.d(TAG, "getPlanById: $res")
                    val userPlantmp = JSONObject(res).getJSONObject("userPlan")
                    Log.d(TAG, "getPlanById: ${userPlantmp}")
                    val userPlan = UserPlan(
                        id = userPlantmp.getInt("id"),
                        title= userPlantmp.getString("title"),
                        userId= userPlantmp.getString("userId"),
                        description = userPlantmp.getString("description"),
                        startDate = userPlantmp.getString("startDate"),
                        endDate = userPlantmp.getString("endDate"),
                        totalDate = userPlantmp.getInt("totalDate"),
                        isPublic = userPlantmp.getString("isPublic"),
                        rate = userPlantmp.getDouble("rate"),
                        heartCnt = userPlantmp.getInt("heartCnt"),
                        areaId = userPlantmp.getInt("areaId")
                    )

                    val routetmp = JSONObject(res).getJSONArray("routeList")
                    var i = 0

                    var routeList = mutableListOf<Route>()
                    while(i < routetmp.length()){
                        var routeDetaillist = arrayListOf<RouteDetail>()
                        val jsonObject = routetmp.getJSONObject(i)
                        var rid = jsonObject.getInt("id")
                        var rname = jsonObject.getString("name")
                        var day = jsonObject.getInt("day")
                        var rmemo = jsonObject.getString("memo")
                        var isCustom = jsonObject.getString("isCustom")
                        var detailtmp = jsonObject.getJSONArray("routeDetailList")
                        var j = 0
                        while(j < detailtmp.length()){
                            val detailObject = detailtmp.getJSONObject(j)
                            var routeId = detailObject.getInt("routeId")
                            var did = detailObject.getInt("id")
                            var placeId = detailObject.getInt("placeId")
                            var priority = detailObject.getInt("priority")
                            var dmemo = detailObject.getString("memo")
                            var placeObject = detailObject.getJSONObject("place")

                            var pid = placeObject.getInt("id")
                            var name =placeObject.getString("name")
                            var type = placeObject.getString("type")
                            var lat = placeObject.getString("lat")
                            var lng = placeObject.getString("lng")
                            var zipCode = placeObject.getString("zipCode")
                            var contact = placeObject.getString("contact")
                            var address = placeObject.getString("address")
                            var description = placeObject.getString("description")
                            var themeId = placeObject.getInt("themeId")
                            var areaId = placeObject.getInt("areaId")
                            var img =placeObject.getString("img")
                            var userId = placeObject.getString("userId")
                            var rate = placeObject.getDouble("rate")
                            var heartCnt = placeObject.getInt("heartCnt")

                            var place = Place(
                                address = address,
                                areaId = areaId,
                                contact = contact,
                                description = description,
                                heartCnt = heartCnt,
                                id = pid,
                                img = img,
                                lat = lat,
                                lng = lng,
                                name = name,
                                rate = rate.toFloat(),
                                themeId = themeId,
                                type = type,
                                userId = userId,
                                zipCode = zipCode
                            )
                            var routeDetail = RouteDetail(
                                id=did,
                                memo = dmemo,
                                place = place,
                                placeId = placeId,
                                priority = priority,
                                routeId = routeId
                            )
                            routeDetaillist.add(routeDetail)
                            j++
                        }
                        var route = Route(
                            day = day,
                            id = rid,
                            isCustom = isCustom,
                            memo = rmemo,
                            name = rname,
                            routeDetailList = routeDetaillist
                        )
                        routeList.add(route)
                        i++
                    }
                    setPlanList(userPlan)
                    setRouteList(routeList)


                    Log.d(TAG, "getPlanById_USERPlan: ${userPlan}")
                    Log.d(TAG, "getPlanById: ${routeList}")
                }
            }else{
                Log.d(TAG, "getPlanById: ")
            }
        }
    }
    fun getRouteDetailbyDay(day:Int){
        viewModelScope.launch {
            for(i in 0..routeList.value!!.size-1){
                if(routeList.value!!.get(i).day == day){
                    setRouteDetailList(routeList.value!!.get(i).routeDetailList.toMutableList())
                }
            }
        }
    }
    
    suspend fun getMyNotendPlan(userId:String){
        val response = UserPlanService().getMyPlanNotEnd(userId)
        viewModelScope.launch {
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    setPlanNotEndList(res)
                    Log.d(TAG, "getMyNotendPlan: ")
                }
            }else{
                Log.d(TAG, "getMyNotendPlan: ")
            }
        }
    }
    
    suspend fun getMyEndPlan(userId:String){
        val response = UserPlanService().getMyPlanEnd(userId)
        viewModelScope.launch { 
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    setPlanEndList(res)
                    Log.d(TAG, "getMyEndPlan: ")
                }
            }else{
                Log.d(TAG, "getMyEndPlan: ")
            }
        }
    }
}