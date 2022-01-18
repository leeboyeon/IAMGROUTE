package com.ssafy.groute.src.main.travel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.databinding.FragmentTravelPlanListBinding

class TravelPlanListFragment : Fragment() {
    lateinit var binding: FragmentTravelPlanListBinding
    lateinit var travelPlanListRecyclerviewAdapter: TravelPlanListRecyclerviewAdapter
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
        travelPlanListRecyclerviewAdapter = TravelPlanListRecyclerviewAdapter()
        binding.travelplanListRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = travelPlanListRecyclerviewAdapter
        }
    }
}