package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteCreateBinding
import com.ssafy.groute.src.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import com.ssafy.groute.src.dto.Member
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.viewmodel.HomeViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

private const val TAG = "RouteCreateF_Groute"
class RouteCreateFragment : BaseFragment<FragmentRouteCreateBinding>(FragmentRouteCreateBinding::bind, R.layout.fragment_route_create) {
    private lateinit var mainActivity:MainActivity

    private lateinit var routeAreaAdapter:RouteAreaAdapter
    private var memberAdapter:MemberAdapter = MemberAdapter()
    private val homeViewModel: HomeViewModel by activityViewModels()
//    val area = mutableListOf<Area>()
    val member = mutableListOf<Member>()
    val ids = arrayListOf<String>()
    private var areaId = 1
    var startDate = ""
    var endDate = ""
    var totalDate = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            homeViewModel.getAreaLists()
        }
        homeViewModel.areaList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            routeAreaAdapter = RouteAreaAdapter(it)
            routeAreaAdapter.setItemClickListener(object : RouteAreaAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, id: Int) {
                    Log.d(TAG, "onClick: ${id}")
                    areaId = id
                }
            })
            routeAreaAdapter.notifyDataSetChanged()
            binding.routecreateRvArea.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter = routeAreaAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

        binding.rcBtnSave.setOnClickListener {
            insertUserPlan()
        }
        binding.rcBtnDatepicker.setOnClickListener {
            showDataRangePicker()
        }
        binding.rcIbtnAddMember.setOnClickListener {
            findMemberbyUserId()
        }
    }
    fun findMemberbyUserId(){
        val userId = binding.rcEtMemberId.text.toString()
        memberAdapter = MemberAdapter()
        val userInfo = UserService().getUserInfo(userId)

        if(userInfo!=null){
            userInfo.observe(viewLifecycleOwner, {
                member.apply {
                    if(it.img != null){
                        add(Member(img= it.img!!, name=it.nickname))
                    }else{
                        add(Member(img=R.drawable.user.toString(), name=it.nickname))
                    }
                    memberAdapter.list = member
                    memberAdapter.notifyDataSetChanged()
                }
                ids.apply{
                    add(ApplicationClass.sharedPreferencesUtil.getUser().id)
                    add(userId)
                }
                binding.routeRvMember.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = memberAdapter
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
            })
        }else{
            Toast.makeText(context,"해당하는 사용자가 없습니다.",Toast.LENGTH_SHORT).show()
            binding.rcEtMemberId.setText("")
        }

    }
    @SuppressLint("SetTextI18n")
    fun showDataRangePicker(){

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
//                .setSelection(
//                    Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                        MaterialDatePicker.todayInUtcMilliseconds()
//                    )
//                )
                .build()
        dateRangePicker.show(childFragmentManager, "date_picker")
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection?.first ?: 0
            val sDate = selection.first
            startDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time).toString()
            Log.d("start", startDate)

            calendar.timeInMillis = selection?.second ?: 0
            val eDate = selection.second
            endDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time).toString()
            Log.d("end", endDate)

            binding.rcBtnDatepicker.text = "${startDate} ~ ${endDate}"

            val msDiff = eDate - sDate
            var daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
            totalDate = daysDiff.toInt()+1

            Log.d(TAG, "showDataRangePicker: ${totalDate}")
        }
    }
    fun insertUserPlan(){
        val planId = 0
        val title = binding.rcEtTitle.text.toString()
        val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
        val userplan = UserPlan(
            areaId= areaId,
            description = "",
            endDate = endDate,
            isPublic = "F",
            startDate = startDate,
            title = title,
            totalDate = totalDate,
            userId = userId
        )
        val userIds = ids

        UserPlanService().insertUserPlan(planId, userplan, userIds, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(13)
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            RouteCreateFragment().apply {
                arguments = Bundle().apply {
                    putInt(key,value)
                }
            }
    }
}