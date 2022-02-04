package com.ssafy.groute.src.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.service.UserPlanService
import kotlinx.coroutines.launch

private const val TAG = "PlanViewModel_Groute"
class PlanViewModel : ViewModel(){
    private val _planBestResponse = MutableLiveData<MutableList<UserPlan>>()
    private val _planMyResponse = MutableLiveData<MutableList<UserPlan>>()
    
    val planBestList : LiveData<MutableList<UserPlan>>
        get() = _planBestResponse
    val planMyList : LiveData<MutableList<UserPlan>>
        get() = _planMyResponse
    
    fun setPlanBestList(plan: MutableList<UserPlan>) = viewModelScope.launch {
        _planBestResponse.value = plan
    }
    fun setPlanMyList(plan: MutableList<UserPlan>) = viewModelScope.launch { 
        _planMyResponse.value = plan
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
}