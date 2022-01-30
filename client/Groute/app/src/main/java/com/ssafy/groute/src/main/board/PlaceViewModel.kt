//package com.ssafy.groute.src.main.board
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.ssafy.groute.src.dto.Place
//import com.ssafy.groute.src.service.PlaceService
//import kotlinx.coroutines.launch
//
//class PlaceViewModel :ViewModel() {
//    var _placeList = PlaceService().getAllPlaces()
//    val placeList:MutableLiveData<List<Place>>
//        get() = _placeList
//
//    fun setPlaceList(placeList:List<Place> ) = viewModelScope.launch {
//        _placeList.value = placeList
//        _placeList.postValue(placeList)
//    }
//
//    fun getPlace(id:Int){
//        PlaceService().getPlace(id, PlaceCallback())
//    }
//    inner class PlaceCallback(){
//
//    }
//}