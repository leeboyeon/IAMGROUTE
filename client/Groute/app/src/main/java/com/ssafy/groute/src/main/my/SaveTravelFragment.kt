package com.ssafy.groute.src.main.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentMyTravelBinding
import com.ssafy.groute.databinding.FragmentSaveTravelBinding
import com.ssafy.groute.src.main.route.RouteListRecyclerviewAdapter
import com.ssafy.groute.src.viewmodel.PlanViewModel

class SaveTravelFragment : BaseFragment<FragmentSaveTravelBinding>(FragmentSaveTravelBinding::bind, R.layout.fragment_save_travel) {
//    private lateinit var binding: FragmentSaveTravelBinding
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter
    private val planViewModel: PlanViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    fun initAdapter(){
        RouteListAdapter = RouteListRecyclerviewAdapter(planViewModel, viewLifecycleOwner)
        binding.myRvSave.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RouteListAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveTravelFragment().apply {

            }
    }
}