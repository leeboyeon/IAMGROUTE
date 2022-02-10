package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentReviewWriteBinding
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "ReviewWriteFragment"
class ReviewWriteFragment : BaseFragment<FragmentReviewWriteBinding>(FragmentReviewWriteBinding::bind, R.layout.fragment_review_write) {
    private lateinit var mainActivity: MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private var placeId = -1
    private var reviewId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            placeId = it.getInt("placeId",-1)
            reviewId = it.getInt("reviewId",-1)
            Log.d(TAG, "onAttach_ReviewId: ${reviewId}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기
        binding.viewModel = placeViewModel
        runBlocking {
            placeViewModel.getPlace(placeId)
            placeViewModel.getReviewById(reviewId)
            Log.d(TAG, "onViewCreated: ${placeViewModel.getReviewById(reviewId)}")
        }
        binding.reviewWriteIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
        if(reviewId > 0){
            binding.reviewWriteBtnWrite.text = "리뷰 수정"
            placeViewModel.review.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "onViewCreated: ${it.toString()}")
                binding.reviewWriteEtContent.setText(it.content)
                binding.ratingBar.rating = it.rate.toFloat()
            })
            initModifyButton()
        }else{
            initButton()
        }

    }
    fun initModifyButton(){
        binding.reviewWriteBtnWrite.setOnClickListener {
            val content = binding.reviewWriteEtContent.text.toString()
            var rate = binding.ratingBar.rating.toDouble()
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
            val review = PlaceReview(
                placeId,
                userId,
                content,
                rate,
                "",
                reviewId
            )
            modifyReview(review)
        }

    }
    fun initButton(){
        binding.reviewWriteBtnWrite.setOnClickListener {
            val content = binding.reviewWriteEtContent.text.toString()
            Log.d(TAG, "onViewCreated: ${content}")
            var rate = binding.ratingBar.rating.toDouble()
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
            val review = PlaceReview(
                placeId,
                userId,
                content,
                rate,
                ""
            )
            insertReview(review)
        }
    }
    fun modifyReview(review:PlaceReview){
        PlaceService().updatePlaceReview(review, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: ")
                mainActivity.moveFragment(4,"placeId",placeId)
                Toast.makeText(requireContext(),"리뷰수정 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    fun insertReview(review:PlaceReview){
        Log.d(TAG, "insertReview: 클릭됬니?")

        PlaceService().insertPlaceReview(review, object : RetrofitCallback<Boolean> {

            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: 성공했니?")
                mainActivity.moveFragment(4,"placeId",placeId)
                Toast.makeText(requireContext(),"리뷰작성 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(key1:String, value1:Int, key2:String, value2:Int) =
            ReviewWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                    putInt(key2,value2)
                }
            }
    }
    inner class ReviewCallback():RetrofitCallback<PlaceReview>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: PlaceReview) {
            Log.d(TAG, "onSuccess: ")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
}

