package com.ssafy.groute.src.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.service.PlaceService
import kotlinx.coroutines.launch

private const val TAG = "PlaceViewModel_Groute"
class PlaceViewModel : ViewModel() {

    private val _placeListResponse = MutableLiveData<MutableList<Place>>()

    val placeList : LiveData<MutableList<Place>>
        get() = _placeListResponse

    fun setPlaceList(place: MutableList<Place>) = viewModelScope.launch {
        _placeListResponse.value = place  // main thread 에서 갱신
//        _placeListResponse.postValue(place)   // 백그라운드 갱신
    }

    suspend fun getPlaceList() {
        val response = PlaceService().getPlaceList()
        viewModelScope.launch {
//            withContext(Dispatchers.Main) {
            var res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    setPlaceList(res)
                    Log.d(TAG, "onResponse: $res")
                }

            } else {
                Log.d(TAG, "Error : ${response.message()} ")
            }
//            }
        }
    }
}