package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentInfoBinding
import com.ssafy.groute.databinding.FragmentReviewBinding
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.dto.Review
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import kotlinx.coroutines.runBlocking

class ReviewFragment : BaseFragment<FragmentReviewBinding>(FragmentReviewBinding::bind, R.layout.fragment_review) {
//    private lateinit var binding: FragmentReviewFragmentBinding
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
        initButton()
    }
    fun initButton(){
        binding.reviewIbtnWrite.setOnClickListener {
            mainActivity.moveFragment(11,"placeId",placeId)
        }
    }

    fun initAdapter(){
        placeViewModel.placeReviewList.observe(viewLifecycleOwner, Observer {
            reviewAdapter = ReviewAdapter(viewLifecycleOwner)
            reviewAdapter.list = it
//            reviewAdapter.setItemClickListener(object : ReviewAdapter.ItemClickListener {
//                override fun onClick(view: View, position: Int, name: String) {
//                    //리뷰 상세페이지제작
//                }
//            })
            binding.reviewRvList.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = reviewAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })
    }
    companion object {

        @JvmStatic
        fun newInstance(key:String, value:Int) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}