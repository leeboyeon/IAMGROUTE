package com.ssafy.groute.src.main.travel

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentAccountWriteBinding
import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.src.dto.AccountCategory
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.AccountService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "AccountWriteFragment"
class AccountWriteFragment : BaseFragment<FragmentAccountWriteBinding>(FragmentAccountWriteBinding::bind,R.layout.fragment_account_write) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    var position = -1
    var category = -1
    var type = ""
    lateinit var accountCategoryAdapter: AccountCategoryAdapter
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
            planViewModel.getCategory()
        }
        initItems()
        initAdapter()

        binding.accountWriteBtnSuccess.setOnClickListener {
            insertAccount()
        }
    }
    fun insertAccount(){
        var account = Account(
            categoryId = category,
            day = position,
            description = binding.accountWriteEtContent.text.toString(),
            spentMoney = binding.accountWriteEtMoney.text.toString().toInt(),
            type = type,
            userPlanId = planId,
        )
        AccountService().insertAccount(account, object: RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(19,"planId",planId)
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    fun initAdapter(){
        var list = planViewModel.accountCategoryList.value

        accountCategoryAdapter = AccountCategoryAdapter()
        if (list != null) {
            accountCategoryAdapter.list = list
        }
        binding.accountWriteRvCategory.apply {
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = accountCategoryAdapter
        }
        accountCategoryAdapter.setItemClickListener(object :AccountCategoryAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, id: Int) {
                category = id
            }

        })

    }
    fun initItems(){
        val userplan = planViewModel.planList.value!!
        Log.d(TAG, "initItems: ${userplan.totalDate}")
        for(i in 1..userplan.totalDate){
            var chip = Chip(requireContext())
            chip.text = "DAY ${i}"
            chip.isCheckable = true
            binding.daychipgroup.addView(chip)

            chip.setOnClickListener {
                position = i
            }
        }

        val cashs = arrayOf("신용카드","현금")
        var spinnerAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,cashs)
        binding.cashspinner.adapter = spinnerAdapter
        binding.cashspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0){
                    type= "신용카드"
                }else{
                    type = "현금"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
    companion object {

        @JvmStatic
        fun newInstance(key1: String, value1: Int) =
            AccountWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)

                }
            }
    }
}