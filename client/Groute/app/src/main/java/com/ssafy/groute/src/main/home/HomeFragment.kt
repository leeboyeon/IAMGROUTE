package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentHomeBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.HomeViewModel
import com.ssafy.groute.src.viewmodel.MainViewModel
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.*
import retrofit2.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "HomeFragment"
//class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val placeViewModel : PlaceViewModel by activityViewModels()
    private val planViewModel : PlanViewModel by activityViewModels()

    private lateinit var homeAreaAdapter:HomeAreaAdapter
    private lateinit var bestPlaceAdapter:BestPlaceAdapter
    private var categoryAdapter:CategoryAdapter = CategoryAdapter()
    private lateinit var bestrouteAdatper: BestRouteAdapter


    private lateinit var mainActivity : MainActivity
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

    override fun onResume() {
        super.onResume()
        mainActivity.hideMainProfileBar(false)
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
        runBlocking {
            homeViewModel.getAreaLists()
            placeViewModel.getPlaceBestList()
            planViewModel.getPlanBestList()
        }
        homeViewModel.areaList.observe(viewLifecycleOwner, Observer {
            homeAreaAdapter = HomeAreaAdapter(it)
            homeAreaAdapter.setItemClickListener(object : HomeAreaAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, name: String, id: Int) {
                    mainActivity.moveFragment(3)
                }
            })
            homeAreaAdapter.notifyDataSetChanged()


            binding.homeRvCategory.apply{
                layoutManager = GridLayoutManager(context, 5)
                adapter = homeAreaAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

        initBestPlaceAdapter()
        initBestPlanAdapter()

        // home 화면에 2번째 배너 - 롤링 배너
        mainViewModel = ViewModelProvider(mainActivity).get(MainViewModel::class.java)
        val bannerList = arrayListOf<Int>(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        mainViewModel.setBannerItems(bannerList)
        initViewPager()
        subscribeObservers()
        autoScrollViewPage()
    }

    /**
     * BEST Place 5개 출력하기
     * */
    fun initBestPlaceAdapter(){
        placeViewModel.placeBestList.observe(viewLifecycleOwner, Observer {
            bestPlaceAdapter = BestPlaceAdapter(it)
            bestPlaceAdapter.setItemClickListener(object : BestPlaceAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(4, "placeId", it.get(position).id)
                }
            })
            bestPlaceAdapter.notifyDataSetChanged()

            binding.homeRvBestPlace.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter = bestPlaceAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })

    }

    /**
     * BEST Plan 5개 출력하기
     * */
    private fun initBestPlanAdapter(){
        planViewModel.planBestList.observe(viewLifecycleOwner, Observer {
            bestrouteAdatper = BestRouteAdapter(it)
            //        bestrouteAdatper.setItemClickListener(object : BestRouteAdapter.ItemClickListener{
//            override fun onClick(view: View, position: Int, name: String) {
////
//            }
//
//          })

            bestrouteAdatper.notifyDataSetChanged()
            binding.homeRvBestRoute.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter = bestrouteAdatper
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })
    }

    /**
     * home 화면 2번째 롤링 배너 Recycler View Adapter
     */
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
            }
    }

//    suspend fun <T : Any> Call<T>.await(): T {
//        return suspendCancellableCoroutine { continuation ->
//            continuation.invokeOnCancellation {
//                cancel()
//            }
//            enqueue(object : Callback<T> {
//                override fun onResponse(call: Call<T>, response: Response<T>) {
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        if (body == null) {
//                            val invocation = call.request().tag(Invocation::class.java)!!
//                            val method = invocation.method()
//                            val e = KotlinNullPointerException("Response from " +
//                                    method.declaringClass.name +
//                                    '.' +
//                                    method.name +
//                                    " was null but response body type was declared as non-null")
//                            continuation.resumeWithException(e)
//                        } else {
//                            continuation.resume(body)
//                        }
//                    } else {
//                        continuation.resumeWithException(HttpException(response))
//                    }
//                }
//
//                override fun onFailure(call: Call<T>, t: Throwable) {
//                    continuation.resumeWithException(t)
//                }
//            })
//        }
//    }

}