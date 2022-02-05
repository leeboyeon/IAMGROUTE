package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteBinding
import com.ssafy.groute.databinding.FragmentRouteListBinding
import com.ssafy.groute.src.main.MainActivity


class RouteListFragment : BaseFragment<FragmentRouteListBinding>(FragmentRouteListBinding::bind, R.layout.fragment_route_list) {
//    lateinit var binding: FragmentRouteListBinding
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter

    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentRouteListBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val themeList = arrayListOf("#힐링", "#로맨틱", "#맛집탐방", "#관광", "#SNS", "#액티비티")
        ThemeAdapter = RouteThemeRecyclerviewAdapter(themeList)
        binding.routeThemeRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ThemeAdapter
        }
        
        RouteListAdapter = RouteListRecyclerviewAdapter()
        binding.routeListRv.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RouteListAdapter
        }

        RouteListAdapter.setItemClickListener(object : RouteListRecyclerviewAdapter.ItemClickListener {
            override fun onClick(position: Int) {
                mainActivity.moveFragment(12, "planId", 39)
            }

        })
    }

}