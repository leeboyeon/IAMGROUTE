package com.ssafy.groute.src.main.travel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
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
import com.ssafy.groute.databinding.FragmentRouteListBinding
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.my.MyTravelFragment
import com.ssafy.groute.src.main.route.RouteListFragment
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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
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
import kotlinx.coroutines.selects.select


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
    var isFabOpen: Boolean = false
    private var planId = -1
    private var placeId = -1
    val routeSelectList = arrayListOf<RouteRecom>()
    private lateinit var routeRecomDialogAdapter:RouteRecomDialogAdapter
    private lateinit var placeShopAdapter: PlaceShopAdapter
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
        initAdapter()
        initPlaceList()
        floatingButtonEvent()
    }
    //임시 플레이스 저장리스트
    fun initPlaceList(){
        planViewModel.livePlaceshopList.observe(viewLifecycleOwner, Observer {
            placeShopAdapter = PlaceShopAdapter()
            Log.d(TAG, "initPlaceList: ${it}")
            if(!it.isEmpty() || it != null){
                binding.travelPlanRvPlaceList.visibility = View.VISIBLE

                placeShopAdapter.list = it
                binding.travelPlanRvPlaceList.apply {
                    layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                    adapter = placeShopAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                placeShopAdapter.setItemClickListener(object : PlaceShopAdapter.ItemClickListener{
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onClick(view: View, position: Int, id: Int) {
                        showDataRangePicker()
                    }

                })
            }
        })
    }
    //달력 띄우기
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun showDataRangePicker(){
        var selectDay = ""
        planViewModel.planList.observe(viewLifecycleOwner, Observer {
            val calendar = Calendar.getInstance()
            val minDate = Calendar.getInstance()
            val maxDate = Calendar.getInstance()
            var formmater = SimpleDateFormat("yyyy-MM-dd")
            var sDate = formmater.parse(it.startDate)
            var eDate = formmater.parse(it.endDate)
            calendar.time = sDate

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth -> selectDay = "$year - $month - $dayOfMonth"},
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.datePicker.minDate = sDate.time
            datePickerDialog.datePicker.maxDate = eDate.time

            datePickerDialog.show()
        })


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
    fun initAdapter(){
        planViewModel.planList.observe(viewLifecycleOwner, Observer {
            val travelPlanAdapter = TravelPlanTabPageAdapter(this)
            var totalDays = it.totalDate
            val tabDayList:ArrayList<String> = arrayListOf()
            for(i in 1..totalDays){
                tabDayList.apply {
                    add("day${i}")
                }
            }
            val tabDateList = arrayListOf<String>()
            tabDateList.apply {
                var sdate = it.startDate
                var edate = it.endDate

                var sdateTmp = LocalDate.parse(sdate,DateTimeFormatter.ISO_DATE)
//                var edateTmp = LocalDate.parse(edate,DateTimeFormatter.ISO_DATE)
                for(i in 0..totalDays){
                    add(sdateTmp.plusDays(i.toLong()).toString())
                }
            }
            for(i in 1..totalDays){
                travelPlanAdapter.addFragment(TravelPlanListFragment.newInstance("day",i,"planId",planId))
            }
            binding.pager.adapter = travelPlanAdapter

            TabLayoutMediator(binding.travelplanTabLayout, binding.pager) { tab, position ->
                val customTabView = LayoutInflater.from(context).inflate(R.layout.item_travelplan_tab, con, false)
                customTabView.findViewById<TextView>(R.id.item_travelplan_day_tv).text = tabDayList.get(position)
                customTabView.findViewById<TextView>(R.id.item_travelplan_date_tv).text = tabDateList.get(position)
                tab.setCustomView(customTabView)
            }.attach()
        })

    }

    fun showRouteSelectDialog(){
        routeRecomDialogAdapter = RouteRecomDialogAdapter(requireContext())
        routeSelectList.apply {
            add(RouteRecom(lottie="auto.json",typeName="AUTO",typeDescript="계획을 짜야하지만 시간이 없을 때, 당신이 원하는 지역의 원하는 테마로! 당신의 여행경로를 완성해드립니다"))
            add(RouteRecom(lottie="semiauto.json",typeName="SEMIAUTO",typeDescript="원하는 장소가 있으신가요? 그 장소가 추가된 경로를 추천드립니다!"))
            add(RouteRecom(lottie="manual.json",typeName="MANUAL",typeDescript="완벽한 계획형인 당신! 처음부터 끝까지 세세하게 계획을 짜는 것을 도와드립니다"))

            routeRecomDialogAdapter.list = routeSelectList
            routeRecomDialogAdapter.notifyDataSetChanged()
        }

        var dialogView:View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_the_recomroute,null)
        var dialogRv = dialogView.findViewById<RecyclerView>(R.id.routeRecom_rv_typeSelect)
        dialogRv.apply {
            layoutManager = GridLayoutManager(requireContext(),1,LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = routeRecomDialogAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        var selectPosition = -1
        routeRecomDialogAdapter.setItemClickListener(object: RouteRecomDialogAdapter.ItemClickListener{
            override fun onClick(view:View, position: Int, type:String) {
                selectPosition = position
            }
        })

//        dialogRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                    val firstPos = (dialogRv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
//                    val secondPos = (dialogRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    val lastPos = (dialogRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//                    val selectPos = max(lastPos,max(firstPos,secondPos))
////                    val selectPos = (dialogRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    if(selectPos != -1){
//                        val viewItem =  (dialogRv.layoutManager as LinearLayoutManager).findViewByPosition(selectPos)
//                        viewItem?.run{
//                            val itemMargin = (dialogRv.measuredWidth - viewItem.measuredWidth) / 3
//                            dialogRv.smoothScrollBy(this.x.toInt()-itemMargin, 0)
//                        }
//                    }
//                }
//            }
//        })

        var dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialogView.findViewById<Button>(R.id.routeRecom_btn_cancle).setOnClickListener {
            dialog.cancel()
        }
        dialogView.findViewById<Button>(R.id.routeRecom_btn_ok).setOnClickListener {
            if(selectPosition == 0){
                //auto
            }
            if(selectPosition == 1){
                //semiauto
            }
            if(selectPosition == 2){
                //manual
                mainActivity.moveFragment(3,"planId",planId)
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