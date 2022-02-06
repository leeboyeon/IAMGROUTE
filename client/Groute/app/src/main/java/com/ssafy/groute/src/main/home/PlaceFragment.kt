package com.ssafy.groute.src.main.home

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentAreaBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.response.PlaceLikeResponse
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

// Place List 보여주는 화면
private const val TAG = "AreaFragment_Groute"
class PlaceFragment : BaseFragment<FragmentAreaBinding>(FragmentAreaBinding::bind, R.layout.fragment_area) {
    private val placeViewModel: PlaceViewModel by activityViewModels()

    private lateinit var mainActivity : MainActivity
    private lateinit var areaFilterAdapter : PlaceFilterAdapter
    private var planId = -1
//    val lists = listOf<Places>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            planId = it.getInt("planId",-1)
            Log.d(TAG, "onAttach: ${planId}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = placeViewModel
        // place List 데이터 서버로부터 받아오기
        runBlocking {
            placeViewModel.getPlaceList()
        }
        runBlocking {
            placeViewModel.getPlaceLikeList(ApplicationClass.sharedPreferencesUtil.getUser().id)
            Log.d(TAG, "onViewCreated_Id: ${ApplicationClass.sharedPreferencesUtil.getUser().id}")
            Log.d(TAG, "onViewCreated: ${placeViewModel.placeLikeList.value}")
        }
        initTab()
        initAdapter()
    }

    /**
     * Init RecyclerView by Place List
     */
    private fun initAdapter() {
        placeViewModel.placeList.observe(viewLifecycleOwner, Observer {
            areaFilterAdapter = PlaceFilterAdapter(it, placeViewModel.placeLikeList, viewLifecycleOwner)
            areaFilterAdapter.setItemClickListener(object : PlaceFilterAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, placeId: Int) {
                    mainActivity.moveFragment(4,"placeId", placeId,"planId",planId)
                }
            })
            areaFilterAdapter.setHeartClickListener(object :PlaceFilterAdapter.HeartClickListener{
                override fun onClick(view: View, position: Int, placeId: Int) {
                    Log.d(TAG, "onClick: CLICKED")
                    var placeLike = PlaceLikeResponse(
                        0,
                        ApplicationClass.sharedPreferencesUtil.getUser().id,
                        placeId
                    )
                    placeGoLike(placeLike)
                }

            })
            binding.areaRvPlaceitem.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = areaFilterAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })


    }

    /**
     * Place TabLayout initialize & filtering
     */
    private fun initTab(){
//        initAdapter()
        binding.areaTabLayout.addTab(binding.areaTabLayout.newTab().setText("문화"))
        binding.areaTabLayout.addTab(binding.areaTabLayout.newTab().setText("맛집"))
        binding.areaTabLayout.addTab(binding.areaTabLayout.newTab().setText("숙박"))

        binding.areaTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 ->{
                        areaFilterAdapter.filter.filter("")
                    }
                    1->{
                        areaFilterAdapter.filter.filter("관광지")
                    }
                    2->{
                        areaFilterAdapter.filter.filter("레포츠")
                    }
                    3->{
                        areaFilterAdapter.filter.filter("문화시설")
                    }
                    4->{
                        areaFilterAdapter.filter.filter("음식점")
                    }
                    5->{
                        areaFilterAdapter.filter.filter("숙박")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    fun placeGoLike(placeLike: PlaceLikeResponse){
        Log.d(TAG, "placeGoLike: $placeLike")
        PlaceService().placeLike(placeLike, object :RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                if(responseData){
                    Log.d(TAG, "onSuccessLIKE: ")
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })

    }


    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            PlaceFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
//                    putInt(key1,value1)
                }
            }
    }
}