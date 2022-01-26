package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentBoardDetailDetailBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import org.json.JSONObject

private const val TAG = "BoardDetailDetailFragme"
class BoardDetailDetailFragment : Fragment() {
    private lateinit var binding: FragmentBoardDetailDetailBinding
    private lateinit var mainActivity: MainActivity
    private var commentAdapter:CommentAdapter = CommentAdapter()
    private val lists = arrayListOf<Comment>()

    private var boardDetailId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        arguments?.let {
            boardDetailId = it.getInt("boardDetailId", -1)
            Log.d(TAG, "onCreate: $boardDetailId")
        }
        mainActivity.hideBottomNav(true)
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
        initData()
    }
    fun initData(){
        getListBoardDetail(boardDetailId)
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
    fun getUserInfo(userId:String){
        val userInfo = UserService().getUserInfo(userId)
        userInfo.observe(
            viewLifecycleOwner, {
                Glide.with(this)
                    .load(it.img)
                    .circleCrop()
                    .into(binding.boardDetailIvUserImg)
            }
        )
    }
    fun getListBoardDetail(id:Int){
        BoardService().getListBoardDetail(id, object : RetrofitCallback<Map<String,Any>> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Map<String, Any>) {

                Log.d(TAG, "onSuccess: ${JSONObject(responseData).getJSONObject("boardDetail")}")
                Log.d(TAG, "onSuccess: ${JSONObject(responseData)}")

                val boardDetail = JSONObject(responseData).getJSONObject("boardDetail")
                val title = boardDetail.get("title")
                val content = boardDetail.get("content")
                val userId = boardDetail.get("userId")
                val img = boardDetail.get("img")

                binding.boardDetailTvUserName.setText(userId.toString())
                binding.boardDtailTvTitle.setText(title.toString())
                binding.boardDetailTvContent.setText(content.toString())
                getUserInfo(userId.toString())
                if(img == "" || img == null){
                    binding.boardDetailIvImg.visibility = GONE
                }else{
                    Glide.with(this@BoardDetailDetailFragment)
                        .load(img)
                        .into(binding.boardDetailIvImg)
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    companion object {
        @JvmStatic
        fun newInstance(key: String, value:Int) =
            BoardDetailDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}