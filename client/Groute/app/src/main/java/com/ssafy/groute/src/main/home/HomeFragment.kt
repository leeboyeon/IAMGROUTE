package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentHomeBinding
import com.ssafy.groute.src.dto.Category
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.util.MainViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var categoryAdapter:CategoryAdapter = CategoryAdapter()
    private var bestrouteAdatper:BestRouteAdapter = BestRouteAdapter()


    private lateinit var mainActivity : MainActivity
    val catelists = mutableListOf<Category>()
    val bests = mutableListOf<BestRoute>()

    // 롤링 배너
    private lateinit var bannerViewPagerAdapter: BannerViewPagerAdapter
    private lateinit var mainViewModel: MainViewModel
    private var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(false)
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdatper()

        // 롤링 배너
        mainViewModel = ViewModelProvider(mainActivity).get(MainViewModel::class.java)
        val bannerList = arrayListOf<Int>(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        mainViewModel.setBannerItems(bannerList)
        initViewPager()
        subscribeObservers()
        autoScrollViewPage()
    }
    fun initAdatper(){
        getData()
//        categoryAdapter = CategoryAdapter()
        bestrouteAdatper = BestRouteAdapter()
        bests.apply {
            add(BestRoute(img=R.drawable.normalimg, title="[대구] 3박 4일 뭐하고 놀지?!"))
            add(BestRoute(img=R.drawable.normalimg, title="[대구] 3박 4일 뭐하고 놀지?!"))
            add(BestRoute(img=R.drawable.normalimg, title="[대구] 3박 4일 뭐하고 놀지?!"))

            bestrouteAdatper.list = bests
            bestrouteAdatper.notifyDataSetChanged()
        }

        bestrouteAdatper.setItemClickListener(object : BestRouteAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, name: String) {
                //event
            }
        })

        binding.homeRvBestRoute.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = bestrouteAdatper
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }


    }

    private fun initViewPager() {
        binding.viewPager2.apply {
            bannerViewPagerAdapter = BannerViewPagerAdapter()
            adapter = bannerViewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    isRunning = true

                }
            })
        }
    }

    private fun subscribeObservers() {
        mainViewModel.bannerItemList.observe(mainActivity, Observer{ bannerItemList ->
            bannerViewPagerAdapter.submitList(bannerItemList)

        })

        mainViewModel.currentPosition.observe(mainActivity, Observer { currentPosition ->
            binding.viewPager2.currentItem = currentPosition
        })
    }

    private fun autoScrollViewPage() {
        lifecycleScope.launch{
            whenResumed {
                while(isRunning) {
                    delay(3000)
                    mainViewModel.getcurrentPosition()?.let {
                        mainViewModel.setCurrentPosition((it.plus(1)) % 3)
                    }
                }
            }
        }
    }

    fun getData(){
        AreaService().getAreas(AreaCallback())
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }
    inner class AreaCallback: RetrofitCallback<List<Category>>{

        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: $t")
        }

        override fun onSuccess(code: Int, responseData: List<Category>) {
            Log.d(TAG, "onSuccess: ${responseData}")
            responseData.let{
                categoryAdapter = CategoryAdapter()
                categoryAdapter.list = responseData
                categoryAdapter.setItemClickListener(object : CategoryAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, name: String) {
                        mainActivity.moveFragment(3)
                    }
                })
            }
            binding.homeRvCategory.apply{
                layoutManager = GridLayoutManager(context, 5)
                adapter = categoryAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
}