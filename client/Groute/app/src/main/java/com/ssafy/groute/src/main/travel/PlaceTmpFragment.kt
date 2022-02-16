package com.ssafy.groute.src.main.travel

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.*
import com.google.android.material.tabs.TabLayout
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentPlaceTmpBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "PlaceTmpFragment_Groute"
class PlaceTmpFragment : BaseFragment<FragmentPlaceTmpBinding>(FragmentPlaceTmpBinding::bind, R.layout.fragment_place_tmp) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1

    private lateinit var placeShopAdapter: PlaceShopAdapter
    private val placeViewModel: PlaceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            placeViewModel.getPlaceLikeList(ApplicationClass.sharedPreferencesUtil.getUser().id)
        }

        initTabLayout()

        binding.tmpPlaceIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    private fun initTabLayout(){
        binding.emptyLayout.isVisible = false
        binding.placeTmpAddCartBtn.isVisible = false

        placeViewModel.placeLikeList.observe(viewLifecycleOwner, Observer {
            initAdapter(it)
        })
        binding.placetmpTabLayout.getTabAt(0)?.select()
        binding.placetmpTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0->{
                        binding.emptyLayout.isVisible = false
                        binding.placeTmpAddCartBtn.isVisible = false

                        placeViewModel.placeLikeList.observe(viewLifecycleOwner, Observer {
                            initAdapter(it)
                        })
                    }
                    1->{
                        planViewModel.livePlaceshopList.observe(viewLifecycleOwner, Observer {
                            if(it.isEmpty()){
                                binding.emptyLayout.isVisible = true
                                binding.placeTmpAddCartBtn.isVisible = false
                                initAdapter(it)
                                binding.goShopBtn.setOnClickListener {
                                    mainActivity.moveFragment(3, "planId", planId)
                                }
                            }else{
                                binding.emptyLayout.isVisible = false
                                binding.placeTmpAddCartBtn.isVisible = true
                                initAdapter(it)
                                binding.placeTmpAddCartBtn.setOnClickListener {
                                    mainActivity.moveFragment(3, "planId", planId)
                                }
                            }
                        })
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                placeViewModel.placeLikeList.observe(viewLifecycleOwner, Observer {
                    initAdapter(it)
                })
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun initAdapter(list:MutableList<Place>){
        placeShopAdapter = PlaceShopAdapter()
        placeShopAdapter.list = list
        binding.placeTmpRvList.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = placeShopAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        placeShopAdapter.setItemClickListener(object : PlaceShopAdapter.ItemClickListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(view: View, position: Int, id: Int) {
                showDatePicker(list[position].id)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker(placeId:Int){
        planViewModel.planList.observe(viewLifecycleOwner, Observer { it ->
            val formmater = SimpleDateFormat("yyyy-MM-dd")
            val sDate = formmater.parse(it.startDate)
            var eDate = formmater.parse(it.endDate)
            val cal = Calendar.getInstance()
            cal.time = eDate
            cal.add(Calendar.DATE, 1)
            eDate = cal.time
            val builder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("추가하실 날짜를 선택하세요")
                .setSelection(sDate.time)
            val cBuilderRange = CalendarConstraints.Builder()
            val dateMin = DateValidatorPointForward.from(sDate.time)
            Log.d(TAG, "showDatePicker: ${eDate}")
            val dateMax = DateValidatorPointBackward.before(eDate.time)
            
            val listValidator = ArrayList<CalendarConstraints.DateValidator>()
            listValidator.add(dateMin)
            listValidator.add(dateMax)
            val validators = CompositeDateValidator.allOf(listValidator)
            cBuilderRange.setValidator(validators)
            builder.setCalendarConstraints(cBuilderRange.build())
            val picker = builder.build()
            picker.show(mainActivity.supportFragmentManager,picker.toString())

            picker.addOnPositiveButtonClickListener { hey ->
                val calendar = Calendar.getInstance()
                calendar.time = Date(hey)
                planViewModel.routeList.observe(viewLifecycleOwner, Observer { it2 ->
                    var sdateTmp = LocalDate.parse(it.startDate, DateTimeFormatter.ISO_DATE)
                    var selectDay = ""

                    if(calendar.get(Calendar.MONTH)+1< 10){
                        var month = "0${calendar.get(Calendar.MONTH)+1}"
                        if(calendar.get(Calendar.DATE) < 10){
                            var day = "0${calendar.get(Calendar.DATE)}"
                            selectDay = "${calendar.get(Calendar.YEAR)}-$month-$day"
                        }else{
                            selectDay = "${calendar.get(Calendar.YEAR)}-$month-${calendar.get(
                                Calendar.DATE)}"
                        }
                    }else{
                        selectDay = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)+1}-${calendar.get(
                            Calendar.DATE)}"

                    }
                    for(i in 0..it.totalDate-1){
                        if(sdateTmp.plusDays(i.toLong()).toString().equals(selectDay)){
                            for(j in 0..it2.size-1){
                                if(it2[j].day == i+1){
                                    insertPlace(it2[j].day, placeId)
                                }
                            }
                        }
                    }
                })
            }
        })
    }

    private fun insertPlace(day:Int,placeId:Int){
        //placeId, priority,routeId
        planViewModel.routeList.observe(viewLifecycleOwner, Observer {
            var routeId = 0
            var priority = 0
            for(i in 0..it.size-1){
                if(it.get(i).day == day){
                    routeId = it.get(i).id
                    priority = it.get(i).routeDetailList.size+1
                }
            }

            val routeDatil = RouteDetail(
                placeId = placeId,
                priority = priority,
                routeId = routeId,
            )

            UserPlanService().insertPlaceToUserPlan(routeDatil, object : RetrofitCallback<Boolean> {
                override fun onError(t: Throwable) {
                    Log.d(TAG, "onError: ")
                }

                override fun onSuccess(code: Int, responseData: Boolean) {
                    planViewModel.removePlaceShopList(placeId)
                    showCustomToast("추가 되었습니다.")
                    Log.d(TAG, "onSuccess: $responseData")
                    binding.placetmpTabLayout.getTabAt(0)?.select()

                }

                override fun onFailure(code: Int) {
                    Log.d(TAG, "onFailure: ")
                }

            })
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String, value: Int) =
            PlaceTmpFragment().apply {
                arguments = Bundle().apply {
                    putInt(key,value)
                }
            }
    }
}