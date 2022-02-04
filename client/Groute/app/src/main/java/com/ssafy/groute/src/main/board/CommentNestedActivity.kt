package com.ssafy.groute.src.main.board

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseActivity
import com.ssafy.groute.databinding.ActivityCommentNestedBinding
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.service.CommentService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import androidx.lifecycle.Observer
import com.ssafy.groute.src.viewmodel.BoardViewModel

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
        commentNestedAdapter = CommentNestedAdapter(this, this, false)
        binding.commentNestedDetailRv.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = commentNestedAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        commentNestedAdapter.setItemClickListener(object: CommentNestedAdapter.ItemClickListener{
            override fun onEditClick(position: Int, comment: Comment) {
                showEditDialog(comment)
            }
        })

    }

    fun showEditDialog(comment: Comment) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_comment_edit, null)
        var uComment = comment
        view.findViewById<EditText>(R.id.dialog_comment_edit_et).setText(comment.content)
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("")
            .setPositiveButton("수정") {dialogInterface, i ->
                val editText: EditText = view.findViewById(R.id.dialog_comment_edit_et)
                val content = editText.text.toString()
                uComment.content = content
                updateComment(uComment)
            }
            .setNegativeButton("취소", null)
            .create()

        alertDialog.setCancelable(false)
        alertDialog.setView(view)
        alertDialog.show()


    }

    @SuppressLint("LongLogTag")
    fun updateComment(comment: Comment) {
        CommentService().updateBoardComment(comment, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 댓글 수정 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                showCustomToast("수정되었습니다.")
                boardViewModel.getBoardDetailWithNestedComment(this@CommentNestedActivity, comment.boardDetailId, comment.groupNum)
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: 댓글 수정 실패")
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