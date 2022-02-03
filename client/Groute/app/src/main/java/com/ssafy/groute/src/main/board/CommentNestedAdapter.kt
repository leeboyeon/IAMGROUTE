package com.ssafy.groute.src.main.board

import android.annotation.SuppressLint
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

private const val TAG = "CommentNestedAdapter_groute"
class CommentNestedAdapter(val context: Context, val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<CommentNestedAdapter.CommentNestedHolder>(){
    var list = mutableListOf<Comment>()

    // 현재 로그인한 유저의 아이디
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

    fun setCommentNestedList(list: List<Comment>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }

    inner class CommentNestedHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val more = itemView.findViewById<ImageView>(R.id.comment_nested_more_iv)
        fun bindInfo(data : Comment){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                lifecycleOwner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(itemView.findViewById(R.id.comment_nested_iv_userImg))
                    itemView.findViewById<TextView>(R.id.comment_nested_tv_userNick).text = it.nickname
                }
            )
            itemView.findViewById<TextView>(R.id.comment_nested_tv_comment).text = data.content

            if(userId != data.userId) { // 로그인한 유저 댓글이 아닐 때
                more.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentNestedHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_nested_comment_list_item,parent,false)
        return CommentNestedHolder(view)
    }

    override fun onBindViewHolder(holder: CommentNestedHolder, position: Int) {
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
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    @SuppressLint("LongLogTag")
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