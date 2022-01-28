package com.ssafy.groute.src.main.travel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.databinding.FragmentRouteListBinding
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.route.RouteListFragment
import com.ssafy.groute.src.main.route.RouteTabPageAdapter


class TravelPlanFragment : BaseFragment<FragmentTravelPlanBinding>(FragmentTravelPlanBinding::bind, R.layout.fragment_travel_plan) {
//    lateinit var binding: FragmentTravelPlanBinding
    private lateinit var mainActivity: MainActivity
    lateinit var con : ViewGroup
    lateinit var addButton: FloatingActionButton
    lateinit var memoAddButton: FloatingActionButton
    lateinit var routeRecomButton: FloatingActionButton
    lateinit var placeAddButton: FloatingActionButton
    var isFabOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentTravelPlanBinding.inflate(inflater, container, false)
        con = container!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        floatingButtonEvent()
    }

    // 플로팅 버튼 이벤트 처리
    fun floatingButtonEvent() {
        addButton = binding.travelplanAddFb
        //val closeButton = binding.travelplanCloseFb
        memoAddButton = binding.travelplanAddMemoFb
        routeRecomButton = binding.travelplanRecomRouteFb
        placeAddButton = binding.travelplanAddPlaceFb

        addButton.setOnClickListener{
            fbAnim()
        }
        memoAddButton.setOnClickListener{
            fbAnim()
        }
        routeRecomButton.setOnClickListener {
            fbAnim()
        }
        placeAddButton.setOnClickListener {
            fbAnim()
        }
    }

    // 플로팅 버튼 애니메이션 처리
    fun fbAnim() {
        val memoAddLayout = binding.travelplanAddMemoLayout
        val routeRecomLayout = binding.travelplanRecomRouteLayout
        val placeAddLayout = binding.travelplanAddPlaceLayout

        val fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        val fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        val rotate_close = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anticlockwise)
        val rotate_open = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)

        if(isFabOpen) {
            addButton.startAnimation(rotate_close)
            memoAddLayout.startAnimation(fab_close)
            routeRecomLayout.startAnimation(fab_close)
            placeAddLayout.startAnimation(fab_close)
            memoAddButton.isClickable = false
            routeRecomButton.isClickable = false
            placeAddLayout.isClickable = false
            isFabOpen = false
        } else {
            addButton.startAnimation(rotate_open)
            memoAddLayout.startAnimation(fab_open)
            routeRecomLayout.startAnimation(fab_open)
            placeAddLayout.startAnimation(fab_open)
            memoAddButton.isClickable = true
            routeRecomButton.isClickable = true
            placeAddLayout.isClickable = true
            isFabOpen = true
        }
    }

    fun initAdapter(){
        val travelPlanAdapter = TravelPlanTabPageAdapter(this)

        val tabDayList = arrayListOf("day1", "day2", "day3", "day4")
        val tabDateList = arrayListOf("01.24 월", "01.25 화", "01.26 수", "01.27 목")
        travelPlanAdapter.addFragment(TravelPlanListFragment())
        travelPlanAdapter.addFragment(TravelPlanListFragment())
        travelPlanAdapter.addFragment(TravelPlanListFragment())
        travelPlanAdapter.addFragment(TravelPlanListFragment())

        binding.pager.adapter = travelPlanAdapter

        TabLayoutMediator(binding.travelplanTabLayout, binding.pager) { tab, position ->
            val customTabView = LayoutInflater.from(context).inflate(R.layout.item_travelplan_tab, con, false)
            customTabView.findViewById<TextView>(R.id.item_travelplan_day_tv).text = tabDayList.get(position)
            customTabView.findViewById<TextView>(R.id.item_travelplan_date_tv).text = tabDateList.get(position)
            tab.setCustomView(customTabView)
        }.attach()
    }

}