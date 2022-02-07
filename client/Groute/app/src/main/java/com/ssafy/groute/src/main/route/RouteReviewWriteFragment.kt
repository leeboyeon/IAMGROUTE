package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
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
import com.ssafy.groute.databinding.FragmentRouteReviewWriteBinding
import com.ssafy.groute.src.dto.PlanReview
import androidx.lifecycle.Observer
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteReviewWriteFrament"
class RouteReviewWriteFragment : BaseFragment<FragmentRouteReviewWriteBinding>(FragmentRouteReviewWriteBinding::bind, R.layout.fragment_route_review_write) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    private var reviewId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let {
            planId = it.getInt("planId",-1)
            reviewId = it.getInt("reviewId",-1)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.planViewModel = planViewModel
        runBlocking {
            planViewModel.getPlanById(planId)
            planViewModel.getPlanReviewById(reviewId)
        }

        binding.routeReviewWriteIbtnBack.setOnClickListener{
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
        if(reviewId > 0) {
            binding.routeReviewWriteBtnWrite.text = "리뷰 수정"
            planViewModel.review.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "onViewCreated: ${it.toString()}")
                binding.routeReviewWriteEtContent.setText(it.content)
                binding.routeReviewWriteRatingBar.rating = it.rate.toFloat()
            })
            initModifyButton()

        } else {
            initButton()
        }


    }

    fun initModifyButton(){
        binding.routeReviewWriteBtnWrite.setOnClickListener {
            val content = binding.routeReviewWriteEtContent.text.toString()
            var rate = binding.routeReviewWriteRatingBar.rating.toDouble()
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
            val review = PlanReview(
                planId,
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
        binding.routeReviewWriteBtnWrite.setOnClickListener {
            val content = binding.routeReviewWriteEtContent.text.toString()
            var rate = binding.routeReviewWriteRatingBar.rating.toDouble()
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
            val review = PlanReview(
                planId,
                userId,
                content,
                rate,
                ""
            )
            insertReview(review)
        }
    }

    fun modifyReview(review: PlanReview){
        UserPlanService().updatePlanReview(review, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: ")
                mainActivity.moveFragment(12,"planId", planId)
                Toast.makeText(requireContext(),"리뷰수정 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    fun insertReview(review: PlanReview){
        UserPlanService().insertPlanReview(review, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: ")
                mainActivity.moveFragment(12,"planId", planId)
                Toast.makeText(requireContext(),"리뷰작성 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(key1: String, value1: Int, key2: String, value2: Int) =
            RouteReviewWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                    putInt(key2, value2)
                }
            }
    }
}