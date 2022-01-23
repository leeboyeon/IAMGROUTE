package com.ssafy.groute.src.main.home

import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentPlaceDetailBinding
import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "PlaceDetailFragment"
class PlaceDetailFragment : Fragment() {
    private lateinit var binding: FragmentPlaceDetailBinding
    private lateinit var mainActivity : MainActivity
    
    private var placeId = -1
    private lateinit var places:Places
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val areaTabPagerAdapter = AreaTabPagerAdapter(this)
        val tabList = arrayListOf("Info","Review")

        areaTabPagerAdapter.addFragment(InfoFragment.newInstance("placeId",placeId))
        areaTabPagerAdapter.addFragment(ReviewFragment())

        binding.pdVpLayout.adapter = areaTabPagerAdapter
        TabLayoutMediator(binding.pdTablayout, binding.pdVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        initData()

        binding.placeDetailAbtnHeart.setOnClickListener {

            val place:Places = Places(
                places.address,
                places.areaId,
                places.contact,
                places.description,
                places.heartCnt+1,
                placeId,
                places.img,
                places.lat,
                places.lng,
                places.name,
                places.rate,
                places.themeId,
                places.type,
                ApplicationClass.sharedPreferencesUtil.getUser().id,
                places.zipCode
            )

            updatePlace(place)
        }

    }
    fun updatePlace(place : Places){
        PlaceService().updatePlace(place, placeUpdateCallback())
    }
    fun initData(){
        Log.d(TAG, "initData: $placeId")
        val placesDetail = PlaceService().getPlace(placeId, placesCallback())
    }
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
                val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
                animator.addUpdateListener { animation ->
                    binding.placeDetailAbtnHeart.progress = animation.animatedValue as Float
                }
                animator.start()
                Log.d(TAG, "onSuccess: 업데이트 성공")
            }else{
                Log.d(TAG, "onSuccess: 통신오류")
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
    inner class placesCallback : RetrofitCallback<Places>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Places) {
            binding.placeDetailTvName.text = responseData.name
            binding.placeDetailTvStar.text = responseData.rate.toFloat().toString()
            places = responseData
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: $code")
            
        }

    }
}