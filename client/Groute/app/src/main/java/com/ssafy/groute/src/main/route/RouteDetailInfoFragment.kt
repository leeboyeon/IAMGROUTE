package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailInfoBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking


class RouteDetailInfoFragment : BaseFragment<FragmentRouteDetailInfoBinding>(FragmentRouteDetailInfoBinding::bind, R.layout.fragment_route_detail_info) {
    private lateinit var routeDetailDayPerAdapter: RouteDetailDayPerAdapter
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getPlanById(planId)
        }

        routeDetailDayPerAdapter = RouteDetailDayPerAdapter(viewLifecycleOwner, planViewModel.planList.value!!.totalDate, planViewModel)
        binding.RouteDetailDayPerRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = routeDetailDayPerAdapter
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(key: String, value: Int) =
            RouteDetailInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}