package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentReviewFragmentBinding
import com.ssafy.groute.src.main.MainActivity

class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewFragmentBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var reviewAdapter:ReviewAdapter

    val lists = arrayListOf<Review>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }
    fun initAdapter(){
        reviewAdapter = ReviewAdapter()
        lists.apply {
            add(Review(userimg = R.drawable.user,
            username = "김싸피",
            rating = 3.0,
            reviewimg = R.drawable.defaultimg,
            reviewcontent = "가기전부터 너무 가고싶었던 곳인데 별로였어요...왜 이렇게 다들 불친절하고 나는 너무 하기싫은지....진짜 진짜 정말 하기")
            )
            add(Review(userimg = R.drawable.user,
                username = "김싸피",
                rating = 3.0,
                reviewimg = R.drawable.defaultimg,
                reviewcontent = "가기전부터 너무 가고싶었던 곳인데 별로였어요...왜 이렇게 다들 불친절하고 나는 너무 하기싫은지....진짜 진짜 정말 하기")
            )
            add(Review(userimg = R.drawable.user,
                username = "김싸피",
                rating = 3.0,
                reviewimg = R.drawable.defaultimg,
                reviewcontent = "가기전부터 너무 가고싶었던 곳인데 별로였어요...왜 이렇게 다들 불친절하고 나는 너무 하기싫은지....진짜 진짜 정말 하기")
            )
            add(Review(userimg = R.drawable.user,
                username = "김싸피",
                rating = 3.0,
                reviewimg = R.drawable.defaultimg,
                reviewcontent = "가기전부터 너무 가고싶었던 곳인데 별로였어요...왜 이렇게 다들 불친절하고 나는 너무 하기싫은지....진짜 진짜 정말 하기")
            )

            reviewAdapter.list = lists
            reviewAdapter.notifyDataSetChanged()
        }

        binding.reviewRvList.apply{
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = reviewAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewFragment().apply {

            }
    }
}