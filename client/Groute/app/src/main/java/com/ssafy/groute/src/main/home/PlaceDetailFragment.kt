package com.ssafy.groute.src.main.home

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass

import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentPlaceDetailBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.response.PlaceLikeResponse
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking


// place 하나 선택 했을 때 장소에 대한 정보를 보여주는 화면
private const val TAG = "PlaceDetailF_Groute"
class PlaceDetailFragment : BaseFragment<FragmentPlaceDetailBinding>(FragmentPlaceDetailBinding::bind, R.layout.fragment_place_detail) {
//    private lateinit var binding: FragmentPlaceDetailBinding
    private lateinit var mainActivity : MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private val planViewModel:PlanViewModel by activityViewModels()
    private var placeId = -1
    private var planId = -1
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
            planId = it.getInt("planId", -1)
            Log.d(TAG, "onAttach: $placeId")
            Log.d(TAG, "onAttach_PLAN: ${planId}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.placeViewModel = placeViewModel

        runBlocking {
            placeViewModel.getPlace(placeId)
            planViewModel.getPlanMyList(ApplicationClass.sharedPreferencesUtil.getUser().id)
        }
        binding.placeDetailAbtnHeart.progress = 0.5f
        val areaTabPagerAdapter = AreaTabPagerAdapter(this)
        val tabList = arrayListOf("Info","Review")

        areaTabPagerAdapter.addFragment(InfoFragment.newInstance("placeId",placeId))
        areaTabPagerAdapter.addFragment(ReviewFragment.newInstance("placeId",placeId))

        binding.pdVpLayout.adapter = areaTabPagerAdapter
        TabLayoutMediator(binding.pdTablayout, binding.pdVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        binding.placeDatilIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
        planViewModel.planMyList.observe(viewLifecycleOwner, Observer {
            if(it.size > 0){
                binding.placeDetailBtnAddList.setOnClickListener {
                    binding.placeDetailLottieAddPlan.playAnimation()
                    placeViewModel.place.observe(viewLifecycleOwner, Observer {
                        planViewModel.insertPlaceShopList(it)
                        Log.d(TAG, "onViewCreated_PlaceSHOP: ${it}")
                        showCustomToast("추가되었습니다.")
                    })
                }
            }else{
                binding.placeDetailBtnAddList.setOnClickListener {
                    showCustomToast("추가하실 일정이 없습니다")
                }
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(key1:String, value1:Int, key2:String, value2:Int) =
            PlaceDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1,value1)
                    putInt(key2,value2)
                }
            }
    }


}