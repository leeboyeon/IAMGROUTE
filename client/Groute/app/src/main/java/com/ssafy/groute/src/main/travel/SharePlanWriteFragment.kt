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
import com.jakewharton.rxbinding3.widget.textChanges
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSharePlanWriteBinding
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

private const val TAG = "SharePlanWriteFragment"
class SharePlanWriteFragment : BaseFragment<FragmentSharePlanWriteBinding>(FragmentSharePlanWriteBinding::bind, R.layout.fragment_share_plan_write) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1

    private lateinit var editTextSubscription: Disposable

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
            planViewModel.getPlanById(planId, 2)
        }

        binding.planShareBtnComplete.setOnClickListener {
            updatePlan()
        }

        binding.sharePlanIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }

        initTiedListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePlan(){
        var userPlan = planViewModel.planList.value!!

        var plan = UserPlan(
            areaId = userPlan.areaId,
            description = binding.sharePlanTietContent.text.toString(),
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
        var routeDetailSize = 0
        for(i in 0 until planViewModel.routeList.value!!.size) {
            var routeDetailList = planViewModel.routeList.value!!.get(i).routeDetailList
            routeDetailSize += routeDetailList.size
        }
        if(routeDetailSize > 2){
            if(caldate > 0){
                if(textLengthChk(binding.sharePlanTietContent.text.toString()) == true){
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

    // init TextInputEditText Listener
    private fun initTiedListener() {
        editTextSubscription = binding.sharePlanTietContent
            .textChanges()
            .subscribe {
                textLengthChk(it.toString())
            }
    }

    private fun textLengthChk(str : String) : Boolean {
        if(str.trim().isEmpty()){
            binding.sharePlanTilContent.error = "Required Field"
            binding.sharePlanTietContent.requestFocus()
            return false
        } else if(str.length <= 30 || str.length >= 255) {
            binding.sharePlanTilContent.error = "30자 이상 255자 이하로 작성해주세요."
            binding.sharePlanTietContent.requestFocus()
            return false
        }
        else {
            binding.sharePlanTilContent.error = null
            return true
        }
    }


    override fun onDestroy() {
        editTextSubscription.dispose()
        mainActivity.hideBottomNav(false)
        super.onDestroy()
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