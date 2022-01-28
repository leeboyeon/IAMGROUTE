package com.ssafy.groute.src.main.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.service.PlaceService
import kotlinx.coroutines.launch

class PlaceViewModel :ViewModel() {
    var _placeList = PlaceService().getAllPlaces()
    val placeList:MutableLiveData<List<Places>>
        get() = _placeList

    fun setPlaceList(placeList:List<Places> ) = viewModelScope.launch {
        _placeList.value = placeList
        _placeList.postValue(placeList)
    }

    fun getPlace(id:Int){
        PlaceService().getPlace(id, PlaceCallback())
    }
    inner class PlaceCallback(){
        
    }
}