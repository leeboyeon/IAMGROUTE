package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentMyTravelBinding
import com.ssafy.groute.databinding.FragmentSaveTravelBinding
import com.ssafy.groute.src.dto.PlanLike
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.route.RouteListRecyclerviewAdapter
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "SaveTravelFragment"
class SaveTravelFragment : BaseFragment<FragmentSaveTravelBinding>(FragmentSaveTravelBinding::bind, R.layout.fragment_save_travel) {
//    private lateinit var binding: FragmentSaveTravelBinding
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter
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
        RouteListAdapter = RouteListRecyclerviewAdapter(planViewModel, viewLifecycleOwner)
        planViewModel.planLikeList.observe(viewLifecycleOwner, Observer {
            RouteListAdapter.setRouteList(it)

            binding.myRvSave.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = RouteListAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            RouteListAdapter.setHeartClickListener(object : RouteListRecyclerviewAdapter.HeartClickListener{
                override fun onClick(view: View, position: Int, planId: Int) {
                    planLike(PlanLike(userId, planId), position)
                }

            })
            RouteListAdapter.setItemClickListener(object : RouteListRecyclerviewAdapter.ItemClickListener {
                override fun onClick(position: Int, id: Int, totalDate: Int) {
                    mainActivity.moveFragment(12, "planIdDetail", id, "planIdUser", -1)
                }

            })
        })

    }

    fun planLike(planLike: PlanLike, position: Int) {
        UserPlanService().planLike(planLike, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 루트 좋아요 에러")
            }
            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: 루트 좋아요 성공")
                runBlocking {
                    planViewModel.getPlanLikeList(userId)
                }

            }
            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveTravelFragment().apply {

            }
    }
}