package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
<<<<<<< HEAD
=======
import com.ssafy.groute.databinding.FragmentMyBinding
>>>>>>> feature/MyFragment_Frontend
import com.ssafy.groute.src.main.MainActivity


class MyFragment : Fragment() {
<<<<<<< HEAD
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)

=======
    private lateinit var binding: FragmentMyBinding
    private lateinit var mainActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
>>>>>>> feature/MyFragment_Frontend
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater,container,false)
        return binding.root
    }

<<<<<<< HEAD
=======
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = MyTravelTabPageAdapter(this)
        val tabList = arrayListOf("My Travel", "Shared Travel", "Save Travel")

        pagerAdapter.addFragment(MyTravelFragment())
        pagerAdapter.addFragment(SharedTravelFragment())
        pagerAdapter.addFragment(SaveTravelFragment())

        binding.myVpLayout.adapter = pagerAdapter
        TabLayoutMediator(binding.mypagetablayout, binding.myVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyFragment().apply {

            }
    }
>>>>>>> feature/MyFragment_Frontend
}