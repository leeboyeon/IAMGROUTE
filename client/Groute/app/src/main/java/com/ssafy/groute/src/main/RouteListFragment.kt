package com.ssafy.groute.src.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.databinding.FragmentRouteListBinding


class RouteListFragment : Fragment() {
    lateinit var binding: FragmentRouteListBinding
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRouteListBinding.inflate(inflater, container, false)
        return binding.root
    }

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
    }

}