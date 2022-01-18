package com.ssafy.groute.src.main.travel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.databinding.FragmentTravelPlanListBinding

class TravelPlanListFragment : Fragment() {
    lateinit var binding: FragmentTravelPlanListBinding
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
}