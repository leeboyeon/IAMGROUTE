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
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentReviewWriteBinding
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "ReviewWriteFragment"
class ReviewWriteFragment : BaseFragment<FragmentReviewWriteBinding>(FragmentReviewWriteBinding::bind, R.layout.fragment_review_write) {
    private lateinit var mainActivity: MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private var placeId = -1
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
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기
        binding.reviewWriteIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
        var content = binding.reviewWriteEtContent.text.toString()
        var rate = binding.ratingBar.progress

        // 리뷰 작성하기
        binding.reviewWriteBtnWrite.setOnClickListener {
            val review = PlaceReview(
                placeId,
                ApplicationClass.sharedPreferencesUtil.getUser().id,
                content,
                rate.toInt(),
                ""
            )
            insertReview(review)
        }
    }
    fun insertReview(review:PlaceReview){
        PlaceService().insertPlaceReview(review, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
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
        fun newInstance(key:String, value:Int) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}