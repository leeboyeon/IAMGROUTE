package com.ssafy.groute.src.main.travel

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentDayByAccountBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class DayByAccountFragment : BaseFragment<FragmentDayByAccountBinding>(FragmentDayByAccountBinding::bind, R.layout.fragment_day_by_account) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var accountOutAdapter : AccountOutAdapter
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            planViewModel.getAccountList(planId)
        }

        initOutAdapter()
    }

    private fun initOutAdapter(){
        planViewModel.accountList.observe(viewLifecycleOwner, Observer {
            accountOutAdapter = AccountOutAdapter(requireContext())
            accountOutAdapter.list = it
            binding.accountRvList.layoutManager = LinearLayoutManager(requireContext())
            binding.accountRvList.adapter = accountOutAdapter
        })
    }

    companion object {
        fun newInstance(key: String, value: Int) =
            DayByAccountFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}