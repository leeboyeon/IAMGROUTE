package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteBinding
import com.ssafy.groute.databinding.FragmentRouteCreateBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.home.CategoryAdapter
import com.ssafy.groute.src.main.home.HomeAreaAdapter
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.src.viewmodel.HomeViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteFragment_Groute"
class RouteFragment : BaseFragment<FragmentRouteBinding>(FragmentRouteBinding::bind, R.layout.fragment_route) {
    private lateinit var mainActivity: MainActivity
    lateinit var routeAreaAdapter: HomeAreaAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val planViewModel: PlanViewModel by activityViewModels()
    private var areaId = 1
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)


    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getUserPlanList()
            planViewModel.getThemeList()
            homeViewModel.getAreaLists()
        }
        initTab()
        initAdapter()
        binding.routeCreateBtn.setOnClickListener {
            mainActivity.moveFragment(1)
        }


    }
    fun initAdapter(){
        homeViewModel.areaList.observe(viewLifecycleOwner, Observer {
            routeAreaAdapter = HomeAreaAdapter(it)
            routeAreaAdapter.setItemClickListener(object : HomeAreaAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, name: String, id: Int) {
                    Log.d(TAG, "onClick: ${id}")
                    areaId = id
                }
            })
            routeAreaAdapter.notifyDataSetChanged()
            binding.routeAreaRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter = routeAreaAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

        ThemeAdapter = RouteThemeRecyclerviewAdapter(requireContext())
        binding.routeThemeRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ThemeAdapter
        }
        ThemeAdapter.setItemClickListener(object : RouteThemeRecyclerviewAdapter.ItemClickListener {
            override fun onClick(position: Int, id: Int, selectCheck: ArrayList<Int>) {
                ThemeAdapter.notifyDataSetChanged()
                Log.d(TAG, "onClick: $id")
                Log.d(TAG, "onClick selectCheck List : $selectCheck")
                var list = mutableListOf<UserPlan>()
                var select = mutableListOf<Int>()
                for(i in 0 until selectCheck.size) {
                    if(selectCheck.get(i) == 1) {
                        select.add(i + 1)
                    }
                }
//                Log.d(TAG, "onClick select List: $select")
//                planViewModel.userPlanByDayList.observe(viewLifecycleOwner, Observer {
//                    for(i in 0 until it.size) {
//                        var themeList = it.get(i).themeIdList
//                        if(select.size == themeList.size) {
//                            var flag = true
//                            for(j in 0 until select.size) {
//                                if(select.get(j) == themeList.get(j)) {
//                                    continue
//                                }else {
//                                    flag = false
//                                    break
//                                }
//                            }
//                            if(flag) {
//                                Log.d(TAG, "onClick flag == true : ${it.get(i)}")
//                                list.add(it.get(i))
//                            }
//                        }
//                    }
//                    planViewModel.setUserPlanByDayList(list)
//                    Log.d(TAG, "onClick setUserPlanByDayList : $list")
//                })
            }

        })

        planViewModel.userPlanByDayList.observe(viewLifecycleOwner, Observer {
            RouteListAdapter = RouteListRecyclerviewAdapter(planViewModel, viewLifecycleOwner)
            RouteListAdapter.setRouteList(it)
            binding.routeListRv.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = RouteListAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            RouteListAdapter.setItemClickListener(object : RouteListRecyclerviewAdapter.ItemClickListener {
                override fun onClick(position: Int, id: Int) {
                    mainActivity.moveFragment(12, "planId", id)
                }

            })
        })

    }

    fun initTab() {
        binding.routeTabLayout.addTab(binding.routeTabLayout.newTab().setText("2박3일"))
        binding.routeTabLayout.addTab(binding.routeTabLayout.newTab().setText("3박4일"))
        binding.routeTabLayout.addTab(binding.routeTabLayout.newTab().setText("4박5일"))

        binding.routeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        planViewModel.getRoutebyDay(0)
                    }
                    1 -> {
                        planViewModel.getRoutebyDay(1)
                    }
                    2 -> {
                        planViewModel.getRoutebyDay(2)
                    }
                    3 -> {
                        planViewModel.getRoutebyDay(3)
                    }
                    4 -> {
                        planViewModel.getRoutebyDay(4)
                    }
                    5 -> {
                        planViewModel.getRoutebyDay(5)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    fun initRouteListAdapter() {
        planViewModel.userPlanList.observe(viewLifecycleOwner, Observer {

        })

    }


}