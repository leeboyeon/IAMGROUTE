package com.ssafy.groute.src.main.my

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentNotificationBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.board.BoardAdapter
import com.ssafy.groute.src.service.NotificationService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.viewmodel.NotificationViewModel
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import kotlinx.coroutines.runBlocking


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "NotificationF_Groute"
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::bind, R.layout.fragment_notification) {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mainActivity: MainActivity
    private val notiViewModel : NotificationViewModel by activityViewModels()
    private lateinit var notiAdapter : NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        notiViewModel.notificationList.observe(viewLifecycleOwner, {
            binding.notiRvNotiList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            notiAdapter = NotificationAdapter(it, viewLifecycleOwner, notiViewModel)

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



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}