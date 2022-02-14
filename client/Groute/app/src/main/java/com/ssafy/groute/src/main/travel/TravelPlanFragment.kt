package com.ssafy.groute.src.main.travel

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.datepicker.*
import com.google.android.material.tabs.TabLayout
import com.kakao.kakaonavi.Destination.newBuilder
import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.Location.newBuilder
import com.kakao.kakaonavi.NaviOptions
import com.kakao.kakaonavi.options.CoordType
import com.kakao.kakaonavi.options.RpOption
import com.kakao.kakaonavi.options.VehicleType

import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Place
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
import net.daum.mf.map.api.MapPolyline
import net.daum.mf.map.api.MapView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.kakaonavi.Destination
import java.lang.Exception


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
    var markerArr = arrayListOf<MapPoint>()
    private lateinit var mapView:MapView
    private lateinit var routeRecomDialogAdapter:RouteRecomDialogAdapter
    private lateinit var travelPlanListRecyclerviewAdapter: TravelPlanListRecyclerviewAdapter
    private lateinit var findLocationAdapter: FindLocationAdapter
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
            planViewModel.getPlanById(planId, 2)
            homeViewModel.getAreaLists()
        }

        initPlaceListAdapter()
        findArea()
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
        binding.findLocationBtn.setOnClickListener {
            var routeId = planViewModel.routeList.value?.get(curPos)?.id
            if (routeId != null) {
                showFindLocation()
            }
        }
        binding.travelPlanBtnBestPriority.setOnClickListener {
            showBestPriorityDialog()
//            var routeId = planViewModel.routeList.value?.get(curPos)?.id
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun showBestPriorityDialog(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_best_priority,null)
        val dialog = BottomSheetDialog(requireContext())
        if(dialogView.parent != null){
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }
        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var placeInfo = arrayListOf<RouteDetail>()
        var places = arrayListOf<String>()
        var routeDetailList = planViewModel.routeList.value?.get(curPos)
        
        places.add("선택안함")
        if(routeDetailList!=null){
            for(i in 0..routeDetailList.routeDetailList.size-1){
                placeInfo.add(routeDetailList.routeDetailList[i])
                places.add(routeDetailList.routeDetailList[i].place.name)
            }
        }
        var startAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,places)
        var endAdapter =  ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,places)
        var startSpinner = dialogView.findViewById<Spinner>(R.id.startSpinner)
        var endSpinner = dialogView.findViewById<Spinner>(R.id.endSpinner)

        startSpinner.adapter = startAdapter
        endSpinner.adapter = endAdapter

        startSpinner.setSelection(0,false)
        endSpinner.setSelection(0,false)
        var startId = 0
        var endId = 0
        var routeId = 0
        startSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(startSpinner.selectedItemPosition > 0){
                    startId = placeInfo[startSpinner.selectedItemPosition-1].priority
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        endSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(endSpinner.selectedItemPosition > 0){
                    endId = placeInfo[endSpinner.selectedItemPosition-1].priority
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        dialog.show()
        dialogView.findViewById<AppCompatButton>(R.id.bestPriority_btn_cancle).setOnClickListener {
            dialog.dismiss()
        }
        dialogView.findViewById<AppCompatButton>(R.id.bestPriority_btn_ok).setOnClickListener {
            routeId = routeDetailList!!.id
            Log.d(TAG, "showBestPriorityDialog: ${endId},${startId},${routeId}")
            runBlocking {
                planViewModel.getBestPriority(endId, startId,routeId,curPos)
            }
            dialog.dismiss()
        }
    }
    fun showFindLocation(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_location_find, null)
        val dialog = Dialog(requireContext())
        if(dialogView.parent != null){
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }
        planViewModel.removeAllViaList()
        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var params = dialog.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = params

        val destinationInfo = arrayListOf<Place>()
        val destinations = arrayListOf<String>()
        destinations.add("선택안함")
        var routeDetail = planViewModel.routeList.value?.get(curPos)
        if (routeDetail != null) {
            for(i in 0..routeDetail.routeDetailList.size-1){
                destinationInfo.add(routeDetail.routeDetailList[i].place)
                destinations.add(routeDetail.routeDetailList[i].place.name)
            }
        }
        var spinnerAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,destinations)
        var viaAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,destinations)

        var destSpinner = dialogView.findViewById<Spinner>(R.id.destination_spinner)
        var viaSpinner = dialogView.findViewById<Spinner>(R.id.via_spinner)

        destSpinner.adapter = spinnerAdapter
        viaSpinner.adapter = viaAdapter

        var destLat = 0.0
        var destLng = 0.0

        destSpinner.setSelection(0,false)
        destSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(destSpinner.selectedItemPosition > 0){
                    Log.d(TAG, "onItemSelected: ${destinationInfo[destSpinner.selectedItemPosition-1]}")
                    destLat = destinationInfo[destSpinner.selectedItemPosition-1].lat.toDouble()
                    destLng = destinationInfo[destSpinner.selectedItemPosition-1].lng.toDouble()
                }
                
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        viaSpinner.setSelection(0,false)
        viaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(viaSpinner.selectedItemPosition > 0){
                    if(planViewModel.liveViaList.value?.size!! >= 3){
                        showCustomToast("더이상 추가하실 수 없습니다")
                    }else{
                        planViewModel.insertViaList(destinationInfo[viaSpinner.selectedItemPosition-1])
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        planViewModel.liveViaList.observe(viewLifecycleOwner,{
            Log.d(TAG, "showFindLocation: ${it}")
            findLocationAdapter = FindLocationAdapter()
            findLocationAdapter.list = it
            dialogView.findViewById<RecyclerView>(R.id.viaList_rv).apply {
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                adapter = findLocationAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

        dialog.show()
        dialogView.findViewById<AppCompatButton>(R.id.findLocation_btn_cancle).setOnClickListener {
            dialog.dismiss()
        }
        dialogView.findViewById<AppCompatButton>(R.id.findLocation_btn_ok).setOnClickListener {
            goNavi(destLat,destLng)
        }
    }
    fun goNavi(destLat:Double, destLng:Double){
        try {
            if (KakaoNaviService.isKakaoNaviInstalled(requireContext())) {

                val kakao: com.kakao.kakaonavi.Location =
                    Destination.newBuilder("destination", destLng, destLat).build()
                val stops = LinkedList<com.kakao.kakaonavi.Location>()
                planViewModel.liveViaList.observe(viewLifecycleOwner,{
                    for(i in 0..it.size-1){
                        val stop = com.kakao.kakaonavi.Location.newBuilder("출발",it[i].lng.toDouble(),it[i].lat.toDouble()).build()
                        stops.add(stop)
                    }
                })



                val params = KakaoNaviParams.newBuilder(kakao)
                    .setNaviOptions(
                        NaviOptions.newBuilder()
                            .setCoordType(CoordType.WGS84) // WGS84로 설정해야 경위도 좌표 사용 가능.
                            .setRpOption(RpOption.NO_AUTO)
                            .setStartAngle(200) //시작 앵글 크기 설정.
                            .setVehicleType(VehicleType.FIRST).build()
                    ).setViaList(stops).build() //길 안내 차종 타입 설정

                KakaoNaviService.navigate(requireContext(), params)

            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa")
                )
                Log.e(TAG, "showNaviKakao: 네비 설치 안됨")
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e("네비연동 에러", e.toString() + "")
        }
    }
    fun initMemo(){
        var routes = planViewModel.routeList.value!!
        if(routes[curPos].memo == null || routes[curPos].memo == ""){
            showCustomToast("메모가 없습니다.")
        }
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
                    planViewModel.getPlanById(planId, 2)
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }
    fun findArea(){
        var area = homeViewModel.areaList.value!!
        var plan = planViewModel.planList.value!!
        for(i in 0 until area.size){
            if(area[i].id == plan.areaId){
                initKakaoMap(area[i].lat.toDouble(), area[i].lng.toDouble())
            }
        }
    }
    fun initKakaoMap(lat:Double, lng:Double){
        mapView = MapView(requireContext())
        if(mapView.parent!=null){
            (mapView.parent as ViewGroup).removeView(mapView)
        }
        binding.travelplanMapview.addView(mapView)
        var mapPoint = MapPoint.mapPointWithGeoCoord(lat, lng)
        mapView.setMapCenterPoint(mapPoint,true)
        mapView.setZoomLevel(9, true)
    }

    fun addPing(day:Int){
        markerArr = arrayListOf()
        planViewModel.routeList.observe(viewLifecycleOwner, {
            var dayList = it[day].routeDetailList
//            Log.d(TAG, "addPing: ${dayList}")
            for(i in 0..dayList.size-1){
                var lat = dayList[i].place.lat.toDouble()
                var lng = dayList[i].place.lng.toDouble()
                var mapPoint = MapPoint.mapPointWithGeoCoord(lat,lng)
                markerArr.add(mapPoint)
            }
            setPing(markerArr)
            addPolyLine(markerArr)
        })

    }
    fun setPing(markerArr:ArrayList<MapPoint>){
        removePing()
        var res = ""
        var list = arrayListOf<MapPOIItem>()
        for(i in 0..markerArr.size-1){
            var marker = MapPOIItem()
            res = "number${i+1}"
            marker.itemName = (i+1).toString()
            marker.mapPoint = markerArr[i]
            marker.markerType = MapPOIItem.MarkerType.CustomImage
            var resources = requireContext().resources.getIdentifier("@drawable/"+res,"drawable",requireContext().packageName)
            marker.customImageResourceId = resources
            marker.isCustomImageAutoscale = false
            marker.setCustomImageAnchor(0.5f,1.0f)
            list.add(marker)
        }
        mapView.addPOIItems(list.toArray(arrayOfNulls(list.size)))

    }
    fun removePing(){
        mapView.removeAllPOIItems()
        mapView.removeAllPolylines()
    }
    fun addPolyLine(markerArr: ArrayList<MapPoint>){
        var polyLine = MapPolyline()
        polyLine.tag = 1000
        polyLine.lineColor = Color.parseColor("#2054B3")
        polyLine.addPoints(markerArr.toArray(arrayOfNulls(markerArr.size)))
        mapView.addPolyline(polyLine)
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
        addPing(0)
        binding.travelplanTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                travelPlanListRecyclerviewAdapter.filter.filter((tab?.position?.plus(1)).toString())
                curPos = tab!!.position
                removePing()
                addPing(tab!!.position)
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
            Log.d(TAG, "initPlaceListAdapter: 실행되니?")
            travelPlanListRecyclerviewAdapter = TravelPlanListRecyclerviewAdapter(requireContext(),it,planViewModel,viewLifecycleOwner,planId)

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
                                planViewModel.getPlanById(planId, 2)
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
                                planViewModel.getPlanById(planId, 2)
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