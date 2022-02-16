package com.ssafy.groute.src.main.board

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewCommentListItemBinding
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

private const val TAG = "CommentAdapter_groute"
class CommentAdapter( val context: Context, val lifecycleOwner: LifecycleOwner, val boardViewModel: BoardViewModel, val mainViewModel: MainViewModel, var boardDetailId: Int) : RecyclerView.Adapter<CommentAdapter.CommentHolder>(){
//class CommentAdapter(val commentList : MutableList<Comment>, val context: Context, val lifecycleOwner: LifecycleOwner, val boardViewModel: BoardViewModel, val mainViewModel: MainViewModel)
//    : ListAdapter<Comment, CommentAdapter.CommentHolder>(DiffCallback){
    var commentList = mutableListOf<Comment>()

    // 현재 로그인한 유저의 아이디
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

    fun setCommentListData(list: List<Comment>?) {
        if(list == null) {
            this.commentList = ArrayList()
        } else {
            this.commentList = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }

    inner class CommentHolder(private val binding: RecyclerviewCommentListItemBinding) : RecyclerView.ViewHolder(binding.root){
        val more = itemView.findViewById<ImageView>(R.id.comment_more_iv)
        val commentNestedTv = itemView.findViewById<TextView>(R.id.comment_nested_btn)
        val commentNestedRv = itemView.findViewById<RecyclerView>(R.id.comment_nested_rv)
        fun bindInfo(data : Comment){
            if(userId != data.userId) { // 로그인한 유저 댓글이 아닐 때
                more.visibility = View.GONE
            }

            runBlocking {
                mainViewModel.getUserInformation(data.userId, false)
                val user = mainViewModel.userInformation.value!!
                val writeUser = User(user.id, user.nickname, user.img.toString())
                binding.writeUser = writeUser
            }

            binding.comment = data
//            binding.boardViewModels = boardViewModel
            binding.executePendingBindings()
                val list = mutableListOf<Comment>()
            Log.d(TAG, "bindInfo: ${boardViewModel.commentAllList.value!!}")
                for(i in 0 until boardViewModel.commentAllList.value!!.size) {
                    if(boardViewModel.commentAllList.value!!.get(i).groupNum == data.groupNum) {
                        if(boardViewModel.commentAllList.value!!.get(i).level == 1) {
                            list.add(boardViewModel.commentAllList.value!!.get(i))
                            //Log.d(TAG, "bindInfo: ${boardViewModel.commentAllList.value!!.get(i)}, $list")
                        }
                    }
                }
                //Log.d(TAG, "bindInfo CommentAdapter: ${list}")
//                    commentNestedAdapter.setCommentNestedList(list)
                // 같은 그룹인 comment list
                val commentNestedAdapter = CommentNestedAdapter(list, context, lifecycleOwner, true, mainViewModel)
//                commentNestedAdapter.submitList(list)
                commentNestedRv.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = commentNestedAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                commentNestedAdapter.setItemClickListener(object: CommentNestedAdapter.ItemClickListener {
                    override fun onEditClick(position: Int, comment: Comment) {
                    }
                })




//            val userInfo = UserService().getUserInfo(data.userId)
//            userInfo.observe(
//                lifecycleOwner, {
//                    Glide.with(itemView)
//                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
//                        .circleCrop()
//                        .into(itemView.findViewById(R.id.comment_detail_iv_userImg))
//                    itemView.findViewById<TextView>(R.id.comment_detail_tv_userNick).text = it.nickname
//                }
//            )
//            itemView.findViewById<TextView>(R.id.comment_detail_tv_comment).text = data.content






        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder{
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_comment_list_item,parent,false)
//        return CommentHolder(view)
        return CommentHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_comment_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.apply {
            bindInfo(commentList[position])

            // 답글 달기 버튼을 눌렀을 때
            commentNestedTv.setOnClickListener {
                itemClickListener.onCommentNestedClick(position, commentList[position])
            }

            // 더보기 버튼 클릭
            more.setOnClickListener {
                val popup: PopupMenu = PopupMenu(context,more)
                MenuInflater(context).inflate(R.menu.board_menu_item, popup.menu)

                popup.show()
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            itemClickListener.onEditClick(position, commentList[position])
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_delete ->{
                            CommentService().deleteBoardComment(commentList[position].id, DeleteCallback(position))
                            return@setOnMenuItemClickListener true
                        } else -> {
                            return@setOnMenuItemClickListener false
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    interface ItemClickListener{
        fun onEditClick(position: Int, comment: Comment)
        fun onCommentNestedClick(position: Int, comment: Comment)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    inner class DeleteCallback(var position:Int): RetrofitCallback<Any> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Any) {
            if(responseData == "SUCCESS"){
                Toast.makeText(context,"삭제되었습니다.", Toast.LENGTH_SHORT).show()
                commentList.removeAt(position)
                notifyDataSetChanged()
                runBlocking {
                    boardViewModel.getBoardDetail(boardDetailId)
                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }
    }
}