package com.ssafy.groute.src.main.home

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R

import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentPlaceDetailBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking


// place 하나 선택 했을 때 장소에 대한 정보를 보여주는 화면
private const val TAG = "PlaceDetailF_Groute"
class PlaceDetailFragment : BaseFragment<FragmentPlaceDetailBinding>(FragmentPlaceDetailBinding::bind, R.layout.fragment_place_detail) {
//    private lateinit var binding: FragmentPlaceDetailBinding
    private lateinit var mainActivity : MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()

    private var placeId = -1
    private lateinit var place:Place
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

        binding.placeViewModel = placeViewModel

        runBlocking {
            placeViewModel.getPlace(placeId)
        }
        val areaTabPagerAdapter = AreaTabPagerAdapter(this)
        val tabList = arrayListOf("Info","Review")

        areaTabPagerAdapter.addFragment(InfoFragment.newInstance("placeId",placeId))
        areaTabPagerAdapter.addFragment(ReviewFragment())

        binding.pdVpLayout.adapter = areaTabPagerAdapter
        TabLayoutMediator(binding.pdTablayout, binding.pdVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()

//        initData()

        binding.placeDetailAbtnHeart.setOnClickListener {
            val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
            animator.addUpdateListener { animation ->
                binding.placeDetailAbtnHeart.progress = animation.animatedValue as Float
            }
            animator.start()
        }
        binding.placeDatilIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }
    fun updatePlace(place : Place){
        PlaceService().updatePlace(place, placeUpdateCallback())
    }
//    suspend fun initData(){
//        Log.d(TAG, "initData: $placeId")
//        placeViewModel.getPlace(placeId)
//    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            PlaceDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key,value)
                }
            }
    }
    inner class placeUpdateCallback : RetrofitCallback<Boolean>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){

                Log.d(TAG, "onSuccess: 업데이트 성공")
            }else{
                Log.d(TAG, "onSuccess: 통신오류")
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
////    inner class placesCallback : RetrofitCallback<Place>{
////        override fun onError(t: Throwable) {
////            Log.d(TAG, "onError: ")
////        }
////
////        override fun onSuccess(code: Int, responseData: Place) {
//////                binding.placeDetailTvPlaceName.text = responseData.name
//////            binding.placeDetailTvReview.text = responseData.rate.toFloat().toString()
////            var place = Place(
////                responseData.address,
////                responseData.areaId,
////                responseData.contact,
////                responseData.description,
////                responseData.heartCnt,
////                responseData.id,
////                responseData.img,
////                responseData.lat,
////                responseData.lng,
////                responseData.name,
////                responseData.rate,
////                responseData.themeId,
////                responseData.type,
////                responseData.userId,
////                responseData.zipCode
////            )
////            binding.placeDetail = place
//////            places = responseData
////        }
//
//        override fun onFailure(code: Int) {
//            Log.d(TAG, "onFailure: $code")
//
//        }
//
//    }
}