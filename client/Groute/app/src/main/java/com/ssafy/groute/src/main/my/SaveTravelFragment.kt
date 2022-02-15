package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSaveTravelBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.route.RouteListRecyclerviewAdapter
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class SaveTravelFragment : BaseFragment<FragmentSaveTravelBinding>(FragmentSaveTravelBinding::bind, R.layout.fragment_save_travel) {

    lateinit var routeListAdapter: RouteListRecyclerviewAdapter
    private val planViewModel: PlanViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = ApplicationClass.sharedPreferencesUtil.getUser().id
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getPlanLikeList(userId)
        }
        initAdapter()
    }

    fun initAdapter(){
        routeListAdapter = RouteListRecyclerviewAdapter(planViewModel, viewLifecycleOwner)
        planViewModel.planLikeList.observe(viewLifecycleOwner, Observer {
            routeListAdapter.setRouteList(it)

            binding.myRvSave.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = routeListAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            routeListAdapter.setItemClickListener(object : RouteListRecyclerviewAdapter.ItemClickListener {
                override fun onClick(position: Int, id: Int, totalDate: Int) {
                    mainActivity.moveFragment(12, "planIdDetail", id, "planIdUser", -1)
                }

            })
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveTravelFragment().apply {

            }
    }
}