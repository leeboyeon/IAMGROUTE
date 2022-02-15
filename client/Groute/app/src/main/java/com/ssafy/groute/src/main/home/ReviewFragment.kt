package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentReviewBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "ReviewFragment"
class ReviewFragment : BaseFragment<FragmentReviewBinding>(FragmentReviewBinding::bind, R.layout.fragment_review) {
    private lateinit var mainActivity: MainActivity
    private lateinit var reviewAdapter:ReviewAdapter
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private var placeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            placeId = it.getInt("placeId",-1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = placeViewModel

        runBlocking {
            placeViewModel.getPlaceReviewListbyId(placeId)
        }

        initAdapter()

        binding.reviewIbtnWrite.setOnClickListener {
            mainActivity.moveFragment(11,"placeId",placeId)
        }
    }

    private fun initAdapter(){
        placeViewModel.placeReviewList.observe(viewLifecycleOwner, Observer {
            reviewAdapter = ReviewAdapter(viewLifecycleOwner,requireContext(), placeViewModel)
            reviewAdapter.list = it
            reviewAdapter.setModifyClickListener(object: ReviewAdapter.ModifyClickListener{
                override fun onClick(position: Int) {
                    mainActivity.moveFragment(11,"placeId",placeId,"reviewId",it[position].id)
                }
            })
            binding.reviewRvList.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = reviewAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(key1:String, value1:Int) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                }
            }
    }
}