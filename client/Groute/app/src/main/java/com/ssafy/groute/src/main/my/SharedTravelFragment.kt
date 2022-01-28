package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSaveTravelBinding
import com.ssafy.groute.databinding.FragmentSharedTravelBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.BestRoute

class SharedTravelFragment : BaseFragment<FragmentSharedTravelBinding>(FragmentSharedTravelBinding::bind, R.layout.fragment_shared_travel) {
//    private lateinit var binding: FragmentSharedTravelBinding
    private lateinit var mainActivity: MainActivity
    private var sharedAdapter:SharedTravelAdapter = SharedTravelAdapter()

    val lists = mutableListOf<BestRoute>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentSharedTravelBinding.inflate(layoutInflater,container,false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }
    fun initAdapter(){
        sharedAdapter = SharedTravelAdapter()
        lists.apply {
            add(BestRoute(img=R.drawable.normalimg, title="[부산]지우와 함께하는 부산여행"))
            add(BestRoute(img=R.drawable.normalimg, title="[인천]경희님과 함께하는 월미도"))
            add(BestRoute(img=R.drawable.normalimg, title="[인천]경희님과 함께하는 월미도"))
            add(BestRoute(img=R.drawable.normalimg, title="[인천]경희님과 함께하는 월미도"))
            add(BestRoute(img=R.drawable.normalimg, title="[부산]지우와 함께하는 부산여행"))
            add(BestRoute(img=R.drawable.normalimg, title="[부산]지우와 함께하는 부산여행"))
            add(BestRoute(img=R.drawable.normalimg, title="[인천]경희님과 함께하는 월미도"))
            add(BestRoute(img=R.drawable.normalimg, title="[인천]경희님과 함께하는 월미도"))

            sharedAdapter.list = lists
            sharedAdapter.notifyDataSetChanged()
        }

        binding.myRvShared.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = sharedAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SharedTravelFragment().apply {

            }
    }
}