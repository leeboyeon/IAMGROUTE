package com.ssafy.groute.src.main.home

import android.net.Network
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.nhn.android.idp.common.connection.CommonConnection.cancel
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.dto.BoardDetail
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
//            withContext(Dispatchers.Main) {
                var res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        setAreaList(res)
                        Log.d(TAG, "onResponse: $res")
                    }

                } else {
                    Log.d(TAG, "Error : ${response.message()} ")
                }
//            }
        }
    }




    private val _selectedProperty = MutableLiveData<Int>()

    val selectedProperty: LiveData<Int>
        get() = _selectedProperty

    fun displayAreaDetails(areaId: Int) {
        _selectedProperty.value = areaId
    }

    // 원본
//    private fun getAreaList() = viewModelScope.launch {
//        val responseAreaList = AreaService().getAreaList()
//        var areaList = mutableListOf<Area>()
//
//        withContext(Dispatchers.Main) {
//            if (responseAreaList.code() == 200) {
////                emitSource(responseAreaList.body())
//                areaList = responseAreaList.body() as MutableList<Area>
//                setAreaList(responseAreaList.body() as MutableList<Area>)
////                _areaResponse.value = responseAreaList.body() as MutableList<Area>
////                _areaResponse.postValue(responseAreaList.body() as MutableList<Area>)
//                Log.d(TAG, "getAreaList: ${responseAreaList.body()}")
//            } else {
//                Log.d(TAG, "Error : ${responseAreaList.message()} ")
//            }
//        }
//    }


//    val getAreaList = _areaResponse.switchMap {
//        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
//            emit(AreaService().getAreaList().body())
//        }
//    }

//    private val userId: LiveData<String> = MutableLiveData()
//    val user = userId.switchMap { id ->
//        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
//            emit(AreaService().getAreaList())
//        }
//    }







//    val currentTimeTransformed = currentTime.switchMap {
//        liveData(defaultDispatcher) { emit(timeStampToTime(it)) }
//    }
    // Called when the user clicks on the "FETCH NEW DATA" button. Updates value in data source.
//    fun onRefresh() {
//        // Launch a coroutine that reads from a remote data source and updates cache
//        viewModelScope.launch {
//            dataSource.fetchNewData()
//        }
//    }

//    private val getAreaLists = viewModelScope.launch {
//        val responseAreaList = AreaService().getAreaList()
////        var areaList = mutableListOf<Area>()
////        withContext(Dispatchers.Main) {
//        if (responseAreaList.code() == 200) {
//            _areaResponse = responseAreaList.body() as MutableLiveData<MutableList<Area>>
//            Log.d(TAG, "getAreaList: ${responseAreaList.body()}")
//        } else {
//            Log.d(TAG, "Error : ${responseAreaList.message()} ")
//        }
////        }
////        emit(areaList)
//    }

//    private var _areaResponse = mutableListOf<Area>()
//    var job: Job? = null
//    val areaList : MutableList<Area>
//        get() = _areaResponse
//
//    fun setAreaList(areaList : MutableList<Area>) {
//        _areaResponse = areaList
//    }

//    fun getAreaList() {
//        viewModelScope.launch() {
////            val result = Logic().doWork()
////            Log.d(TAG, "getAreaList: $result")
////            _areaResponse = result as MutableList<Area>
////            AreaService().getAreas(AreaCallback())
//            _areaResponse = RetrofitUtil.areaService.listAreas() as MutableList<Area>
//            Log.d(TAG, "getAreaList: ${_areaResponse}")
//        }

//        job = CoroutineScope(Dispatchers.IO).launch {
//            val response = RetrofitUtil.areaService.listAreas()
//            withContext(Dispatchers.Main) {
//                if (response.code() == 200) {
//                    _areaResponse = response.body() as MutableList<Area>
//                    Log.d(TAG, "getAreaList: ${response.body()}")
//                } else {
//                    Log.d(TAG, "Error : ${response.message()} ")
//                }
//            }
//        }
//    }

//    inner class AreaCallback: RetrofitCallback<List<Area>> {
//        override fun onError(t: Throwable) {
//            Log.d(TAG, "onError: $t")
//        }
//
//        override fun onSuccess(code: Int, responseData: List<Area>) {
//            Log.d(TAG, "onSuccess: $responseData")
//            _areaResponse = responseData as MutableList<Area>
//        }
//
//        override fun onFailure(code: Int) {
//            Log.d(TAG, "onFailure: ")
//        }
//    }



//    inner class Logic {
//        suspend fun doWork(): Any {
//
//            return suspendCancellableCoroutine { cont ->
//                cont.invokeOnCancellation {
//                    cancel()
//                }
//                val areaRequest: Call<List<Area>> = RetrofitUtil.areaService.listArea()
//                areaRequest.enqueue(object : Callback<List<Area>> {
//                    override fun onResponse(
//                        call: Call<List<Area>>,
//                        response: Response<List<Area>>
//                    ) {
//                        val res = response.body()
//                        if (response.code() == 200) {
//                            if (res != null) {
//                                cont.resume(res)
////                                callback.onSuccess(response.code(), res)
//                                Log.d("Home", "onResponse: $res")
////                        responseData = res
//                            }
//                        } else {
//                            cont.resumeWithException(HttpException(response))
////                            callback.onFailure(response.code())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<Area>>, t: Throwable) {
//                        cont.resumeWithException(t)
////                        callback.onError(t)
//                    }
//                })
//            }
//        }
//    }
}
