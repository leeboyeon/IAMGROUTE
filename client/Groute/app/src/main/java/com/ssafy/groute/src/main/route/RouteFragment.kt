package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentRouteBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.Category


class RouteFragment : Fragment() {
    lateinit var binding: FragmentRouteBinding
    private lateinit var mainActivity: MainActivity
    lateinit var pagerAdapter: RouteTabPageAdapter
    lateinit var routeAreaAdapter: RouteAreaAdapter
    val catelists = mutableListOf<Category>()
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
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.routeCreateBtn.setOnClickListener {
            mainActivity.openFragment(1)
        }


    }
    fun initAdapter(){
        pagerAdapter = RouteTabPageAdapter(this)
        routeAreaAdapter = RouteAreaAdapter()

        val tabList = arrayListOf("당일치기", "1박 2일", "2박 3일", "3박 4일", "4박 5일")
        catelists.apply {
            add(Category(img = R.drawable.jeju, name="제주"))
            add(Category(img = R.drawable.busan, name="부산"))
            add(Category(img = R.drawable.kangwondo, name="강원"))
            add(Category(img = R.drawable.keungju, name="경주"))
            add(Category(img = R.drawable.chungbuk, name="충북"))
            add(Category(img = R.drawable.keongkido, name="경기"))
            add(Category(img = R.drawable.deagu, name="대구"))
            add(Category(img = R.drawable.yeosu, name="여수"))
            add(Category(img = R.drawable.jeonju, name="전주"))
            add(Category(img = R.drawable.incheon, name="인천"))

            routeAreaAdapter.list = catelists
            routeAreaAdapter.notifyDataSetChanged()
        }

        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())

        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        binding.routeRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = routeAreaAdapter
        }
    }

}