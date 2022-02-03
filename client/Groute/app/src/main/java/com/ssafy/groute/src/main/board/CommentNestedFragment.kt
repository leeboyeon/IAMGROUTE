package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentCommentNestedBinding
import com.ssafy.groute.src.main.MainActivity


class CommentNestedFragment : BaseFragment<FragmentCommentNestedBinding>(FragmentCommentNestedBinding::bind, R.layout.fragment_comment_nested){
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentNestedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}