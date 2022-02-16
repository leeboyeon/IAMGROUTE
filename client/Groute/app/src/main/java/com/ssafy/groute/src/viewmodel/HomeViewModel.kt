package com.ssafy.groute.src.viewmodel

import android.net.Network
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.nhn.android.idp.common.connection.CommonConnection.cancel
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import io.reactivex.internal.util.HalfSerializer.onError
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "HomeViewModel_Groute"
class HomeViewModel: ViewModel() {

    private val _areaResponse = MutableLiveData<MutableList<Area>>()

    val areaList : LiveData<MutableList<Area>>
        get() = _areaResponse

    fun setAreaList(area: MutableList<Area>) = viewModelScope.launch {
        _areaResponse.value = area  // main thread 에서 갱신
        _areaResponse.postValue(area)   // 백그라운드 갱신
    }

    suspend fun getAreaLists() {
        val response = AreaService().getAreaList()
        viewModelScope.launch {
                var res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        setAreaList(res)
                    }

                } else {
                    Log.d(TAG, "Error : ${response.message()} ")
                }
        }
    }

    private val _selectedProperty = MutableLiveData<Int>()

    val selectedProperty: LiveData<Int>
        get() = _selectedProperty

    fun displayAreaDetails(areaId: Int) {
        _selectedProperty.value = areaId
    }

}
