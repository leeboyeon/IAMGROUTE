package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardDetailDetailBinding
import com.ssafy.groute.databinding.FragmentReviewBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.CommentService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.BoardViewModel
import com.ssafy.groute.util.RetrofitCallback
import org.json.JSONObject

private const val TAG = "BoardDetailDF_Groute"
class BoardDetailDetailFragment : BaseFragment<FragmentBoardDetailDetailBinding>(FragmentBoardDetailDetailBinding::bind, R.layout.fragment_board_detail_detail) {
//    private lateinit var binding: FragmentBoardDetailDetailBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var commentAdapter:CommentAdapter
    private var userId : Any= ""
    var boardViewModel: BoardViewModel = BoardViewModel()

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
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        binding = FragmentBoardDetailDetailBinding.inflate(layoutInflater,container,false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        initAdapter()
        initData()


        binding.boardDetailIbtnBack.setOnClickListener {
            Log.d(TAG, "onViewCreated: CLICK")
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }
    fun initData(){
        getListBoardDetail(boardDetailId)
        commentWrite(boardDetailId)
    }

    fun initViewModels(){
        boardViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        binding.lifecycleOwner = this
        binding.boardViewModels = boardViewModel
    }
    fun initAdapter(){
        boardViewModel.getBoardDetailWithComment(this, boardDetailId)
        commentAdapter = CommentAdapter(viewLifecycleOwner)
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
                    .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
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

                val boardDetail = JSONObject(responseData).getJSONObject("boardDetail")
                var title = boardDetail.get("title")
                val content = boardDetail.get("content")
                userId = boardDetail.get("userId")
                val img = boardDetail.get("img")
                val boardId = boardDetail.get("boardId").toString().substring(0,1)

                var bd = BoardDetail(
                    title.toString(),
                    content.toString(),
                    img.toString(),
                    boardId.toInt(),
                    userId.toString()
                )

                binding.boardDetail = bd

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

    fun commentWrite(boardDetailId: Int) {
        val uId = ApplicationClass.sharedPreferencesUtil.getUser().id
        binding.commentWriteTv.setOnClickListener {
            if(binding.commentWriteEt.text.toString() == "") {
                Toast.makeText(mainActivity, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                var comment = Comment(boardDetailId, binding.commentWriteEt.text.toString(), boardViewModel.commentCount.value!! + 1, uId)
                CommentService().insertBoardComment(comment, object : RetrofitCallback<Any> {
                    override fun onError(t: Throwable) {
                        Log.d(TAG, "onError: 댓글 쓰기 에러")
                    }
                    override fun onSuccess(code: Int, responseData: Any) {
                        boardViewModel.getBoardDetailWithComment(viewLifecycleOwner, boardDetailId)
                        Toast.makeText(requireContext(),"댓글 쓰기 성공",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(code: Int) {
                        Log.d(TAG, "onFailure: 댓글 쓰기 실패")
                    }

                })

            }
        }
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