package com.ssafy.groute.src.main.travel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentAccountBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.CommonUtils
import kotlinx.coroutines.runBlocking

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::bind,R.layout.fragment_account) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    private var maxMember = -1

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            planViewModel.getPlanById(planId,2)
            planViewModel.getShareUserbyPlanId(planId)
        }
        val pagerAdapter = AccountPagerAdapter(this)

        pagerAdapter.addFragment(TypeByAccountFragment.newInstance("planId",planId))
        pagerAdapter.addFragment(DayByAccountFragment.newInstance("planId",planId))

        binding.accountvp.adapter = pagerAdapter

        binding.accountToggleDay.setOnClickListener {
            //날짜별 클릭시
            toggleClick(true)
            binding.accountvp.currentItem = 1
        }
        binding.accountToggleCate.setOnClickListener {
            toggleClick(false)
            binding.accountvp.currentItem = 0
        }
        binding.accountFabWrite.setOnClickListener {
            mainActivity.moveFragment(20,"planId",planId)
        }
        binding.accountBtnDiv.setOnClickListener {
            showDivDialog()
        }

        binding.accountIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }


    private fun showDivDialog(){
//        shareuser
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_div_account,null)
        val dialog = BottomSheetDialog(requireContext())
        if(dialogView.parent != null){
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val param = WindowManager.LayoutParams()
        param.width = WindowManager.LayoutParams.MATCH_PARENT
        param.height = WindowManager.LayoutParams.MATCH_PARENT
        val window = dialog.window
        window?.attributes = param
        dialog.setContentView(dialogView)
        dialogView.findViewById<TextView>(R.id.accountDia_tv_date).text = "${planViewModel.planList.value!!.startDate} ~ ${planViewModel.planList.value!!.endDate}"
        dialogView.findViewById<TextView>(R.id.accountDia_tv_memberCnt).text = "공유된 사용자는 총 ${planViewModel.shareUserList.value!!.size}명입니다"
        var total = 0
        for(i in 0..planViewModel.accountPriceList.value!!.size-1){
            total += planViewModel.accountPriceList.value!![i]
        }

        var member = planViewModel.shareUserList.value!!.size
        dialogView.findViewById<TextView>(R.id.accountDia_tv_count).text = member.toString()
        maxMember = member
        val div = total/member
        dialogView.findViewById<TextView>(R.id.accountDia_tv_total).text = "1인, ${CommonUtils.makeComma(div)}"

        dialogView.findViewById<ImageButton>(R.id.accountDia_ibtn_minus).setOnClickListener {
            if(member > 1){
                member -= 1
                dialogView.findViewById<TextView>(R.id.accountDia_tv_count).text = member.toString()
                val div = total/dialogView.findViewById<TextView>(R.id.accountDia_tv_count).text.toString().toInt()
                dialogView.findViewById<TextView>(R.id.accountDia_tv_total).text = "1인, ${CommonUtils.makeComma(div)}"
            }
        }
        dialogView.findViewById<ImageButton>(R.id.accountDia_ibtn_plus).setOnClickListener {
            if(member <= maxMember - 1) {
                member += 1
                dialogView.findViewById<TextView>(R.id.accountDia_tv_count).text = member.toString()
                val div = total / dialogView.findViewById<TextView>(R.id.accountDia_tv_count).text.toString().toInt()
                dialogView.findViewById<TextView>(R.id.accountDia_tv_total).text = "1인, ${CommonUtils.makeComma(div)}"
            }
        }
        dialog.show()
        dialogView.findViewById<ImageButton>(R.id.accountDia_ibtn_back).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun toggleClick(clicked:Boolean){
        if(clicked){
            //일자별 클릭시
            binding.accountToggleCate.setBackgroundResource(0)
            binding.accountToggleDay.setBackgroundResource(R.drawable.drawable_toggle_item_background)
        }else{
            binding.accountToggleCate.setBackgroundResource(R.drawable.drawable_toggle_item_background)
            binding.accountToggleDay.setBackgroundResource(0)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(key1: String, value1: Int) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                }
            }
    }
}