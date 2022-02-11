package com.ssafy.groute.src.main.board

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardPostDetailBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.CommentService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.viewmodel.BoardViewModel
import com.ssafy.groute.src.viewmodel.MainViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

private const val TAG = "BoardDetailDF_Groute"
class BoardDetailDetailFragment : BaseFragment<FragmentBoardPostDetailBinding>(FragmentBoardPostDetailBinding::bind, R.layout.fragment_board_post_detail) {
    private lateinit var mainActivity: MainActivity
    private lateinit var commentAdapter:CommentAdapter
    private var userId : Any= ""
    val boardViewModel: BoardViewModel by activityViewModels()
    val mainViewModel: MainViewModel by activityViewModels()
    val loginUserId: String = ApplicationClass.sharedPreferencesUtil.getUser().id    // 로그인한 userId
    private lateinit var intent: Intent

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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        runBlocking {
            boardViewModel.getBoardDetailNoHit(boardDetailId)
        }
        boardViewModel.commentList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onResume: ${it}")
            commentAdapter.setCommentListData(it)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataBinding()

        initBtnEvent()

        initCommentAdapter()

        initData()

        mainActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        intent = Intent(mainActivity, CommentNestedActivity::class.java)

    }

    private fun initDataBinding() {
        runBlocking {
            boardViewModel.getBoardDetail(boardDetailId)
        }

        boardViewModel.boardDetail.observe(viewLifecycleOwner,{
            runBlocking {
                mainViewModel.getUserInformation(it.userId, false)
            }
            val writeUserInfo = mainViewModel.userInformation.value!!
            val writeUser = User(writeUserInfo.id, writeUserInfo.nickname, writeUserInfo.img.toString())
            binding.writeUser = writeUser

            binding.boardDetail = it
        })

        mainViewModel.loginUserInfo.observe(viewLifecycleOwner, {
            val loginUser = User(it.id, it.nickname, it.img.toString())
            binding.loginUser = loginUser
        })

        binding.boardViewModels = boardViewModel
    }

    private fun initBtnEvent() {
        // 좋아요(👍) 버튼 클릭 이벤트
        binding.boardDetailIvHeart.setOnClickListener{
            boardLike()
        }

        // backBtn click Event
        binding.boardDetailIbtnBack.setOnClickListener {
            Log.d(TAG, "onViewCreated: CLICK")
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    private fun initData(){
//        getListBoardDetail(boardDetailId)
        runBlocking {
            boardViewModel.getBoardPostIsLike(boardDetailId, loginUserId)
        }
        Log.d(TAG, "initData: ${boardViewModel.isBoardPostLike.value}")
        if(boardViewModel.isBoardPostLike.value == true) {
            binding.boardDetailIvHeart.setColorFilter(requireContext().resources.getColor(R.color.red))
        } else {
            binding.boardDetailIvHeart.setColorFilter(requireContext().resources.getColor(R.color.black))
        }
        commentWrite(boardDetailId)
    }

    private fun initCommentAdapter(){
//        runBlocking {
//            boardViewModel.getBoardDetail(boardDetailId)
//        }
        boardViewModel.commentList.observe(viewLifecycleOwner, {
            Log.d(TAG, "initCommentAdapter: $it")
            commentAdapter = CommentAdapter(requireContext(), viewLifecycleOwner, boardViewModel, mainViewModel)
            commentAdapter.setCommentListData(it)

            binding.boardDetailRvComment.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = commentAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            commentAdapter.setItemClickListener(object: CommentAdapter.ItemClickListener{
                override fun onEditClick(position: Int, comment: Comment) {
                    showEditDialog(comment)
                }

                override fun onCommentNestedClick(position: Int, comment: Comment) {
                    Log.d(TAG, "onCommentNestedClick: $comment")
                    intent.putExtra("commentData", comment)
                    startActivity(intent)
                }

            })
        })
    }

    // 게시글 좋아요 눌렀을 때
    private fun boardLike() {
        BoardService().boardLike(boardDetailId, loginUserId, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 게시판 찜하기 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                Log.d(TAG, "onSuccess: BoardDetail 찜하기 성공")
//                getListBoardDetail(boardDetailId)
                runBlocking {
                    boardViewModel.getBoardDetailNoHit(boardDetailId)
                    boardViewModel.getBoardPostIsLike(boardDetailId, loginUserId)
                }
                Log.d(TAG, "onSuccess: ${boardViewModel.isBoardPostLike.value}")
                if(boardViewModel.isBoardPostLike.value == true) {
                    binding.boardDetailIvHeart.setColorFilter(requireContext().resources.getColor(R.color.red))
                } else {
                    binding.boardDetailIvHeart.setColorFilter(requireContext().resources.getColor(R.color.black))
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    // 댓글 수정?
    fun showEditDialog(comment: Comment) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_comment_edit, null)
//        val uComment = comment
        view.findViewById<EditText>(R.id.dialog_comment_edit_et).setText(comment.content)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("")
            .setPositiveButton("수정") {dialogInterface, i ->
                val editText: EditText = view.findViewById(R.id.dialog_comment_edit_et)
                val content = editText.text.toString()
                comment.content = content
                updateComment(comment)
            }
            .setNegativeButton("취소", null)
            .create()

        alertDialog.setCancelable(false)
        alertDialog.setView(view)
        alertDialog.show()
    }

    // comment 수정
    private fun updateComment(comment: Comment) {
        CommentService().updateBoardComment(comment, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 댓글 수정 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                showCustomToast("수정되었습니다.")
                runBlocking {
                    boardViewModel.getBoardDetailNoHit(boardDetailId)
                }
//                boardViewModel.getBoardDetailWithComment(viewLifecycleOwner, boardDetailId)
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: 댓글 수정 실패")
            }

        })
    }

    private fun getUserInfo(userId:String) {
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

    // login User가 해당 게시글에 좋아요 눌렀는지 체크
    private fun getListBoardDetail(id:Int){

        BoardService().isBoardLike(id, loginUserId, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 찜하기 여부 에러")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
//                runBlocking {
//                    boardViewModel.getBoardDetail(boardDetailId)
//                }
                Log.d(TAG, "onSuccess: $responseData")
                if(responseData) {
                    binding.boardDetailIvHeart.setColorFilter(requireContext().resources.getColor(R.color.red))
                } else {
                    binding.boardDetailIvHeart.setColorFilter(requireContext().resources.getColor(R.color.black))
                }
            }
            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })

//        BoardService().getListBoardDetail(id, object : RetrofitCallback<Map<String,Any>> {
//            override fun onError(t: Throwable) {
//                Log.d(TAG, "onError: ")
//            }
//
//            override fun onSuccess(code: Int, responseData: Map<String, Any>) {
//
//                val boardDetail = JSONObject(responseData).getJSONObject("boardDetail")
//                var title = boardDetail.get("title")
//                val content = boardDetail.get("content")
//                userId = boardDetail.get("userId")
//                val img = boardDetail.get("img")
//                val boardId = boardDetail.get("boardId").toString().substring(0,1)
//                val heartCnt = boardDetail.get("heartCnt").toString().substring(0,1)
//
//                var bd = BoardDetail(
//                    title.toString(),
//                    content.toString(),
//                    img.toString(),
//                    boardId.toInt(),
//                    heartCnt.toInt(),
//                    userId.toString()
//                )
//
//                binding.boardDetail = bd
//
//                getUserInfo(userId.toString())
//                if(img == "" || img == null){
//                    binding.boardDetailIvImg.visibility = GONE
//                }else{
//                    Glide.with(this@BoardDetailDetailFragment)
//                        .load("${ApplicationClass.IMGS_URL}${bd.img}")
//                        .into(binding.boardDetailIvImg)
//                }
//            }
//
//            override fun onFailure(code: Int) {
//                Log.d(TAG, "onFailure: ")
//            }
//
//        })
    }

    // comment insert
    private fun commentWrite(boardDetailId: Int) {
//        val uId = ApplicationClass.sharedPreferencesUtil.getUser().id
//        viewModel.getUser().observe(viewLifecycleOwner, Observer {
//            if(it.type.equals("sns")){
//                Glide.with(this)
//                    .load(it.img)
//                    .circleCrop()
//                    .into(binding.commentWriteProfileIv)
//            } else{
//                Glide.with(this)
//                    .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
//                    .circleCrop()
//                    .into(binding.commentWriteProfileIv)
//            }
//        })

        binding.commentWriteTv.setOnClickListener {
            if(binding.commentWriteEt.text.toString() == "") {
                Toast.makeText(mainActivity, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val comment = Comment(boardDetailId, binding.commentWriteEt.text.toString(), boardViewModel.boardDetail.value!!.commentCnt + 1, loginUserId)
                CommentService().insertBoardComment(comment, object : RetrofitCallback<Any> {
                    override fun onError(t: Throwable) {
                        Log.d(TAG, "onError: 댓글 쓰기 에러")
                    }
                    override fun onSuccess(code: Int, responseData: Any) {
                        runBlocking {
                            boardViewModel.getBoardDetailNoHit(boardDetailId)
                        }
                        binding.commentWriteEt.setText("")
//                        boardViewModel.getBoardDetailWithComment(viewLifecycleOwner, boardDetailId)
                        //Toast.makeText(requireContext(),"댓글 쓰기 성공",Toast.LENGTH_SHORT).show()
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