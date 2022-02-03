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
    private val _placeResponse = MutableLiveData<Place>()
    private val _placeBestResponse =MutableLiveData<MutableList<Place>>()
    private val _placeLikeListResponse = MutableLiveData<MutableList<Place>>()

    val placeList : LiveData<MutableList<Place>>
        get() = _placeListResponse

    val place : LiveData<Place>
        get() = _placeResponse
    
    val placeBestList : LiveData<MutableList<Place>>
        get() = _placeBestResponse

    val placeLikeList : LiveData<MutableList<Place>>
        get() = _placeLikeListResponse

    fun setPlaceList(place: MutableList<Place>) = viewModelScope.launch {
        _placeListResponse.value = place  // main thread 에서 갱신
//        _placeListResponse.postValue(place)   // 백그라운드 갱신
    }
    fun setPlace(place: Place) = viewModelScope.launch { 
        _placeResponse.value = place
    }
    fun setPlaceBestList(place: MutableList<Place>) = viewModelScope.launch { 
        _placeBestResponse.value = place
    }
    fun setPlaceLikeList(place:MutableList<Place>) = viewModelScope.launch {
        _placeLikeListResponse.value = place
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

    suspend fun getPlace(id:Int){
        val response = PlaceService().getPlaces(id)
        viewModelScope.launch { 
            var res = response.body()
            if(response.code()==200){
                if(res!= null){
                    setPlace(res)
                    Log.d(TAG, "getPlace: $res")
                }
            }else{
                Log.d(TAG, "getPlace: ${response.message()}")
            }
        }
    }
    
    suspend fun getPlaceBestList(){
        val response = PlaceService().getPlaceBestList()
        viewModelScope.launch {
            var res = response.body()
            if(response.code() == 200){
                if(res!= null){
                    setPlaceBestList(res)
                    Log.d(TAG, "getPlaceBestList: $res")
                }
            }else{
                Log.d(TAG, "getPlaceBestList: ")
            }
        }
    }

    suspend fun getPlaceLikeList(userId:String){
        val response = PlaceService().getPlaceLikeList(userId)
        viewModelScope.launch {
            var res = response.body()
            if(response.code() == 200){
                if(res!=null){
                    setPlaceLikeList(res)
                    Log.d(TAG, "getPlaceLikeList: ")
                }
            }else{
                Log.d(TAG, "getPlaceLikeList: ")
            }
        }
    }
}