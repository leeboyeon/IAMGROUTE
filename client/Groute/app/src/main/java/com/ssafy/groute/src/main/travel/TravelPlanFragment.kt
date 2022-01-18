package com.ssafy.groute.src.main.travel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.route.RouteListFragment
import com.ssafy.groute.src.main.route.RouteTabPageAdapter


class TravelPlanFragment : Fragment() {
    lateinit var binding: FragmentTravelPlanBinding
    private lateinit var mainActivity: MainActivity
    lateinit var con : ViewGroup
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
        con = container!!
        binding = FragmentTravelPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

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