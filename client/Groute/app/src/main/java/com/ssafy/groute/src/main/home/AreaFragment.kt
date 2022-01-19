package com.ssafy.groute.src.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentAreaBinding
import com.ssafy.groute.src.main.MainActivity


class AreaFragment : Fragment() {
    private lateinit var binding: FragmentAreaBinding
    private lateinit var mainActivity:MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAreaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()
    }
    fun initTab(){
        val pagerAdapter = AreaTabPagerAdapter(this)
        val tabList = arrayListOf("best","관광명소","테마/체험" ,"쇼핑", "음식점", "카페")

        pagerAdapter.addFragment()
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AreaFragment().apply {

            }
    }
}