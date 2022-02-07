package com.ssafy.groute.src.main.travel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.my.MyTravelFragment
import com.ssafy.groute.src.main.route.RouteTabPageAdapter
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.view.Display
import android.view.View.inflate
import android.widget.*
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.ssafy.groute.databinding.ActivityCommentNestedBinding.inflate
import java.lang.Integer.max
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.Date.from
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import android.widget.DatePicker
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.datepicker.*
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.databinding.ActivityCommentNestedBinding.bind
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.selects.select
import kotlin.time.days


private const val TAG = "TravelPlanFragment"
//class TravelPlanFragment : BaseFragment<FragmentTravelPlanBinding>(FragmentTravelPlanBinding::bind, R.layout.fragment_travel_plan) {
 class TravelPlanFragment: Fragment(){

    private lateinit var binding:FragmentTravelPlanBinding
    private lateinit var mainActivity: MainActivity
    lateinit var con : ViewGroup
    lateinit var addButton: FloatingActionButton
    lateinit var memoAddButton: FloatingActionButton
    lateinit var routeRecomButton: FloatingActionButton
    lateinit var placeAddButton: FloatingActionButton

    private val planViewModel:PlanViewModel by activityViewModels()
    private val placeViewModel:PlaceViewModel by activityViewModels()

    var isFabOpen: Boolean = false
    private var planId = -1
    private var placeId = -1
    val routeSelectList = arrayListOf<RouteRecom>()

    private lateinit var routeRecomDialogAdapter:RouteRecomDialogAdapter
    private lateinit var travelPlanListRecyclerviewAdapter: TravelPlanListRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
        Log.d(TAG, "onAttach: ${planId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelPlanBinding.inflate(layoutInflater,container,false)
        con = container!!
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ${con}")
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getPlanById(planId)
        }
        initPlaceListAdapter()
        initTabLayout()
//        initPlaceList()
        floatingButtonEvent()
        binding.travelplanBackIv.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
        binding.travelplanCalcBtn.setOnClickListener {
            val money = binding.travelplanCalcIv
            val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
            animator.addUpdateListener { animation ->
                money.progress = animation.animatedValue as Float
            }
            animator.start()
        }

        binding.travelplanMemberBtn.setOnClickListener {
            val member = binding.travelplanMemberIv
            val animator = ValueAnimator.ofFloat(0f,1f).setDuration(1000)
            animator.addUpdateListener { animation ->
                member.progress = animation.animatedValue as Float
            }
            animator.start()
        }
    }



    // 플로팅 버튼 이벤트 처리
    fun floatingButtonEvent() {
        addButton = binding.travelplanAddFb
        //val closeButton = binding.travelplanCloseFb
        memoAddButton = binding.travelplanAddMemoFb
        routeRecomButton = binding.travelplanRecomRouteFb
        placeAddButton = binding.travelplanAddPlaceFb

        addButton.setOnClickListener{
            fbAnim()

        }
        memoAddButton.setOnClickListener{
            fbAnim()
        }
        routeRecomButton.setOnClickListener {
            fbAnim()
            showRouteSelectDialog()
        }
        placeAddButton.setOnClickListener {
            fbAnim()
            mainActivity.moveFragment(15,"planId",planId)
        }
    }

    // 플로팅 버튼 애니메이션 처리
    fun fbAnim() {
        val memoAddLayout = binding.travelplanAddMemoLayout
        val routeRecomLayout = binding.travelplanRecomRouteLayout
        val placeAddLayout = binding.travelplanAddPlaceLayout

        val fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        val fab_close = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        val rotate_close = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anticlockwise)
        val rotate_open = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)

        if(isFabOpen) {
            addButton.startAnimation(rotate_close)
            memoAddLayout.startAnimation(fab_close)
            routeRecomLayout.startAnimation(fab_close)
            placeAddLayout.startAnimation(fab_close)
            memoAddButton.isClickable = false
            routeRecomButton.isClickable = false
            placeAddLayout.isClickable = false
            isFabOpen = false
        } else {
            addButton.startAnimation(rotate_open)
            memoAddLayout.startAnimation(fab_open)
            routeRecomLayout.startAnimation(fab_open)
            placeAddLayout.startAnimation(fab_open)
            memoAddButton.isClickable = true
            routeRecomButton.isClickable = true
            placeAddLayout.isClickable = true
            isFabOpen = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initTabLayout(){
        planViewModel.routeList.observe(viewLifecycleOwner, Observer {
            for(i in 1.. it.size-1){
                binding.travelplanTabLayout.addTab(binding.travelplanTabLayout.newTab().setText(it.get(i).name))
            }
        })
        binding.travelplanTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                travelPlanListRecyclerviewAdapter.filter.filter((tab?.position?.plus(1)).toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }
    @SuppressLint("ClickableViewAccessibility")
    fun initPlaceListAdapter(){
        planViewModel.routeList.observe(viewLifecycleOwner, Observer {
            travelPlanListRecyclerviewAdapter = TravelPlanListRecyclerviewAdapter(requireContext(),it)
            binding.travelplanListRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = travelPlanListRecyclerviewAdapter
            }

            val travelPlanListRvHelperCallback = TravelPlanListRvHelperCallback(travelPlanListRecyclerviewAdapter).apply {
                setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
            }

            ItemTouchHelper(travelPlanListRvHelperCallback).attachToRecyclerView(binding.travelplanListRv)

            binding.travelplanListRv.setOnTouchListener{ _, _ ->
                travelPlanListRvHelperCallback.removePreviousClamp(binding.travelplanListRv)
                false
            }
        })

    }

    fun showRouteSelectDialog(){
        routeRecomDialogAdapter = RouteRecomDialogAdapter(requireContext())
        routeSelectList.apply {
            add(RouteRecom(lottie="oneday.json",typeName="당일일정",typeDescript="하루의 일정만 \n 추천받으시고 싶으신가요?"))
            add(RouteRecom(lottie="allday.json",typeName="전체일정",typeDescript="모든 일정을 \n 추천받으시고 싶으신가요?"))

            routeRecomDialogAdapter.list = routeSelectList
            routeRecomDialogAdapter.notifyDataSetChanged()
        }

        var dialogView:View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_the_recomroute,null)
        var dialogRv = dialogView.findViewById<RecyclerView>(R.id.routeRecom_rv_typeSelect)
        dialogRv.apply {
            layoutManager = GridLayoutManager(requireContext(),1,LinearLayoutManager.HORIZONTAL,false)
            adapter = routeRecomDialogAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        var selectPosition = -1
        routeRecomDialogAdapter.setItemClickListener(object: RouteRecomDialogAdapter.ItemClickListener{
            override fun onClick(view:View, position: Int, type:String) {
                selectPosition = position
            }
        })

        var dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialogView.findViewById<Button>(R.id.routeRecom_btn_cancle).setOnClickListener {
            dialog.cancel()
        }
        dialogView.findViewById<Button>(R.id.routeRecom_btn_ok).setOnClickListener {
            if(selectPosition == 0){
                //당일
                mainActivity.moveFragment(16,"days",1)
                dialog.cancel()
            }
            if(selectPosition == 1){
                //전체
                    var total = 0
                     planViewModel.planList.observe(viewLifecycleOwner, Observer {
                        total = it.totalDate
                    })
                mainActivity.moveFragment(16,"days",total)
                dialog.cancel()
            }
        }
        dialog.setTitle("원하는 타입을 선택해주세요")
        dialog.show()
    }
    companion object {
        @JvmStatic
        fun newInstance(key1: String, value1: Int) =
            TravelPlanFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                }
            }
    }
}