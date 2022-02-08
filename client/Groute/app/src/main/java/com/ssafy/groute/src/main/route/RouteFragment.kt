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
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteBinding
import com.ssafy.groute.databinding.FragmentRouteCreateBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.dto.PlanLike
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.home.CategoryAdapter
import com.ssafy.groute.src.main.home.PlaceDetailFragment
import com.ssafy.groute.src.main.home.HomeAreaAdapter
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.HomeViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteFragment_Groute"

class RouteFragment :
    BaseFragment<FragmentRouteBinding>(FragmentRouteBinding::bind, R.layout.fragment_route) {
    //    lateinit var binding: FragmentRouteBinding
    private lateinit var mainActivity: MainActivity
    lateinit var pagerAdapter: RouteTabPageAdapter
    private var days = 0
    lateinit var routeAreaAdapter: HomeAreaAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val planViewModel: PlanViewModel by activityViewModels()
    private var areaId = 1
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter
    var selectedTheme = mutableListOf<Int>()
    var tabPosition = 0
    lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(false)
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            days = it.getInt("days", -1)
            Log.d(TAG, "onAttach: ${days}")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = ApplicationClass.sharedPreferencesUtil.getUser().id
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getUserPlanList()
            planViewModel.getThemeList()
            homeViewModel.getAreaLists()
            planViewModel.getPlanLikeList(userId)
        }

        initTab()
        initAdapter()
        binding.routeCreateBtn.setOnClickListener {
            mainActivity.moveFragment(1)
        }


    }

    fun initAdapter() {
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
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = routeAreaAdapter
                adapter!!.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

        ThemeAdapter = RouteThemeRecyclerviewAdapter(requireContext())
        binding.routeThemeRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ThemeAdapter
        }
        ThemeAdapter.setItemClickListener(object : RouteThemeRecyclerviewAdapter.ItemClickListener {
            override fun onClick(position: Int, id: Int, selectCheck: ArrayList<Int>) {
                ThemeAdapter.notifyDataSetChanged()
                selectedTheme.clear()
                for (i in 0 until selectCheck.size) {
                    if (selectCheck.get(i) == 1) {
                        selectedTheme.add(i + 1)
                    }
                }
                Log.d(TAG, "onClick select List: $selectedTheme")
                planViewModel.getRoutebyDay(tabPosition, selectedTheme)
            }

        })

        planViewModel.userPlanByDayList.observe(viewLifecycleOwner, Observer {
            RouteListAdapter = RouteListRecyclerviewAdapter(planViewModel, viewLifecycleOwner)
            RouteListAdapter.setRouteList(it)
            RouteListAdapter.setHasStableIds(true)
            binding.routeListRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = RouteListAdapter
                adapter!!.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            RouteListAdapter.setItemClickListener(object :
                RouteListRecyclerviewAdapter.ItemClickListener {
                override fun onClick(position: Int, id: Int) {
                    mainActivity.moveFragment(12, "planId", id)
                }

            })

            RouteListAdapter.setHeartClickListener(object :
                RouteListRecyclerviewAdapter.HeartClickListener {
                override fun onClick(view: View, position: Int, planId: Int) {
                    Log.d(TAG, "PlanLike onClick: ")
                    planLike(PlanLike(userId, planId), position)
                }

            })
        })

    }

    fun initTab() {
        binding.routeTabLayout.addTab(binding.routeTabLayout.newTab().setText("2박3일"))
        binding.routeTabLayout.addTab(binding.routeTabLayout.newTab().setText("3박4일"))
        binding.routeTabLayout.addTab(binding.routeTabLayout.newTab().setText("4박5일"))

        binding.routeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tabPosition = 0
                        planViewModel.getRoutebyDay(0, selectedTheme)
                    }
                    1 -> {
                        tabPosition = 1
                        planViewModel.getRoutebyDay(1, selectedTheme)
                    }
                    2 -> {
                        tabPosition = 2
                        planViewModel.getRoutebyDay(2, selectedTheme)
                    }
                    3 -> {
                        tabPosition = 3
                        planViewModel.getRoutebyDay(3, selectedTheme)
                    }
                    4 -> {
                        tabPosition = 4
                        planViewModel.getRoutebyDay(4, selectedTheme)
                    }
                    5 -> {
                        tabPosition = 5
                        planViewModel.getRoutebyDay(5, selectedTheme)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    fun planLike(planLike: PlanLike, position: Int) {
        UserPlanService().planLike(planLike, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 루트 좋아요 에러")
            }
            override fun onSuccess(code: Int, responseData: Boolean) {
                //planViewModel.getRoutebyDay(tabPosition, selectedTheme)
                //RouteListAdapter.notifyDataSetChanged()
                Log.d(TAG, "onSuccess: 루트 좋아요 성공")

            }
            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(key1: String, value1: Int) =
            RouteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                }
            }
    }

    fun initRouteListAdapter() {
        planViewModel.userPlanList.observe(viewLifecycleOwner, Observer {

        })

    }


}