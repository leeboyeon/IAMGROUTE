package com.ssafy.groute.src.main.travel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.databinding.FragmentTravelPlanListBinding
import com.ssafy.groute.src.main.my.MyTravel

class TravelPlanListFragment : Fragment() {
    lateinit var binding: FragmentTravelPlanListBinding
    lateinit var travelPlanListRecyclerviewAdapter: TravelPlanListRecyclerviewAdapter
    val list = arrayListOf<TravelPlan>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelPlanListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.add(TravelPlan("협재 해수욕장", "애월 한림"))
        list.add(TravelPlan("새별오름", "애월 봉성리"))
        list.add(TravelPlan("아쿠아플라넷 제주", "서귀포 성산"))
        list.add(TravelPlan("용머리해안", "서귀포 안덕"))
        list.add(TravelPlan("주상절리", "서귀포 중문"))
        travelPlanListRecyclerviewAdapter = TravelPlanListRecyclerviewAdapter(requireContext(), list)
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
    }
}