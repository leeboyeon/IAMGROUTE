package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSaveTravelBinding
import com.ssafy.groute.databinding.FragmentSharedTravelBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.BestRoute
import com.ssafy.groute.src.viewmodel.MyViewModel
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class SharedTravelFragment : BaseFragment<FragmentSharedTravelBinding>(FragmentSharedTravelBinding::bind, R.layout.fragment_shared_travel) {
    private lateinit var mainActivity: MainActivity
    private lateinit var sharedAdapter: SharedTravelAdapter
    private val planViewModel: PlanViewModel by activityViewModels()
    val lists = mutableListOf<BestRoute>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel
        runBlocking {
            planViewModel.getPlanMyList(ApplicationClass.sharedPreferencesUtil.getUser().id)
            planViewModel.getSharedPlanList()
        }
        initAdapter()
    }
    fun initAdapter(){
        sharedAdapter = SharedTravelAdapter(planViewModel)
        planViewModel.sharedTravelList.observe(viewLifecycleOwner, Observer {
            sharedAdapter.setShareTravelList(it)
            sharedAdapter.notifyDataSetChanged()
            binding.myRvShared.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = sharedAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            sharedAdapter.setItemClickListener(object : SharedTravelAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(12, "planIdDetail", id, "planIdUser", -1)
                }

            })
        })

    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SharedTravelFragment().apply {

            }
    }
}