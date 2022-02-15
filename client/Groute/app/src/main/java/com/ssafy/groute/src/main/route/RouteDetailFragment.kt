package com.ssafy.groute.src.main.route

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailBinding
import com.ssafy.groute.src.dto.PlanLike
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.AreaTabPagerAdapter
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var addDay = -1 // 선택한 day에 루트 추가
    private var selectUserPlan = UserPlan() // 선택한 userPlan에 루트 추가
    private var isAddPlan = false
    lateinit var userId: String

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
            planViewModel.getPlanById(planIdDetail, 2)
        }
        userId = ApplicationClass.sharedPreferencesUtil.getUser().id

        initAdapter()
        initData()

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
            if(planIdUser == -1) {
                mainActivity.moveFragment(16, "planId", -1, "flag", -1)
            } else {
                if(isAddPlan) {
                    mainActivity.moveFragment(16, "planId", -1, "flag", -1)
                } else {
                    mainActivity.moveFragment(16, "planId", planIdUser, "flag", 0)
                }
            }
        }
        binding.routeDetailAbtnHeart.setOnClickListener {
            if(binding.routeDetailAbtnHeart.progress > 0F){
                binding.routeDetailAbtnHeart.pauseAnimation()
                binding.routeDetailAbtnHeart.progress = 0F
                planLike(PlanLike(userId, planIdDetail))
            }else{
                val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
                animator.addUpdateListener { animation ->
                    binding.routeDetailAbtnHeart.progress = animation.animatedValue as Float
                }
                animator.start()
                planLike(PlanLike(userId, planIdDetail))
            }
        }

        binding.RouteDetailAddPlanBtn.setOnClickListener {
            showRouteAddBottomSheet()
        }
    }

    fun initAdapter() {
            planViewModel.theme.observe(viewLifecycleOwner, Observer {
                routeDetailThemeAdapter = RouteDetailThemeAdapter(viewLifecycleOwner, planViewModel)
                Log.d(TAG, "initAdapter Theme: ")
                routeDetailThemeAdapter.setThemeList(it)
                routeDetailThemeAdapter.setHasStableIds(true)
                binding.RouteDetailThemeRv.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = routeDetailThemeAdapter
                }
            })

    }

    fun initData() {
        UserPlanService().planIsLike(PlanLike(userId, planIdDetail), object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 찜하기 여부 에러")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                if(responseData) {
                    binding.routeDetailAbtnHeart.progress = 0.5F
                } else {
                    binding.routeDetailAbtnHeart.progress = 0F
                }
            }
            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    /**
     * 내 일정에 추가하기 클릭 후 BottomSheet show
     */
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
                planViewModel.getPlanById(planIdUser, 1)
            }
        }
        planViewModel.userPlan.observe(viewLifecycleOwner, Observer {
            bottomSheetRecyclerviewAdapter =
                BottomSheetRecyclerviewAdapter(viewLifecycleOwner, planViewModel, requireContext(), true)
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
                override fun onClickDay(position: Int, day: Int, userPlan: UserPlan) {
                    Log.d(TAG, "onClick day: $day")
                    addDay = day
                    selectUserPlan = userPlan

                }


            })

        })
        var dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)

        // 일정 등록 버튼을 눌렀을때
        dialogView.findViewById<RelativeLayout>(R.id.bottom_sheet_userplan_create_btn).setOnClickListener {
            dialog.dismiss()
            mainActivity.moveFragment(1)
        }
        dialogView.findViewById<ConstraintLayout>(R.id.bottom_sheet_route_add_btn)
            .setOnClickListener {
                if(addDay != -1 && selectUserPlan.id != 0) {
                    UserPlanService().insertPlanToUserPlan(addDay, planViewModel.planList.value!!.id, selectUserPlan, object : RetrofitCallback<Boolean> {
                        override fun onError(t: Throwable) {
                            Log.d(TAG, "onError: 루트 반자동 추가 에러")
                        }

                        override fun onSuccess(code: Int, responseData: Boolean) {
                            Log.d(TAG, "onSuccess: 루트 반자동 추가 성공")
                            showCustomToast("일정에 추가되었습니다!")
                            isAddPlan = true
                        }

                        override fun onFailure(code: Int) {
                            Log.d(TAG, "onFailure: ")
                        }

                    })
                    dialog.dismiss()
                } else if(addDay == -1 && selectUserPlan.id != 0){
                    showCustomToast("day를 선택해주세요.")
                } else if(selectUserPlan.id == 0 && addDay != -1) {
                    showCustomToast("일정을 선택해주세요.")
                } else{
                    showCustomToast("일정과 day를 선택해주세요.")
                }

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

    fun planLike(planLike: PlanLike) {
        UserPlanService().planLike(planLike, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 루트 좋아요 에러")
            }
            override fun onSuccess(code: Int, responseData: Boolean) {
                runBlocking {
                    planViewModel.getPlanById(planIdDetail, 3)
                }
                planViewModel.planList.observe(viewLifecycleOwner, Observer {
                    binding.placeDetailTvHeart.text = "${it.heartCnt}"
                })
                Log.d(TAG, "onSuccess: 루트 좋아요 성공")

            }
            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
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