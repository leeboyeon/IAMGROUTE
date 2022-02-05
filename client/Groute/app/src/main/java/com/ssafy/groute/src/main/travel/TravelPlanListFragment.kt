package com.ssafy.groute.src.main.travel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.databinding.FragmentTravelPlanListBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.my.MyTravel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "TravelPlanListFragment"
class TravelPlanListFragment : BaseFragment<FragmentTravelPlanListBinding>(FragmentTravelPlanListBinding::bind, R.layout.fragment_travel_plan_list) {
//    lateinit var binding: FragmentTravelPlanListBinding
    private lateinit var mainActivity: MainActivity
    private val planViewModel :PlanViewModel by activityViewModels()
    private var day = -1
    private var planId = -1

    lateinit var travelPlanListRecyclerviewAdapter: TravelPlanListRecyclerviewAdapter
    val list = arrayListOf<TravelPlan>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            day = it.getInt("day",-1)
            planId = it.getInt("planId",-1)
        }
        Log.d(TAG, "onAttach: ${day}")
        Log.d(TAG, "onAttach: ${planId}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getRouteDetailbyDay(day)
        }
        initAdapter()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initAdapter(){
        planViewModel.routeDetailList.observe(viewLifecycleOwner, Observer {

            travelPlanListRecyclerviewAdapter = TravelPlanListRecyclerviewAdapter(requireContext())
            travelPlanListRecyclerviewAdapter.list = it
            binding.travelplanListRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = travelPlanListRecyclerviewAdapter
            }

            val travelPlanListRvHelperCallback = TravelPlanListRvHelperCallback(travelPlanListRecyclerviewAdapter).apply {
                setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
            }

            ItemTouchHelper(travelPlanListRvHelperCallback).attachToRecyclerView(binding.travelplanListRv)

            binding.travelplanListRv.setOnTouchListener{ _, _ ->
                travelPlanListRvHelperCallback.removePreviousClamp(binding.travelplanListRv)
                false
            }
        })
    }
    companion object{
        @JvmStatic
        fun newInstance(key1:String, value1:Int, key2:String, value2:Int) =
            TravelPlanListFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1,value1)
                    putInt(key2,value2)
                }
            }
    }
}