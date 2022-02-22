package com.ssafy.groute.src.main.route


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import androidx.lifecycle.Observer
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteDetailReviewBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

//private const val TAG = "RouteDetailReviewF"
class RouteDetailReviewFragment : BaseFragment<FragmentRouteDetailReviewBinding>(FragmentRouteDetailReviewBinding::bind, R.layout.fragment_route_detail_review) {
    private lateinit var mainActivity: MainActivity
    private lateinit var routeDetailReviewAdapter: RouteDetailReviewAdapter
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel

        runBlocking {
            planViewModel.getPlanReviewListbyId(planId)
        }

        initAdapter()

        binding.routedetailReviewIbtnWrite.setOnClickListener{
            mainActivity.moveFragment(14, "planId", planId)
        }
    }

    private fun initAdapter(){
        planViewModel.planReviewList.observe(viewLifecycleOwner, Observer {
            routeDetailReviewAdapter = RouteDetailReviewAdapter(viewLifecycleOwner, requireContext(), planViewModel)
            routeDetailReviewAdapter.list = it
            routeDetailReviewAdapter.setModifyClickListener(object : RouteDetailReviewAdapter.ModifyClickListener {
                override fun onClick(position: Int) {
                    mainActivity.moveFragment(14, "planId", planId, "reviewId", it[position].id)
                }

            })

            binding.routedetailReviewRvList.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = routeDetailReviewAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })
    }



    companion object {

        @JvmStatic
        fun newInstance(key: String, value: Int) =
            RouteDetailReviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}