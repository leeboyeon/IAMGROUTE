package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailBinding
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.AreaTabPagerAdapter
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteDetailFragment_groute"
class RouteDetailFragment : BaseFragment<FragmentRouteDetailBinding>(
    FragmentRouteDetailBinding::bind,
    R.layout.fragment_route_detail
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var routeDetailThemeAdapter: RouteDetailThemeAdapter
    private var planId = -1
    private val planViewModel: PlanViewModel by activityViewModels()

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
        Log.d(TAG, "onCreate: ${planId}")
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

        initAdapter()

        val routeDetailTabPagerAdapter = RouteDetailTabPagerAdapter(this)
        val tabList = arrayListOf("Info","Review")

        routeDetailTabPagerAdapter.addFragment(RouteDetailInfoFragment.newInstance("planId", planId))
        routeDetailTabPagerAdapter.addFragment(RouteDetailReviewFragment.newInstance("planId", planId))

        binding.RouteDetailVpLayout.adapter = routeDetailTabPagerAdapter
        TabLayoutMediator(binding.RouteDetailTablayout, binding.RouteDetailVpLayout) {tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        binding.RouteDetailIbtnBack.setOnClickListener{
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }


    }

    fun initAdapter() {
        planViewModel.theme.observe(viewLifecycleOwner, Observer {
            routeDetailThemeAdapter = RouteDetailThemeAdapter(viewLifecycleOwner, planViewModel)
            routeDetailThemeAdapter.setThemeList(it)
            routeDetailThemeAdapter.setHasStableIds(true)
            binding.RouteDetailThemeRv.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = routeDetailThemeAdapter
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String, value: Int) =
            RouteDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}