package com.ssafy.groute.src.main.route

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteBinding
import com.ssafy.groute.databinding.FragmentRouteCreateBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.main.home.CategoryAdapter
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "RouteFragment_Groute"
class RouteFragment : BaseFragment<FragmentRouteBinding>(FragmentRouteBinding::bind, R.layout.fragment_route) {
//    lateinit var binding: FragmentRouteBinding
    private lateinit var mainActivity: MainActivity
    lateinit var pagerAdapter: RouteTabPageAdapter
    lateinit var routeAreaAdapter: RouteAreaAdapter
    private var categoryAdapter:CategoryAdapter = CategoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)

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
//        binding = FragmentRouteBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.routeCreateBtn.setOnClickListener {
            mainActivity.moveFragment(1)
        }


    }
    fun initAdapter(){
        getData()
        pagerAdapter = RouteTabPageAdapter(this)
        routeAreaAdapter = RouteAreaAdapter()

        val tabList = arrayListOf("당일치기", "1박 2일", "2박 3일", "3박 4일", "4박 5일")

        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())

        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabList.get(position)
        }.attach()



    }
    fun getData(){
//        AreaService().getAreas(AreaCallback())
    }

    inner class AreaCallback: RetrofitCallback<List<Area>> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: $t")
        }

        override fun onSuccess(code: Int, responseData: List<Area>) {
            Log.d(TAG, "onSuccess: ${responseData}")
            responseData.let {
                categoryAdapter = CategoryAdapter()
                categoryAdapter.list = responseData
                categoryAdapter.setItemClickListener(object : CategoryAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int, name: String) {
                    }
                })
            }
            binding.routeRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = categoryAdapter
            }
        }
        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }
}