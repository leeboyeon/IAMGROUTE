package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentAreaBinding
import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "AreaFragment"
class AreaFragment : Fragment() {
    private lateinit var binding: FragmentAreaBinding
    private lateinit var mainActivity:MainActivity
    private lateinit var areaFilterAdapter:AreaFilterAdapter

//    val lists = listOf<Places>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAreaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTab()

    }

    fun initAdapter(){
        PlaceService().getPlaces(PlaceCallback())


    }
    fun initTab(){
        initAdapter()
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
                //
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    companion object {

        @JvmStatic
        fun newInstance(key:String, value:Int) =
            AreaFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
    inner class PlaceCallback:RetrofitCallback<List<Places>>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: List<Places>) {
//            Log.d(TAG, "onSuccess: ${responseData}")
            responseData.let{
                areaFilterAdapter = AreaFilterAdapter(responseData)
                areaFilterAdapter.list = responseData
                areaFilterAdapter.setItemClickListener(object : AreaFilterAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, placeId: Int) {
                        mainActivity.moveFragment(4,"placeId", placeId)
                    }

                })
            }
            binding.areaRvPlaceitem.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = areaFilterAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
}