package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailInfoBinding
import com.ssafy.groute.src.main.MainActivity


class RouteDetailInfoFragment : BaseFragment<FragmentRouteDetailInfoBinding>(FragmentRouteDetailInfoBinding::bind, R.layout.fragment_route_detail_info) {
    private lateinit var routeDetailDayPerAdapter: RouteDetailDayPerAdapter
    private lateinit var mainActivity: MainActivity

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
        val dayList = arrayListOf("1 DAY", "2 DAY")
        routeDetailDayPerAdapter = RouteDetailDayPerAdapter(dayList)
        binding.RouteDetailDayPerRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = routeDetailDayPerAdapter
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteDetailInfoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}