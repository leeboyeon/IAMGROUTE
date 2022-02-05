package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailBinding
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.AreaTabPagerAdapter


class RouteDetailFragment : BaseFragment<FragmentRouteDetailBinding>(
    FragmentRouteDetailBinding::bind,
    R.layout.fragment_route_detail
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var routeDetailThemeAdapter: RouteDetailThemeAdapter

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

        routeDetailThemeAdapter = RouteDetailThemeAdapter(themeList)
        binding.RouteDetailThemeRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = routeDetailThemeAdapter
        }

        val routeDetailTabPagerAdapter = RouteDetailTabPagerAdapter(this)
        val tabList = arrayListOf("Info","Review")

        routeDetailTabPagerAdapter.addFragment(RouteDetailInfoFragment())
        routeDetailTabPagerAdapter.addFragment(RouteDetailReviewFragment())

        binding.RouteDetailVpLayout.adapter = routeDetailTabPagerAdapter
        TabLayoutMediator(binding.RouteDetailTablayout, binding.RouteDetailVpLayout) {tab, position ->
            tab.text = tabList.get(position)
        }.attach()


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