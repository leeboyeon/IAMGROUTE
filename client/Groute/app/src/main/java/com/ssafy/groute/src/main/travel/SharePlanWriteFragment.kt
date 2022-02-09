package com.ssafy.groute.src.main.travel

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSharePlanWriteBinding
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

private const val TAG = "SharePlanWriteFragment"
class SharePlanWriteFragment : BaseFragment<FragmentSharePlanWriteBinding>(FragmentSharePlanWriteBinding::bind, R.layout.fragment_share_plan_write) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1

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
        Log.d(TAG, "onAttach: ${planId}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.plan = planViewModel
        runBlocking {
            planViewModel.getPlanById(planId)
        }

        binding.planShareBtnComplete.setOnClickListener {
            updatePlan()
        }
        binding.sharePlanIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePlan(){
        var userPlan = planViewModel.planList.value!!

        var plan = UserPlan(
            areaId = userPlan.areaId,
            description = binding.planShareEtContent.text.toString(),
            endDate = userPlan.endDate,
            heartCnt = 0,
            id = userPlan.id,
            isPublic = "T",
            rate = userPlan.rate,
            reviewCnt = userPlan.reviewCnt,
            startDate = userPlan.startDate,
            themeIdList = userPlan.themeIdList,
            title = userPlan.title,
            totalDate = userPlan.totalDate,
            userId = userPlan.userId,
        )
        val now = Calendar.getInstance()
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        var eDate = formatter.parse(userPlan.endDate)
        val caldate = (now.time.time - eDate.time) / (60 * 60 * 24 * 1000)
        if(planViewModel.routeDetailList.value!!.size > 2){
            if(caldate > 0){
                if(binding.planShareEtContent.text.toString().length > 30){
                    UserPlanService().updateUserPlan(plan, object: RetrofitCallback<Boolean> {
                        override fun onError(t: Throwable) {
                            Log.d(TAG, "onError: ")
                        }

                        override fun onSuccess(code: Int, responseData: Boolean) {
                            Log.d(TAG, "onSuccess: ")
                            mainActivity.moveFragment(12,"planId",planId)
                        }

                        override fun onFailure(code: Int) {
                            Log.d(TAG, "onFailure: ")
                        }

                    })
                }
            }else{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("아직 여행을 다녀오시지 않으셨습니다")
                    .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                        mainActivity.moveFragment(7,"planId",planId)
                    })

                builder.show()
            }

        }else{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("공유하실 일정에 대한 장소가 충분하지 않습니다.")
                .setPositiveButton("확인", DialogInterface.OnClickListener{ dialog, id ->
                    mainActivity.moveFragment(7,"planId",planId)
                })

            builder.show()
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(key1: String, value1: Int) =
            SharePlanWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1,value1)
                }
            }
    }
}