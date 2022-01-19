package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentPlaceDetailBinding
import com.ssafy.groute.src.main.MainActivity

class PlaceDetailFragment : Fragment() {
    private lateinit var binding: FragmentPlaceDetailBinding
    private lateinit var mainActivity : MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val areaTabPagerAdapter = AreaTabPagerAdapter(this)
        val tabList = arrayListOf("Info","Review")

        areaTabPagerAdapter.addFragment(InfoFragment())
        areaTabPagerAdapter.addFragment(ReviewFragment())

        binding.pdVpLayout.adapter = areaTabPagerAdapter
        TabLayoutMediator(binding.pdTablayout, binding.pdVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaceDetailFragment().apply {

            }
    }
}