package com.ssafy.groute.src.main.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.service.CommentService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "CommentAdapter_groute"
class CommentAdapter(val context: Context, val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<CommentAdapter.CommentHolder>(){
    var list = mutableListOf<Comment>()

    // 현재 로그인한 유저의 아이디
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

    fun setCommentList(list: List<Comment>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }

    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val more = itemView.findViewById<ImageView>(R.id.comment_more_iv)
        val commentNestedTv = itemView.findViewById<TextView>(R.id.comment_nested_btn)
        fun bindInfo(data : Comment){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                lifecycleOwner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(itemView.findViewById(R.id.comment_nested_detail_iv_userImg))
                    itemView.findViewById<TextView>(R.id.comment_nested_detail_tv_userNick).text = it.nickname
                }
            )
            itemView.findViewById<TextView>(R.id.comment_nested_detail_tv_comment).text = data.content

            if(userId != data.userId) { // 로그인한 유저 댓글이 아닐 때
                more.visibility = View.GONE
            }

            // 답글 달기 버튼을 눌렀을 때
            commentNestedTv.setOnClickListener {
                itemClickListener.onCommentNestedClick(layoutPosition, data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_comment_list_item,parent,false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])

            more.setOnClickListener {
                val popup: PopupMenu = PopupMenu(context,more)
                MenuInflater(context).inflate(R.menu.board_menu_item, popup.menu)

                popup.show()
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            itemClickListener.onEditClick(position, list[position])
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_delete ->{
                            CommentService().deleteBoardComment(list[position].id, DeleteCallback(position))
                            return@setOnMenuItemClickListener true
                        }else->{
                        return@setOnMenuItemClickListener false
                    }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
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
                list.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }

    }
}