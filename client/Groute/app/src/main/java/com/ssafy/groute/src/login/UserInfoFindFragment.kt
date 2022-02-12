package com.ssafy.groute.src.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentUserInfoFindBinding


class UserInfoFindFragment : BaseFragment<FragmentUserInfoFindBinding>(FragmentUserInfoFindBinding::bind, R.layout.fragment_user_info_find) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserInfoFindFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}