package com.ssafy.groute.src.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.nhn.android.idp.common.connection.CommonConnection.cancel
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentHomeBinding
import com.ssafy.groute.src.api.AreaApi
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.AreaService
import com.ssafy.groute.util.BoardViewModel
import com.ssafy.groute.util.MainViewModel
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import kotlinx.coroutines.*
import retrofit2.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "HomeFragment"
//class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var homeAreaAdapter:HomeAreaAdapter

    private var categoryAdapter:CategoryAdapter = CategoryAdapter()
    private var bestrouteAdatper:BestRouteAdapter = BestRouteAdapter()


    private lateinit var mainActivity : MainActivity
    val catelists = mutableListOf<Area>()
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
        }

        homeViewModel.areaList.observe(viewLifecycleOwner, Observer {

//            homeAreaAdapter = HomeAreaAdapter(HomeAreaAdapter.OnClickListener {
//                homeViewModel.displayAreaDetails(it.id)
//            }, it)

            homeAreaAdapter = HomeAreaAdapter(it)
            homeAreaAdapter.setItemClickListener(object : HomeAreaAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, name: String) {
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


        initAdapter()

        // home 화면에 2번째 배너 - 롤링 배너
        mainViewModel = ViewModelProvider(mainActivity).get(MainViewModel::class.java)
        val bannerList = arrayListOf<Int>(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        mainViewModel.setBannerItems(bannerList)
        initViewPager()
        subscribeObservers()
        autoScrollViewPage()
    }

    /**
     * Area List
     * home 화면 첫번째 배너에 있는 지역 리스트
     */
//    private fun areaListInit(responseData: MutableList<Area>) {
//        homeAreaAdapter = HomeAreaAdapter(responseData)
////        Log.d(TAG, "areaListInit: ${homeViewModel.areaList}")
//        Log.d(TAG, "areaListInit: ${responseData}")
//
////        homeAreaAdapter.setItemClickListener(object : CategoryAdapter.ItemClickListener{
////            override fun onClick(view: View, position: Int, name: String) {
////                mainActivity.moveFragment(3)
////            }
////        })
//
//        binding.homeRvCategory.apply{
//            layoutManager = GridLayoutManager(context, 5)
//            adapter = homeAreaAdapter
//            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        }
//    }

    /**
     * home 화면에 3번째 배너
     */
    fun initAdapter(){
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




//    inner class AreaCallback: RetrofitCallback<List<Area>> {
//        override fun onError(t: Throwable) {
//            Log.d(TAG, "onError: $t")
//        }
//
//        override fun onSuccess(code: Int, responseData: List<Area>) {
//            Log.d(TAG, "onSuccess: ${responseData}")
//            areaListInit(responseData as MutableList<Area>)
////            homeViewModel.setAreaList(responseData)
////            Log.d(TAG, "onSuccess1222: ${homeViewModel.areaList}")
//
//        }
//
//        override fun onFailure(code: Int) {
//            Log.d(TAG, "onFailure: ")
//        }
//    }





    suspend fun <T : Any> Call<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            val invocation = call.request().tag(Invocation::class.java)!!
                            val method = invocation.method()
                            val e = KotlinNullPointerException("Response from " +
                                    method.declaringClass.name +
                                    '.' +
                                    method.name +
                                    " was null but response body type was declared as non-null")
                            continuation.resumeWithException(e)
                        } else {
                            continuation.resume(body)
                        }
                    } else {
                        continuation.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
//    inner class Logic {
//        suspend fun doWork(): Any {
//
//            return suspendCancellableCoroutine { cont ->
//                cont.invokeOnCancellation {
//                    cancel()
//                }
//                val areaRequest: Call<List<Area>> = RetrofitUtil.areaService.listArea()
//                areaRequest.enqueue(object : Callback<List<Area>> {
//                    override fun onResponse(
//                        call: Call<List<Area>>,
//                        response: Response<List<Area>>
//                    ) {
//                        val res = response.body()
//                        if (response.code() == 200) {
//                            if (res != null) {
//                                cont.resume(res)
////                                callback.onSuccess(response.code(), res)
//                                Log.d("Home", "onResponse: $res")
////                        responseData = res
//                            }
//                        } else {
//                            cont.resumeWithException(HttpException(response))
////                            callback.onFailure(response.code())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<Area>>, t: Throwable) {
//                        cont.resumeWithException(t)
////                        callback.onError(t)
//                    }
//                })
//            }
//        }
//    }
}