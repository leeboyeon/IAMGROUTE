package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentInfoBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView



private const val TAG = "InfoFragment"
class InfoFragment : BaseFragment<FragmentInfoBinding>(FragmentInfoBinding::bind, R.layout.fragment_info) {
    private var placeId = -1
    private lateinit var mainActivity : MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    var lat:Double = 0.0
    var lng:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            placeId = it.getInt("placeId", -1)
            Log.d(TAG, "onAttach: $placeId")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModels = placeViewModel
        runBlocking {
            placeViewModel.getPlace(placeId)
        }
        createMap()
//        initData()
    }
    fun createMap(){
        val mapView = MapView(requireContext())
        binding.kakaoMapView.addView(mapView)
        val mapPoint = MapPoint.mapPointWithGeoCoord(lat,lng)
        Log.d(TAG, "createMap: $lat  // $lng")
        mapView.setMapCenterPoint(mapPoint, true)
        mapView.setZoomLevel(3, true)

        val marker = MapPOIItem()
        marker.itemName = binding.placeDetailTvBigContent.text.toString()
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin

        mapView.addPOIItem(marker)
    }

    companion object {

        @JvmStatic
        fun newInstance(key:String, value:Int) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(key,value)
                }
            }
    }

}
