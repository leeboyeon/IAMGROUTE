package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailReviewBinding
import com.ssafy.groute.src.main.MainActivity


class RouteDetailReviewFragment : BaseFragment<FragmentRouteDetailReviewBinding>(FragmentRouteDetailReviewBinding::bind, R.layout.fragment_route_detail_review) {
    private lateinit var mainActivity: MainActivity
    private lateinit var routeDetailReviewAdapter: RouteDetailReviewAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        routeDetailReviewAdapter = RouteDetailReviewAdapter()
        binding.routedetailReviewRvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = routeDetailReviewAdapter
        }

    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteDetailReviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}