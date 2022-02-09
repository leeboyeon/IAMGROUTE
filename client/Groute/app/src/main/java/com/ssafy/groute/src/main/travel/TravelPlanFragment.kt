package com.ssafy.groute.src.main.travel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.datepicker.*
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.databinding.ActivityCommentNestedBinding.bind
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.service.RouteDetailService
import com.ssafy.groute.src.service.RouteService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.HomeViewModel
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.selects.select
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.time.days


private const val TAG = "TravelPlanFragment"
class TravelPlanFragment : BaseFragment<FragmentTravelPlanBinding>(FragmentTravelPlanBinding::bind, R.layout.fragment_travel_plan) {
// class TravelPlanFragment: Fragment(){

//    private lateinit var binding:FragmentTravelPlanBinding
    private lateinit var mainActivity: MainActivity
//    lateinit var con : ViewGroup
    lateinit var addButton: FloatingActionButton
    lateinit var memoAddButton: FloatingActionButton
    lateinit var routeRecomButton: FloatingActionButton
    lateinit var placeAddButton: FloatingActionButton

    private val planViewModel:PlanViewModel by activityViewModels()
    private val placeViewModel:PlaceViewModel by activityViewModels()
    private val homeViewModel:HomeViewModel by activityViewModels()

    var isFabOpen: Boolean = false
    private var planId = -1
    private var placeId = -1
    val routeSelectList = arrayListOf<RouteRecom>()
    private var curPos = 0

    private lateinit var routeRecomDialogAdapter:RouteRecomDialogAdapter
    private lateinit var travelPlanListRecyclerviewAdapter: TravelPlanListRecyclerviewAdapter
    private lateinit var memoAdapter: MemoAdapter

    private var AreaLat = 0.0
    private var AreaLng = 0.0
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getPlanById(planId)
            homeViewModel.getAreaLists()
        }

        initPlaceListAdapter()
//        initKakaoMap()
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
            mainActivity.moveFragment(17,"planId",planId)
        }
        binding.travelPlanIbtnMemo.setOnClickListener {
            initMemo()
        }

    }
    fun initMemo(){
            planViewModel.routeList.observe(viewLifecycleOwner, {
                if(it[curPos].memo != null){
                    val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_show_memo,null)
                    val dialog = Dialog(requireContext())
                    if(dialogView.parent != null){
                        (dialogView.parent as ViewGroup).removeView(dialogView)
                    }
                    dialog.setContentView(dialogView)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    dialogView.findViewById<ImageButton>(R.id.memo_cancle).setOnClickListener {
                        dialog.dismiss()
                    }
                    Log.d(TAG, "initMemo: day")
                    dialogView.findViewById<TextView>(R.id.dayMemo).text = it[curPos].memo
                    dialog.show()
                }else {
                    showCustomToast("메모가 없습니다.")
                }
            })



    }
    fun initKakaoMap(){
        var mapView = MapView(requireContext())
        binding.travelplanMapview.addView(mapView)
        Log.d(TAG, "findLatLng: ")
        homeViewModel.areaList.observe(viewLifecycleOwner, Observer { it1 ->
            planViewModel.planList.observe(viewLifecycleOwner, Observer { it2 ->
                for(i in 0 until it1.size){
                    if(it1[i].id == it2.areaId){
                        AreaLat = it1[i].lat.toDouble()
                        AreaLng = it1[i].lng.toDouble()
                        var mapPoint = MapPoint.mapPointWithGeoCoord(AreaLat, AreaLng)
                        mapView.setMapCenterPoint(mapPoint,true)
                        mapView.setZoomLevel(9, true)
                        Log.d(TAG, "findLatLng: ${AreaLat} || ${AreaLng}")
                    }
                }
            })
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
            var placeId = 0
            insertMemo(placeId)
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
        binding.travelplanTabLayout.removeAllTabs()
        for(i in 0 until planViewModel.routeList.value!!.size){
            binding.travelplanTabLayout.addTab(binding.travelplanTabLayout.newTab().setText(planViewModel.routeList.value!!.get(i).name))
        }

        travelPlanListRecyclerviewAdapter.filter.filter("1")
        binding.travelplanTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                travelPlanListRecyclerviewAdapter.filter.filter((tab?.position?.plus(1)).toString())
                curPos = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    fun initPlaceListAdapter(){
        planViewModel.routeList.observe(viewLifecycleOwner, Observer {
            travelPlanListRecyclerviewAdapter = TravelPlanListRecyclerviewAdapter(requireContext(),it)
            initTabLayout()
            binding.travelplanListRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = travelPlanListRecyclerviewAdapter
            }
            travelPlanListRecyclerviewAdapter.setMemoClickListener(object:TravelPlanListRecyclerviewAdapter.MemoClickListener{
                override fun onClick(view: View, position: Int, placeId: Int) {
                    Log.d(TAG, "onClick: memo button Click")

                    var memo = ""
                    planViewModel.routeList.observe(viewLifecycleOwner, {
                        val details = it[curPos].routeDetailList

                        Log.d(TAG, "onClick: ${details}")
                        for(i in 0.. details.size-1){
                            Log.d(TAG, "onClick: ${placeId} ||${details[i].placeId}")
                            if (details[i].placeId == placeId) {
                                Log.d(TAG, "onClick: findPlace")
                                Log.d(TAG, "onClick_MEMO: ${details[i].memo}")
                                memo = details[i].memo
                                break;
                            }
                        }
                    })

                    if(memo.equals("")|| memo.isEmpty() || memo.length == 0){
                        //메모가 없으면
                        insertMemo(placeId)
                    }

                }
            })



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
    fun insertMemo(placeId:Int){
        Log.d(TAG, "insertMemo: ${placeId}")
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_insert_memo,null)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogView.findViewById<Button>(R.id.memo_btn_cancle).setOnClickListener {
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.memo_btn_insert).setOnClickListener {
            //추가
            val memo = dialogView.findViewById<TextView>(R.id.memo_tv_content).text.toString()
//            RouteService().updateRoute()
            if(placeId == 0){
                //day별 메모추가
                planViewModel.routeList.observe(viewLifecycleOwner, {
                    var route = Route(
                        day = it[curPos].day,
                        id = it[curPos].id,
                        isCustom = it[curPos].isCustom,
                        memo = memo,
                        name = it[curPos].name,
                        routeDetailList = it[curPos].routeDetailList,
                    )
                    RouteService().updateRoute(route, object:RetrofitCallback<Boolean> {
                        override fun onError(t: Throwable) {
                            Log.d(TAG, "onError: ")
                        }

                        override fun onSuccess(code: Int, responseData: Boolean) {
                            showCustomToast("메모가 추가되었습니다.")
                            dialog.dismiss()
                            runBlocking {
                                planViewModel.getPlanById(planId)
                            }
                        }

                        override fun onFailure(code: Int) {
                            Log.d(TAG, "onFailure: ")
                        }

                    })
                })
            }
            if(placeId > 0){
                Log.d(TAG, "insertMemo: ${placeId}")
                var routeDetail = RouteDetail()
                planViewModel.routeList.observe(viewLifecycleOwner, {
                    val details = it[curPos].routeDetailList
                    Log.d(TAG, "onClick: ${details}")
                    for(i in 0.. details.size-1){
                        Log.d(TAG, "onClick: ${placeId} ||${details[i].placeId}")
                        if (details[i].placeId == placeId) {
                            routeDetail = RouteDetail(
                                id= details[i].id,
                                memo = memo,
                                priority = details[i].priority
                            )
                        }
                    }
                })
                RouteDetailService().updateRouteDetail(routeDetail, object:RetrofitCallback<Boolean> {
                    override fun onError(t: Throwable) {
                        Log.d(TAG, "onError: ")
                    }

                    override fun onSuccess(code: Int, responseData: Boolean) {
                        dialog.dismiss()
                        runBlocking {
                            planViewModel.getPlanById(planId)
                        }
                    }

                    override fun onFailure(code: Int) {
                        Log.d(TAG, "onFailure: ")
                    }

                })
            }
        }
        dialog.show()
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
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.routeRecom_btn_ok).setOnClickListener {
            if(selectPosition == 0){
                //당일
                mainActivity.moveFragment(16,"days",1)
                dialog.dismiss()
            }
            if(selectPosition == 1){
                //전체
                    var total = 0
                     planViewModel.planList.observe(viewLifecycleOwner, Observer {
                        total = it.totalDate
                    })
                mainActivity.moveFragment(16,"days",total)
                dialog.dismiss()
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