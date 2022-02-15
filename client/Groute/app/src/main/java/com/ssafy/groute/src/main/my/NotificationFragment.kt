package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentNotificationBinding
import com.ssafy.groute.src.dto.Notification
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.NotificationService
import com.ssafy.groute.src.viewmodel.NotificationViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "NotificationF_Groute"
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::bind, R.layout.fragment_notification) {
    private lateinit var mainActivity: MainActivity
    private val notiViewModel : NotificationViewModel by activityViewModels()
    private lateinit var notiAdapter : NotificationAdapter
    var curPos = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            notiViewModel.getNotificationList(ApplicationClass.sharedPreferencesUtil.getUser().id)
        }

        binding.notiViewModel = notiViewModel
        initSpinner()
        initAdapter(0)
        binding.notiBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    private fun initAdapter(type:Int){
        notiViewModel.notificationList.observe(viewLifecycleOwner, {
            Log.d(TAG, "initAdapter: ${it}")
            binding.notiRvNotiList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            if(type == 1){
                //event
                val eventList = arrayListOf<Notification>()
                for(i in 0 until it.size){
                    if(it[i].category.equals("Event")){
                        eventList.add(it[i])
                    }
                }
                notiAdapter = NotificationAdapter(eventList)
            }
            if(type == 2){
                //user
                val userList = arrayListOf<Notification>()
                for(i in 0 until it.size){
                    if(it[i].category.equals("User")){
                        userList.add(it[i])
                    }
                }
                notiAdapter = NotificationAdapter(userList)
            }
            if(type == 0){
                notiAdapter = NotificationAdapter(it)
            }
            notiAdapter.setHasStableIds(true)
            binding.notiRvNotiList.adapter = notiAdapter

            notiAdapter.setItemClickListener(object: NotificationAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, id: Int) {
                    NotificationService().deleteNotification(id, NotiDeleteCallback())
                }
            })
        })
    }

    inner class NotiDeleteCallback() : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){
                showCustomToast("삭제되었습니다")
                runBlocking {
                    notiViewModel.getNotificationList(ApplicationClass.sharedPreferencesUtil.getUser().id)
                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }

    private fun initSpinner(){
        val spinnerArray = arrayListOf<String>()

        spinnerArray.apply {
            add("전체")
            add("이벤트")
            add("개인")
        }

        val spinner = binding.notiSpinnerCategory
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, spinnerArray)
        spinner.adapter = spinnerAdapter

        spinner.setSelection(0,false)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //0:전체 1:event 2:uer
                curPos = spinner.selectedItemPosition
                initAdapter(curPos)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}