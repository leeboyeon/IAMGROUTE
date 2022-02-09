package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailBinding
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.AreaTabPagerAdapter
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteDetailFragment"

class RouteDetailFragment : BaseFragment<FragmentRouteDetailBinding>(
    FragmentRouteDetailBinding::bind,
    R.layout.fragment_route_detail
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var routeDetailThemeAdapter: RouteDetailThemeAdapter
    private var planIdDetail = -1
    private var planIdUser = -1
    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var bottomSheetRecyclerviewAdapter: BottomSheetRecyclerviewAdapter

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {
            planIdDetail = it.getInt("planIdDetail", -1)
            planIdUser = it.getInt("planIdUser", -1)
        }
        Log.d(TAG, "onCreate: ${planIdDetail}")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getPlanById(planIdDetail, false)
        }

        initAdapter()

        val routeDetailTabPagerAdapter = RouteDetailTabPagerAdapter(this)
        val tabList = arrayListOf("Info", "Review")

        routeDetailTabPagerAdapter.addFragment(
            RouteDetailInfoFragment.newInstance(
                "planId",
                planIdDetail
            )
        )
        routeDetailTabPagerAdapter.addFragment(
            RouteDetailReviewFragment.newInstance(
                "planId",
                planIdDetail
            )
        )

        binding.RouteDetailVpLayout.adapter = routeDetailTabPagerAdapter
        TabLayoutMediator(
            binding.RouteDetailTablayout,
            binding.RouteDetailVpLayout
        ) { tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        binding.RouteDetailIbtnBack.setOnClickListener {
//            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
//            mainActivity.supportFragmentManager.popBackStack()
            mainActivity.moveFragment(16, "planId", -1, "flag", -1)
        }

        binding.RouteDetailAddPlanBtn.setOnClickListener {
            showRouteAddBottomSheet()
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

    fun showRouteAddBottomSheet() {
        var dialogView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.plan_add_bottom_sheet, null)
        Log.d(TAG, "showRouteAddBottomSheet: $planIdUser")
        if (planIdUser == -1) { // 그냥 루트 페이지에서 넘어왔을때 진행중인 일정들 다 보여주기
            runBlocking {
                planViewModel.getMyNotendPlan(ApplicationClass.sharedPreferencesUtil.getUser().id)
            }
            planViewModel.setUserNotPlanList() // 리싸이클러뷰에 바인딩 된 변수에 planNotEndList 할당


        } else {
            runBlocking {
                planViewModel.getPlanById(planIdUser, true)
            }
        }
        planViewModel.userPlan.observe(viewLifecycleOwner, Observer {
            bottomSheetRecyclerviewAdapter =
                BottomSheetRecyclerviewAdapter(viewLifecycleOwner, planViewModel, requireContext())
            bottomSheetRecyclerviewAdapter.setUserPlanList(it)
            Log.d(TAG, "showRouteAddBottomSheet: $it")

            var recyclerview =
                dialogView.findViewById<RecyclerView>(R.id.bottom_sheet_recyclerview)
            recyclerview.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = bottomSheetRecyclerviewAdapter
                adapter!!.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            bottomSheetRecyclerviewAdapter.setItemClickListener(object : BottomSheetRecyclerviewAdapter.ItemClickListener {
                override fun onClick(position: Int, day: Int) {

                }

            })


        })
        var dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialogView.findViewById<RelativeLayout>(R.id.bottom_sheet_route_add_btn)
            .setOnClickListener {
                dialog.dismiss()
            }
        dialog.setTitle("")
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)


    }

    companion object {
        @JvmStatic
        fun newInstance(key1: String, value1: Int, key2: String, value2: Int) =
            RouteDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                    putInt(key2, value2)
                }
            }
    }
}