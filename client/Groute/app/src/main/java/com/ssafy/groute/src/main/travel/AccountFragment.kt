package com.ssafy.groute.src.main.travel

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentAccountBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "AccountFragment"
class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::bind,R.layout.fragment_account) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            planViewModel.getPlanById(planId,false)
            planViewModel.getShareUserbyPlanId(planId)
        }
        val pagerAdapter = AccountPagerAdapter(this)

        pagerAdapter.addFragment(TypeByAccountFragment.newInstance("planId",planId))
        pagerAdapter.addFragment(DayByAccountFragment.newInstance("planId",planId))

        binding.accountvp.adapter = pagerAdapter

        binding.accountToggleDay.setOnClickListener {
            //날짜별 클릭시
            ToggleClick(true)
            binding.accountvp.currentItem = 1
        }
        binding.accountToggleCate.setOnClickListener {
            ToggleClick(false)
            binding.accountvp.currentItem = 0
        }
        binding.accountFabWrite.setOnClickListener {
            mainActivity.moveFragment(20,"planId",planId)
        }
        binding.accountBtnDiv.setOnClickListener {
            showDivDialog()
        }
    }
    @SuppressLint("SetTextI18n")
    fun showDivDialog(){
//        shareuser
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_div_account,null)

        val dialog = BottomSheetDialog(requireContext())

        if(dialogView.parent != null){
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var param = WindowManager.LayoutParams()
        param.width = WindowManager.LayoutParams.MATCH_PARENT
        param.height = WindowManager.LayoutParams.MATCH_PARENT
        var window = dialog.window
        window?.attributes = param
        dialog.setContentView(dialogView)
        dialogView.findViewById<TextView>(R.id.accountDia_tv_date).text = "${planViewModel.planList.value!!.startDate} ~ ${planViewModel.planList.value!!.endDate}"
        dialogView.findViewById<TextView>(R.id.accountDia_tv_memberCnt).text = "공유된 사용자는 총 ${planViewModel.shareUserList.value!!.size}명입니다"
        var member = planViewModel.shareUserList.value!!.size

        dialogView.findViewById<TextView>(R.id.accountDia_tv_count).text = member.toString()

        dialogView.findViewById<ImageButton>(R.id.accountDia_ibtn_minus).setOnClickListener {
            if(member > 0){
                member--
            }
        }
        dialogView.findViewById<ImageButton>(R.id.accountDia_ibtn_plus).setOnClickListener {
            member++
        }
        dialog.show()
        dialogView.findViewById<ImageButton>(R.id.accountDia_ibtn_back).setOnClickListener {
            dialog.dismiss()
        }
    }
    fun ToggleClick(clicked:Boolean){
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