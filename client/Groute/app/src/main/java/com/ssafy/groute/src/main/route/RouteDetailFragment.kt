package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailBinding
import com.ssafy.groute.src.main.MainActivity


class RouteDetailFragment : BaseFragment<FragmentRouteDetailBinding>(FragmentRouteDetailBinding::bind, R.layout.fragment_route_detail) {
    private lateinit var mainActivity: MainActivity
    private lateinit var routeDetailThemeAdapter: RouteDetailThemeAdapter
    private lateinit var routeDetailDayPerAdapter: RouteDetailDayPerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val themeList = arrayListOf("#힐링", "#관광지")
        val dayList = arrayListOf("1 DAY", "2 DAY")
        routeDetailThemeAdapter = RouteDetailThemeAdapter(themeList)
        binding.RouteDetailThemeRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = routeDetailThemeAdapter
        }

        routeDetailDayPerAdapter = RouteDetailDayPerAdapter(dayList)
        binding.RouteDetailDayPerRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = routeDetailDayPerAdapter
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}