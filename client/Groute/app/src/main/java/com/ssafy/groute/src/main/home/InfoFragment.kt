package com.ssafy.groute.src.main.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.contains
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kakao.kakaonavi.options.CoordType
import com.kakao.kakaonavi.options.RpOption
import com.kakao.kakaonavi.options.VehicleType
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentInfoBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.Exception
import java.util.*
import com.kakao.kakaonavi.Destination
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.NaviOptions
import java.lang.RuntimeException


private const val TAG = "InfoFragment"
class InfoFragment : BaseFragment<FragmentInfoBinding>(FragmentInfoBinding::bind, R.layout.fragment_info) {
    private var placeId = -1
    private lateinit var mainActivity : MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private val planViewModel: PlanViewModel by activityViewModels()
    var lat:Double = 0.0
    var lng:Double = 0.0
    var mapViewcontainer:RelativeLayout? = null
    private lateinit var mapView:MapView
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
        binding.infoBtnFindRoad.setOnClickListener {
            placeViewModel.place.observe(viewLifecycleOwner, Observer {
                lat = it.lat.toDouble()
                lng = it.lng.toDouble()
                goNavi(lat, lng)
            })

        }
    }
    fun createMap(){
        mapView = MapView(requireContext())
        val marker = MapPOIItem()
        placeViewModel.place.observe(viewLifecycleOwner, Observer {

            binding.kakaoMapView.addView(mapView)
            val mapPoint = MapPoint.mapPointWithGeoCoord(it.lat.toDouble(),it.lng.toDouble())
            Log.d(TAG, "createMap: $lat  // $lng")
            mapView.setMapCenterPoint(mapPoint, true)
            mapView.setZoomLevel(3, true)
            marker.itemName = binding.placeDetailTvBigContent.text.toString()
            marker.mapPoint = mapPoint
            marker.markerType = MapPOIItem.MarkerType.BluePin
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        })

        mapView.addPOIItem(marker)

    }

    fun goNavi(destLat:Double, destLng:Double){
        try {
            if (KakaoNaviService.isKakaoNaviInstalled(requireContext())) {

                val kakao: com.kakao.kakaonavi.Location =
                    Destination.newBuilder("destination", destLng, destLat).build()

                val params = KakaoNaviParams.newBuilder(kakao)
                    .setNaviOptions(
                        NaviOptions.newBuilder()
                            .setCoordType(CoordType.WGS84) // WGS84로 설정해야 경위도 좌표 사용 가능.
                            .setRpOption(RpOption.NO_AUTO)
                            .setStartAngle(200) //시작 앵글 크기 설정.
                            .setVehicleType(VehicleType.FIRST).build()
                    ).build() //길 안내 차종 타입 설정

                KakaoNaviService.navigate(requireContext(), params)

            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa")
                )
                Log.e(TAG, "showNaviKakao: 네비 설치 안됨")
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e("네비연동 에러", e.toString() + "")
        }
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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.kakaoMapView.removeView(mapView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onSurfaceDestroyed()
    }
}
