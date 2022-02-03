package com.ssafy.groute.src.main.board

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseActivity
import com.ssafy.groute.databinding.ActivityCommentNestedBinding
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.main.MainViewModel
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.CommentService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.BoardViewModel
import com.ssafy.groute.util.RetrofitCallback
import androidx.lifecycle.Observer

private const val TAG = "CommentNestedActivity_groute"
class CommentNestedActivity : BaseActivity<ActivityCommentNestedBinding>(ActivityCommentNestedBinding::inflate) {
    var boardViewModel: BoardViewModel = BoardViewModel()
    lateinit var comment: Comment
    private lateinit var commentNestedAdapter:CommentNestedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comment = intent.getSerializableExtra("commentData") as Comment
        initViewModels()
        initAdapter()
        initCommentData()
        commentWrite()

        binding.boardDetailCommentNestedIbtnBack.setOnClickListener{
            finish()
        }

    }

    fun initViewModels(){
        boardViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        binding.lifecycleOwner = this
        binding.boardViewModels = boardViewModel
    }

    fun initAdapter(){
        boardViewModel.getBoardDetailWithNestedComment(this, comment.boardDetailId, comment.groupNum)
        commentNestedAdapter = CommentNestedAdapter(this, this)
        binding.commentNestedDetailRv.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = commentNestedAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        commentNestedAdapter.setItemClickListener(object: CommentNestedAdapter.ItemClickListener{
            override fun onEditClick(position: Int, comment: Comment) {
                //showEditDialog(comment)
            }

            override fun onCommentNestedClick(position: Int, commentId: Int) {
                //mainActivity.moveFragment(11, "commentId", commentId)
            }

        })

    }

    @SuppressLint("LongLogTag")
    fun initCommentData() {
        binding.commentNestedDetailTvComment.text = comment.content
        UserService().getUserInfo(comment.userId).observe(
            this,
            {

                if(it.type.equals("sns")){
                    Glide.with(this)
                        .load(it.img)
                        .circleCrop()
                        .into(binding.commentNestedDetailIvUserImg)
                } else{
                    Glide.with(this)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(binding.commentNestedDetailIvUserImg)
                }
                binding.commentNestedDetailTvUserNick.text = it.nickname
            }
        )
    }

    @SuppressLint("LongLogTag")
    fun commentWrite() {
        Log.d(TAG, "commentWrite: ${comment.boardDetailId}")
        val uId = ApplicationClass.sharedPreferencesUtil.getUser().id
        viewModel.getUser().observe(this, Observer {
            if(it.type.equals("sns")){
                Glide.with(this)
                    .load(it.img)
                    .circleCrop()
                    .into(binding.commentNestedDetailWriteProfileIv)
            } else{
                Glide.with(this)
                    .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                    .circleCrop()
                    .into(binding.commentNestedDetailWriteProfileIv)
            }
        })

        binding.commentNestedDetailWriteTv.setOnClickListener {
            if(binding.commentNestedDetailWriteEt.text.toString() == "") {
                Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                var c = Comment(comment.boardDetailId, binding.commentNestedDetailWriteEt.text.toString(), comment.groupNum, uId, 1, 0)
                CommentService().insertBoardComment(c, object : RetrofitCallback<Any> {

                    override fun onError(t: Throwable) {
                        Log.d(TAG, "onError: 댓글 쓰기 에러")
                    }
                    override fun onSuccess(code: Int, responseData: Any) {
                        boardViewModel.getBoardDetailWithNestedComment(this@CommentNestedActivity, comment.boardDetailId, comment.groupNum)
                        binding.commentNestedDetailWriteEt.setText("")
                        Toast.makeText(this@CommentNestedActivity,"대댓글 쓰기 성공",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(code: Int) {
                        Log.d(TAG, "onFailure: 댓글 쓰기 실패")
                    }

                })

            }
        }
    }

}