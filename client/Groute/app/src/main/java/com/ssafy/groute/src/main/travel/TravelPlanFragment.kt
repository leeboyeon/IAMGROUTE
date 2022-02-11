package com.ssafy.groute.src.main.travel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.datepicker.*
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.service.RouteDetailService
import com.ssafy.groute.src.service.RouteService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.HomeViewModel
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


private const val TAG = "TravelPlanFragment"
class TravelPlanFragment : BaseFragment<FragmentTravelPlanBinding>(FragmentTravelPlanBinding::bind, R.layout.fragment_travel_plan) {

    private lateinit var mainActivity: MainActivity

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
            planViewModel.getPlanById(planId, false)
            homeViewModel.getAreaLists()
        }

        initPlaceListAdapter()
        initKakaoMap()
        floatingButtonEvent()
        binding.travelplanBackIv.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
        binding.travelplanShareBtn.setOnClickListener {
            val share = binding.travelplanShareIv
            val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
            animator.addUpdateListener { animation ->
                share.progress = animation.animatedValue as Float
            }
            animator.start()
            mainActivity.moveFragment(18,"planId",planId)
        }
        binding.travelplanCalcBtn.setOnClickListener {
            val money = binding.travelplanCalcIv
            val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
            animator.addUpdateListener { animation ->
                money.progress = animation.animatedValue as Float
            }
            animator.start()
            mainActivity.moveFragment(19,"planId",planId)
        }

        binding.travelplanMemberBtn.setOnClickListener {
            val member = binding.travelplanMemberIv
            val animator = ValueAnimator.ofFloat(0f,1f).setDuration(1000)
            animator.addUpdateListener { animation ->
                member.progress = animation.animatedValue as Float
            }
            animator.start()
            if(planViewModel.planList.value!!.userId == ApplicationClass.sharedPreferencesUtil.getUser().id){
                mainActivity.moveFragment(17,"planId",planId)
            }else{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("일정에 대한 생성자만 공유하실 수 있습니다.")
                    .setNeutralButton("확인",null)
                builder.show()
            }
        }
        binding.travelPlanIbtnMemo.setOnClickListener {
            initMemo()
        }

    }

    fun initMemo(){
        var routes = planViewModel.routeList.value!!
        if(routes[curPos].memo != null){
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_show_memo,null)
            val dialog = Dialog(requireContext())
            if(dialogView.parent != null){
                (dialogView.parent as ViewGroup).removeView(dialogView)
            }
            dialog.setContentView(dialogView)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogView.findViewById<TextView>(R.id.dayMemo).text = routes[curPos].memo
            dialog.show()

            dialogView.findViewById<ImageButton>(R.id.memo_cancle).setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<AppCompatButton>(R.id.showmemo_btn_modify).setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
                insertMemo(0,2)
            }

            dialogView.findViewById<AppCompatButton>(R.id.showmemo_btn_delete).setOnClickListener {
                var route = Route(
                    day = routes[curPos].day,
                    id = routes[curPos].id,
                    isCustom = routes[curPos].isCustom,
                    memo = "",
                    name = routes[curPos].name,
                    routeDetailList = routes[curPos].routeDetailList,
                )
                updateMemo(route,3)
                dialog.dismiss()
            }
        }

    }
    fun updateMemo(route:Route,flag:Int){
        //flag 1 : insert/ 2:modify 3:delete
        RouteService().updateRoute(route, object:RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }
            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: ")
                if(flag == 1){
                    showCustomToast("추가되었습니다")
                }
                if(flag == 2){
                    showCustomToast("수정되었습니다")
                }
                if(flag == 3){
                    showCustomToast("삭제되었습니다.")
                }
                runBlocking {
                    planViewModel.getPlanById(planId, false)
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
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
        var marker = MapPOIItem()
        planViewModel.routeList.observe(viewLifecycleOwner, Observer {
            var dayByList = it[curPos].routeDetailList
            for(i in dayByList.indices){
                var lat = dayByList[i].place.lat
                var lng = dayByList[i].place.lng
                var mapPoint = MapPoint.mapPointWithGeoCoord(lat.toDouble(),lng.toDouble())
                marker.itemName = (i+1).toString()
                marker.mapPoint = mapPoint
                marker.markerType = MapPOIItem.MarkerType.YellowPin
            }
        })
        mapView.addPOIItem(marker)

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
            insertMemo(placeId,1)
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
                    var routes = planViewModel.routeList.value!!
                    val details = routes[curPos].routeDetailList

                    for(i in 0.. details.size-1){
                        if (details[i].placeId == placeId) {
                            Log.d(TAG, "onClick: findPlace")
                            Log.d(TAG, "onClick_MEMO: ${details[i].memo}")
                            memo = details[i].memo
                            break;
                        }
                    }

                    if(memo.equals("")|| memo.isEmpty() || memo.length == 0){
                        //메모가 없으면
                        insertMemo(placeId,1)
                    }
                    if(!memo.isEmpty()){
                        insertMemo(placeId, 2)
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
    fun insertMemo(placeId:Int , flag : Int){
        Log.d(TAG, "insertMemo: ${flag}")
        // flag 1 -> insert 2-> modify

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_insert_memo,null)
        if(dialogView.parent!=null){
            (dialogView.parent as ViewGroup).removeAllViews()
        }
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var routes = planViewModel.routeList.value!!
        var details = routes[curPos].routeDetailList!!
        if(flag == 2){
            if(placeId == 0){
                dialogView.findViewById<TextView>(R.id.memo_tv_content).text = routes[curPos].memo
            }
            if(placeId > 0){
                var memos = ""
                for(i in details.indices){
                    if (details[i].placeId == placeId) {
                        memos = details[i].memo
                    }
                }
                dialogView.findViewById<TextView>(R.id.memo_tv_content).text = memos.toString()
            }
        }

        dialog.show()

        dialogView.findViewById<Button>(R.id.memo_btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialogView.findViewById<AppCompatButton>(R.id.memo_btn_ok).setOnClickListener {
            if(flag == 1){
                val memo = dialogView.findViewById<TextView>(R.id.memo_tv_content).text.toString()

                if(placeId == 0){
                    var route = Route(
                        day = routes[curPos].day,
                        id = routes[curPos].id,
                        isCustom = routes[curPos].isCustom,
                        memo = memo,
                        name = routes[curPos].name,
                        routeDetailList = routes[curPos].routeDetailList,
                    )
                    updateMemo(route,1)
                    dialog.dismiss()
                }
                if(placeId > 0){
                    var routeDetail = RouteDetail()
                    for(i in details.indices){
                        if (details[i].placeId == placeId) {
                            routeDetail = RouteDetail(
                                id= details[i].id,
                                memo = memo,
                                priority = details[i].priority
                            )
                        }
                    }
                    RouteDetailService().updateRouteDetail(routeDetail, object:RetrofitCallback<Boolean> {
                        override fun onError(t: Throwable) {
                            Log.d(TAG, "onError: ")
                        }

                        override fun onSuccess(code: Int, responseData: Boolean) {
                            dialog.dismiss()
                            runBlocking {
                                planViewModel.getPlanById(planId, false)
                            }
                        }

                        override fun onFailure(code: Int) {
                            Log.d(TAG, "onFailure: ")
                        }

                    })
                }
            }
            if(flag == 2){
                val memo = dialogView.findViewById<TextView>(R.id.memo_tv_content).text.toString()
                if(placeId == 0){
                    var route = Route(
                        day = routes[curPos].day,
                        id = routes[curPos].id,
                        isCustom = routes[curPos].isCustom,
                        memo = memo,
                        name = routes[curPos].name,
                        routeDetailList = routes[curPos].routeDetailList,
                    )
                    updateMemo(route,2)
                    dialog.dismiss()
                }
                //place 별 메모수정
                if(placeId > 0){
                    var routeDetail = RouteDetail()
                    for(i in details.indices){
                        if (details[i].placeId == placeId) {
                            routeDetail = RouteDetail(
                                id= details[i].id,
                                memo = memo,
                                priority = details[i].priority
                            )
                        }
                    }
                    RouteDetailService().updateRouteDetail(routeDetail, object:RetrofitCallback<Boolean> {
                        override fun onError(t: Throwable) {
                            Log.d(TAG, "onError: ")
                        }

                        override fun onSuccess(code: Int, responseData: Boolean) {
                            dialog.dismiss()
                            runBlocking {
                                planViewModel.getPlanById(planId, false)
                            }
                        }

                        override fun onFailure(code: Int) {
                            Log.d(TAG, "onFailure: ")
                        }

                    })
                }
            }
        }

    }
    fun showRouteSelectDialog(){
        routeRecomDialogAdapter = RouteRecomDialogAdapter(requireContext())
        routeSelectList.apply {
            add(RouteRecom(lottie="oneday.json",typeName="장소 필터링",typeDescript="추가하신 장소를 포함한 일정을 추천해드립니다."))
            add(RouteRecom(lottie="oneday.json",typeName="장소 제외 필터링",typeDescript="추가하신 장소를 제외한 일정을 추천해드립니다."))
            add(RouteRecom(lottie="allday.json",typeName="전체일정 추천",typeDescript="모든 일정을 \n 추천받으시고 싶으신가요?"))

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
                //장소필터링
                mainActivity.moveFragment(16,"planId",planId,"flag",0)
                dialog.dismiss()
            }
            if(selectPosition == 1){
                //장소제외필터링
                mainActivity.moveFragment(16,"planId",planId,"flag",1)
                dialog.dismiss()
            }
            if(selectPosition == 2) {
                //일정추천
                var total = 0
                planViewModel.planList.observe(viewLifecycleOwner, Observer {
                    total = it.totalDate
                })
                mainActivity.moveFragment(16,"planId",planId,"flag",2)
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