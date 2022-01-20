package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentBoardDetailDetailBinding
import com.ssafy.groute.src.main.MainActivity

class BoardDetailDetailFragment : Fragment() {
    private lateinit var binding: FragmentBoardDetailDetailBinding
    private lateinit var mainActivity: MainActivity
    private var commentAdapter:CommentAdapter = CommentAdapter()
    private val lists = arrayListOf<Comment>()
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

        binding = FragmentBoardDetailDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }
    fun initAdapter(){
        commentAdapter = CommentAdapter()
        lists.apply {
            add(Comment(userImg = R.drawable.user, userNick = "보연팀장", comment = "지우가 세상에서 젤 너무해.. 디자인 토할거같아요.."))
            add(Comment(userImg = R.drawable.user, userNick = "보연팀장", comment = "지우가 세상에서 젤 너무해.. 디자인 토할거같아요.."))
            add(Comment(userImg = R.drawable.user, userNick = "보연팀장", comment = "지우가 세상에서 젤 너무해.. 디자인 토할거같아요.."))
            add(Comment(userImg = R.drawable.user, userNick = "보연팀장", comment = "지우가 세상에서 젤 너무해.. 디자인 토할거같아요.."))
            commentAdapter.list = lists
            commentAdapter.notifyDataSetChanged()
        }
        binding.boardDetailRvComment.apply{
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = commentAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoardDetailDetailFragment().apply {

            }
    }
}