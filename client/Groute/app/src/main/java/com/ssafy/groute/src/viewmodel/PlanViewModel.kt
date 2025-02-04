package com.ssafy.groute.src.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParser
import com.ssafy.groute.src.dto.*
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.ThemeService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.service.*
import com.ssafy.groute.util.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "PlanViewModel_Groute"

class PlanViewModel : ViewModel() {
    private val _planBestResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planMyResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planResponse = MutableLiveData<UserPlan>()
    private val _routeResponse = MutableLiveData<MutableList<Route>>()
    private val _routeDetailResponse = MutableLiveData<MutableList<RouteDetail>>()
    private val _planNotEndResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planEndResponse = MutableLiveData<MutableList<UserPlan>>()
    private val placeShopResponse = mutableListOf<Place>()
    private val viaResponse = arrayListOf<Place>()
    private val _planReviewListResponse = MutableLiveData<MutableList<PlanReview>>()
    private val _reviewResponse = MutableLiveData<PlanReview>()
    private val _themeResponse = MutableLiveData<MutableList<Theme>>()
    private val _userPlanListResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _userPlanListByDayResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _shareUserListResponse = MutableLiveData<MutableList<User>>()
    private val _planLikeListResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _userPlanResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _currentUserPlanResponse = MutableLiveData<UserPlan>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _accountPricceListResponse = MutableLiveData<MutableList<Int>>()
    private val _accountListResponse = MutableLiveData<MutableList<AccountOut>>()
    private val  _accountCategoryListResponse = MutableLiveData<MutableList<AccountCategory>>()
    private val _sharedTravelListResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _bestPriorityResponse = MutableLiveData<MutableList<RouteDetail>>()
    private val _accountAllListResponse = MutableLiveData<MutableList<Account>>()
    private val _categoryByaccountResponse = MutableLiveData<Int>()
    private val _bestRouteResponse = MutableLiveData<MutableList<Route>>()

    val planBestList: LiveData<MutableList<UserPlan>>
        get() = _planBestResponse
    val planMyList: LiveData<MutableList<UserPlan>>
        get() = _planMyResponse
    val planList: LiveData<UserPlan>
        get() = _planResponse
    val routeList: LiveData<MutableList<Route>>
        get() = _routeResponse
    val routeDetailList: LiveData<MutableList<RouteDetail>>
        get() = _routeDetailResponse
    val planNotEndList: LiveData<MutableList<UserPlan>>
        get() = _planNotEndResponse
    val planEndList: LiveData<MutableList<UserPlan>>
        get() = _planEndResponse
    val planReviewList: LiveData<MutableList<PlanReview>>
        get() = _planReviewListResponse
    val review: LiveData<PlanReview>
        get() = _reviewResponse
    val theme: LiveData<MutableList<Theme>>
        get() = _themeResponse
    val userPlanList: LiveData<MutableList<UserPlan>>
        get() = _userPlanListResponse
    val userPlanByDayList: LiveData<MutableList<UserPlan>>
        get() = _userPlanListByDayResponse
    val shareUserList: LiveData<MutableList<User>>
        get() = _shareUserListResponse
    val planLikeList: LiveData<MutableList<UserPlan>>
        get() = _planLikeListResponse
    val userPlan: LiveData<MutableList<UserPlan>>
        get() = _userPlanResponse
    val currentUserPlan:LiveData<UserPlan>
        get() = _currentUserPlanResponse
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val accountList : LiveData<MutableList<AccountOut>>
        get() = _accountListResponse
    val accountCategoryList :LiveData<MutableList<AccountCategory>>
        get() =  _accountCategoryListResponse
    val sharedTravelList: LiveData<MutableList<UserPlan>>
        get() = _sharedTravelListResponse
    val accountPriceList : LiveData<MutableList<Int>>
        get() = _accountPricceListResponse
    val bestPriorityList : LiveData<MutableList<RouteDetail>>
        get() = _bestPriorityResponse
    val accountAllList :LiveData<MutableList<Account>>
        get() = _accountAllListResponse
    val categoryByaccountList : LiveData<Int>
        get() = _categoryByaccountResponse
    val bestRouteList : LiveData<MutableList<Route>>
        get() = _bestRouteResponse

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
    fun setBestRouteList(route:MutableList<Route>) = viewModelScope.launch {
        _bestRouteResponse.value = route
    }
    fun setRouteDetailList(routeDetail: MutableList<RouteDetail>) = viewModelScope.launch {
        _routeDetailResponse.value = routeDetail
    }

    fun setPlanNotEndList(plan: MutableList<UserPlan>) = viewModelScope.launch {
        _planNotEndResponse.value = plan
    }

    fun setPlanEndList(plan: MutableList<UserPlan>) = viewModelScope.launch {
        _planEndResponse.value = plan
    }

    fun setShareUserList(users: MutableList<User>) = viewModelScope.launch {
        _shareUserListResponse.value = users
    }
    fun setAccountAllList(account: MutableList<Account>) = viewModelScope.launch {
        _accountAllListResponse.value = account
    }

    val livePlaceshopList = MutableLiveData<MutableList<Place>>().apply {
        value = placeShopResponse
    }
    val liveViaList = MutableLiveData<ArrayList<Place>>().apply {
        value = viaResponse
    }
    fun insertPlaceShopList(place: Place) {
        placeShopResponse.add(place)
        livePlaceshopList.value = placeShopResponse
    }
    fun insertViaList(place:Place){
        viaResponse.add(place)
        liveViaList.value = viaResponse
    }
    fun removeAllViaList(){
        viaResponse.clear()
        liveViaList.value = viaResponse
    }
    fun removePlaceShopList(placeId: Int) {
        for (i in 0..placeShopResponse.size - 1) {
            if (placeShopResponse.get(i).id == placeId) {
                placeShopResponse.removeAt(i)
                break
            }
        }
        livePlaceshopList.value = placeShopResponse
    }

    fun setPlanReviewList(planReview: MutableList<PlanReview>) = viewModelScope.launch {
        _planReviewListResponse.value = planReview
        Log.d(TAG, "setPlanReviewList: $planReview")
    }

    fun setReivew(review: PlanReview) = viewModelScope.launch {
        _reviewResponse.value = review
    }

    fun setTheme(themeList: MutableList<Theme>) = viewModelScope.launch {
        _themeResponse.value = themeList
    }

    fun setUserPlanList(userPlanList: MutableList<UserPlan>) = viewModelScope.launch {
        _userPlanListResponse.value = userPlanList
    }

    fun setUserPlanByDayList(userPlanList: MutableList<UserPlan>) = viewModelScope.launch {
        _userPlanListByDayResponse.value = userPlanList
    }

    fun setPlanLikeList(plan: MutableList<UserPlan>) = viewModelScope.launch {
        _planLikeListResponse.value = plan
    }

    fun setUserPlan(userPlan: UserPlan) = viewModelScope.launch {
        var list = mutableListOf<UserPlan>()
        _currentUserPlanResponse.value = userPlan
        list.add(userPlan)
        _userPlanResponse.value = list
    }
    fun setUserNotPlanList() = viewModelScope.launch {
        _userPlanResponse.value = planNotEndList.value
    }

    fun setIsLoading(loading: Boolean) = viewModelScope.launch {
        _isLoading.value = loading
    }
    fun setAccountList(account: MutableList<AccountOut>) = viewModelScope.launch {
        _accountListResponse.value = account
    }
    fun setAccountCategory(category: MutableList<AccountCategory>) = viewModelScope.launch {
        _accountCategoryListResponse.value = category
    }
    fun setSharedTravelList(sharedTravelList: MutableList<UserPlan>) = viewModelScope.launch {
        _sharedTravelListResponse.value = sharedTravelList
    }

    fun setAccountPrice(price:MutableList<Int>) = viewModelScope.launch {
        _accountPricceListResponse.value = price
    }
    fun setBestPriority(routeDetail:MutableList<RouteDetail>) = viewModelScope.launch {
        _bestPriorityResponse.value = routeDetail
    }
    fun setCategoryByAccount(size:Int) = viewModelScope.launch {
        _categoryByaccountResponse.value = size
    }

    suspend fun getPlanBestList() {
        val response = UserPlanService().getBestUserPlan()
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlanBestList(res)
                    Log.d(TAG, "getPlanBestList: ")
                }
            } else {
                Log.d(TAG, "getPlanBestList: ")
            }
        }
    }

    suspend fun getPlanMyList(userId: String) {
        val response = UserPlanService().getMyUserPlan(userId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlanMyList(res)
                    Log.d(TAG, "getPlanMyList: ")
                }
            } else {
                Log.d(TAG, "getPlanMyList: ")
            }
        }
    }
    suspend fun getPlanById(id:Int, flag: Int){
        val response = UserPlanService().getUserPlanById(id)
        viewModelScope.launch { 
            val res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    if(flag == 1) { // RouteDetail에서 사용자의 현재 짜고있는 일정 보여줄때
                        setUserPlan(res.userPlan)
                    } else if(flag == 2){ // 기존에 쓰던거
                        setPlanList(res.userPlan)
                        setRouteList(res.routeList)
                        getThemeById(res.userPlan.themeIdList)
//                        setRouteDetailList(res.routeDetailList as MutableList<RouteDetail>)
                    } else if(flag == 3) {
                        setPlanList(res.userPlan)
                    }
                }else{
                    Log.d(TAG, "getPlanById: res is null")
                }
            }else{
                Log.d(TAG, "getPlanById: ${response.code()}")
            }
        }
    }
//    suspend fun getPlanById(id: Int, flag: Int) {
//        val response = UserPlanService().getUserPlanById(id)
//        viewModelScope.launch {
//            var res = response.body()
//            if (response.code() == 200) {
//                if (res != null) {
////                    Log.d(TAG, "getPlanById: $res")
//                    val userPlantmp = JSONObject(res).getJSONObject("userPlan")
////                    Log.d(TAG, "getPlanById: ${userPlantmp}")
//                    val themeList = userPlantmp.getJSONArray("themeIdList")
//                    var themeIdList = mutableListOf<Int>()
//                    var k = 0
//                    while (k < themeList.length()) {
//                        themeIdList.add(themeList.getInt(k))
//                        k++
//                    }
//                    val userPlan = UserPlan(
//                        id = userPlantmp.getInt("id"),
//                        title = userPlantmp.getString("title"),
//                        userId = userPlantmp.getString("userId"),
//                        description = userPlantmp.getString("description"),
//                        startDate = userPlantmp.getString("startDate"),
//                        endDate = userPlantmp.getString("endDate"),
//                        totalDate = userPlantmp.getInt("totalDate"),
//                        isPublic = userPlantmp.getString("isPublic"),
//                        rate = userPlantmp.getDouble("rate"),
//                        heartCnt = userPlantmp.getInt("heartCnt"),
//                        areaId = userPlantmp.getInt("areaId"),
//                        themeIdList = themeIdList,
//                        reviewCnt = userPlantmp.getInt("reviewCnt")
//                    )
//
//                    val routetmp = JSONObject(res).getJSONArray("routeList")
//                    var i = 0
//
//                    var routeList = mutableListOf<Route>()
//                    while (i < routetmp.length()) {
//                        var routeDetaillist = arrayListOf<RouteDetail>()
//                        val jsonObject = routetmp.getJSONObject(i)
//                        var rid = jsonObject.getInt("id")
//                        var rname = jsonObject.getString("name")
//                        var day = jsonObject.getInt("day")
//                        var rmemo = jsonObject.getString("memo")
//                        var isCustom = jsonObject.getString("isCustom")
//                        var detailtmp = jsonObject.getJSONArray("routeDetailList")
//                        var j = 0
//                        while (j < detailtmp.length()) {
//                            val detailObject = detailtmp.getJSONObject(j)
//                            var routeId = detailObject.getInt("routeId")
//                            var did = detailObject.getInt("id")
//                            var placeId = detailObject.getInt("placeId")
//                            var priority = detailObject.getInt("priority")
//                            var dmemo = detailObject.getString("memo")
//                            var placeObject = detailObject.getJSONObject("place")
//
//                            var pid = placeObject.getInt("id")
//                            var name = placeObject.getString("name")
//                            var type = placeObject.getString("type")
//                            var lat = placeObject.getString("lat")
//                            var lng = placeObject.getString("lng")
//                            var zipCode = placeObject.getString("zipCode")
//                            var contact = placeObject.getString("contact")
//                            var address = placeObject.getString("address")
//                            var description = placeObject.getString("description")
//                            var themeId = placeObject.getInt("themeId")
//                            var areaId = placeObject.getInt("areaId")
//                            var img = placeObject.getString("img")
//                            var userId = placeObject.getString("userId")
//                            var rate = placeObject.getDouble("rate")
//                            var heartCnt = placeObject.getInt("heartCnt")
//
//                            var place = Place(
//                                address = address,
//                                areaId = areaId,
//                                contact = contact,
//                                description = description,
//                                heartCnt = heartCnt,
//                                id = pid,
//                                img = img,
//                                lat = lat,
//                                lng = lng,
//                                name = name,
//                                rate = rate.toFloat(),
//                                themeId = themeId,
//                                type = type,
//                                userId = userId,
//                                zipCode = zipCode
//                            )
//                            var routeDetail = RouteDetail(
//                                id = did,
//                                memo = dmemo,
//                                place = place,
//                                placeId = placeId,
//                                priority = priority,
//                                routeId = routeId
//                            )
//                            routeDetaillist.add(routeDetail)
//                            setRouteDetailList(routeDetaillist)
//                            j++
//                        }
//                        var route = Route(
//                            day = day,
//                            id = rid,
//                            isCustom = isCustom,
//                            memo = rmemo,
//                            name = rname,
//                            routeDetailList = routeDetaillist
//                        )
//                        routeList.add(route)
//                        i++
//                    }
//                    if(flag == 1) { // RouteDetail에서 사용자의 현재 짜고있는 일정 보여줄때
//                        setUserPlan(userPlan)
//                    } else if(flag == 2){ // 기존에 쓰던거
//                        setPlanList(userPlan)
//                        setRouteList(routeList)
//                        getThemeById(userPlan.themeIdList)
////                        Log.d(TAG, "getPlanById_USERPlan: ${userPlan}")
////                        Log.d(TAG, "getPlanById: ${routeList}")
//                    } else if(flag == 3) {
//                        setPlanList(userPlan)
//                    }
//
//                }
//            } else {
//                Log.d(TAG, "getPlanById: ")
//            }
//
//        }
//    }

    fun getRouteDetailbyDay(day: Int) {
        viewModelScope.launch {
            for (i in 0..routeList.value!!.size - 1) {
                if (routeList.value!!.get(i).day == day) {
                    setRouteDetailList(routeList.value!!.get(i).routeDetailList.toMutableList())
                }
            }
        }
    }

    suspend fun getMyNotendPlan(userId: String) {
        val response = UserPlanService().getMyPlanNotEnd(userId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlanNotEndList(res)
                    Log.d(TAG, "getMyNotendPlan: ")
                }
            } else {
                Log.d(TAG, "getMyNotendPlan: ")
            }
        }
    }

    suspend fun getMyEndPlan(userId: String) {
        val response = UserPlanService().getMyPlanEnd(userId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlanEndList(res)
                    Log.d(TAG, "getMyEndPlan: ")
                }
            } else {
                Log.d(TAG, "getMyEndPlan: ")
            }
        }
    }

    suspend fun getPlanReviewListbyId(planId: Int) {
        val response = UserPlanService().getPlanReviewbyId(planId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlanReviewList(res)
//                    Log.d(TAG, "getPlanReviewListbyId: $res")
                }

            } else {
                Log.d(TAG, "getPlanReviewListbyId: ")
            }
        }
    }

    suspend fun getPlanReviewById(id: Int) {
        val response = UserPlanService().getReviewById(id)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    Log.d(TAG, "UserPlanService: ")
                    setReivew(res)
                }
            } else {
                Log.d(TAG, "UserPlanService: ")
            }
        }
    }

    suspend fun getThemeById(idList: List<Int>) {
        setTheme(mutableListOf())
        var list = mutableListOf<Theme>()
        for (i in 0 until idList.size) {
            val response = ThemeService().getThemeById(idList.get(i))
            viewModelScope.launch {
                var res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        //Log.d(TAG, "getThemeById: $res")
                        list.add(res)
                    }
                } else {
                    Log.d(TAG, "getThemeById: ")
                }
            }

        }
        setTheme(list)

    }

    suspend fun getThemeList() {
        val response = ThemeService().getThemeList()
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setTheme(res)
                }

            } else {
                Log.d(TAG, "Error : ${response.message()} ")
            }
        }

    }

    suspend fun getUserPlanList() {
        val response = UserPlanService().getPlanList()
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setUserPlanList(res)
                    setUserPlanByDayList(res)
                }

            } else {
                Log.d(TAG, "Error : ${response.message()} ")
            }
        }
    }

    fun getRoutebyDay(totalDate: Int, selectedTheme: MutableList<Int>) {
        viewModelScope.launch {
            var list = mutableListOf<UserPlan>()
            if (totalDate == 0) {
                for (i in 0 until userPlanList.value!!.size) {
                    var themeList = userPlanList.value!!.get(i).themeIdList
                    var count = 0
                    for (j in 0 until selectedTheme.size) {
                        if (themeList.contains(selectedTheme.get(j))) {
                            count++
                        }
                    }
                    if (count == selectedTheme.size) {
                        list.add(userPlanList.value!!.get(i))
                    }
                }
                //list = userPlanList.value!!
            } else {
                for (i in 0 until userPlanList.value!!.size) {
                    if (userPlanList.value!!.get(i).totalDate == totalDate) {
                        //list.add(userPlanList.value!!.get(i))
                        var themeList = userPlanList.value!!.get(i).themeIdList
                        var count = 0
                        for (j in 0 until selectedTheme.size) {
                            if (themeList.contains(selectedTheme.get(j))) {
                                count++
                            }
                        }
                        if (count == selectedTheme.size) {
                            list.add(userPlanList.value!!.get(i))
                        }
                    }
                }
            }

            setUserPlanByDayList(list)

        }
    }

    suspend fun getShareUserbyPlanId(planId: Int) {
        val response = UserService().getShareUserbyPlanId(planId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setShareUserList(res)
                }
            } else {
                Log.d(TAG, "getShareUserbyPlanId: ")
            }
        }
    }

    suspend fun getPlanLikeList(userId: String) {
        val response = UserPlanService().getPlanLikeList(userId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlanLikeList(res)
                } else {
                    Log.d(TAG, "getPlanLikeList: ISNULL")
                }
            } else {
                Log.d(TAG, "getPlanLikeList: FAIL")
            }
        }
    }

    fun getPlanByPlace(planId: Int, flag: Int) {
        viewModelScope.launch {
            setIsLoading(true)
            setUserPlanList(mutableListOf())
            setUserPlanByDayList(mutableListOf())
            var placeIds = mutableListOf<Int>()
            getPlanById(planId, 2)
            for (i in 0 until routeList.value!!.size) {
                var detailList = routeList.value!!.get(i).routeDetailList
                for (j in 0 until detailList.size) {
                    placeIds.add(detailList.get(j).placeId)
                }
            }
            val response = UserPlanService().getPlanIncludePlace(flag, placeIds)
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setUserPlanList(res)
                    setUserPlanByDayList(res)
                    setIsLoading(false)
//                    Log.d(TAG, "getPlanByPlace: ${res}")
                } else {
                    Log.d(TAG, "getPlanByPlace: ISNULL")
                }
            } else {
                Log.d(TAG, "getPlanByPlace: FAIL")
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAccountList(planId:Int) {
        val response = AccountService().getListByPlanId(planId)
        viewModelScope.launch {
            var res = response.body()
            if (response.code() == 200) {
                var sDate = planList.value!!.startDate
                var totalDate = planList.value!!.totalDate

                var date = LocalDate.parse(sDate, DateTimeFormatter.ISO_DATE)
                var outlist = arrayListOf<AccountOut>()
                var accountlist = arrayListOf<Account>()

                if (res != null) {
                    setAccountAllList(res)
                    for (i in 0 until totalDate) {
                        for (j in 0..res.size - 1) {
                            if (res[j].day == i + 1) {
                                Log.d(TAG, "getAccountList: ${res[j].day} || ${i + 1}")
                                accountlist.add(res[j])
                            }
                        }
                        var accounts = AccountOut(date.plusDays(i.toLong()).toString(), accountlist)
                        accountlist = arrayListOf()
                        outlist.add(accounts)
                    }
                }

                Log.d(TAG, "getAccountList: ${outlist}")
                setAccountList(outlist)

            } else {
                Log.d(TAG, "getAccountList: ${response.code()}")
            }
        }
    }

    suspend fun getCategory(){
        val response = AccountService().getCategoryList()
        viewModelScope.launch {
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    setAccountCategory(res)
                }
            }
        }
    }

    fun getSharedPlanList() {
        viewModelScope.launch {
            var list = mutableListOf<UserPlan>()
            for(i in 0 until planMyList.value!!.size) {
                if(planMyList.value!!.get(i).isPublic == "T") {
                    list.add(planMyList.value!!.get(i))
                }
            }
            Log.d(TAG, "getSharedPlanList: $list")
            setSharedTravelList(list)
        }
    }
    suspend fun getCategoryChart(planId:Int){
        val response = AccountService().getCategoryChart(planId)
        viewModelScope.launch {
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    Log.d(TAG, "getCategoryChart: ${res}")
                    var list = arrayListOf<Int>()
                    for(i in 0..res.values.size){
                        Log.d(TAG, "getCategoryChart: ${res[i]}")
                        res[i]?.let { list.add(it) }
                    }
                    setAccountPrice(list)
                }
            }
        }
    }

    suspend fun getBestPriority(end:Int, start:Int, routeId:Int,day:Int){
        val response = UserPlanService().getBestPriority(end, routeId,start)
        viewModelScope.launch {
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    Log.d(TAG, "getBestPriority: ${res}")
                    setBestPriority(res)
                    routeList.value?.get(day)?.routeDetailList = res
                    routeList.value?.let { setRouteList(it) }
                }
            }else{
                Log.d(TAG, "getBestPriority: ${response.code()}")
            }
        }
    }
}