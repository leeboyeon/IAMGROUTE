package com.ssafy.groute.src.main.travel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentAccountBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel

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
        val pagerAdapter = AccountPagerAdapter(this)

        pagerAdapter.addFragment(TypeByAccountFragment())
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