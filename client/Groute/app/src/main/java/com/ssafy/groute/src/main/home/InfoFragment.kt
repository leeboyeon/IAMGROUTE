package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentAreaBinding
import com.ssafy.groute.databinding.FragmentInfoBinding
import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.util.RetrofitCallback
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView



private const val TAG = "InfoFragment"
class InfoFragment : BaseFragment<FragmentInfoBinding>(FragmentInfoBinding::bind, R.layout.fragment_info) {
    private var placeId = -1
    private lateinit var mainActivity : MainActivity
//    private lateinit var binding: FragmentInfoBinding

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
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentInfoBinding.inflate(layoutInflater,container,false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
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
    fun initData(){
        Log.d(TAG, "initData: $placeId")
        val placesDetail = PlaceService().getPlace(placeId, placesCallback())

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
    inner class placesCallback : RetrofitCallback<Places> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Places) {
            Log.d(TAG, "onSuccess: ${responseData}")
            val places = Places(
                responseData.address,
                responseData.areaId,
                responseData.contact,
                responseData.description,
                responseData.heartCnt,
                responseData.id,
                responseData.img,
                responseData.lat,
                responseData.lng,
                responseData.name,
                responseData.rate,
                responseData.themeId,
                responseData.type,
                responseData.userId,
                responseData.zipCode
            )
            binding.placeDetail = places
//            binding.placeDetailTvDescript.text = responseData.description
//            binding.infoTvAddr.text = responseData.address
//            if(responseData.contact == "" || responseData.contact ==null){
//                binding.infoTvPhone.text = "없음"
//            }else{
//                binding.infoTvPhone.text = responseData.contact
//            }
//
//            binding.placeDetailTvBigContent.text = responseData.name+"은"
            Glide.with(this@InfoFragment)
                .load("${ApplicationClass.IMGS_URL_PLACE}${responseData.img}")
                .into(binding.placeDetailIvSomenail)

            lat = responseData.lat.toDouble()
            lng = responseData.lng.toDouble()

            createMap()


        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: $code")

        }

    }
}
