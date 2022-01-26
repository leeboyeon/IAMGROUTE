package com.ssafy.groute.src.main.board

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.service.UserService

class CommentAdapter(val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<CommentAdapter.CommentHolder>(){
    var list = mutableListOf<Comment>()

    fun setCommentList(list: List<Comment>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Comment){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                lifecycleOwner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(itemView.findViewById(R.id.comment_iv_userImg))
                    itemView.findViewById<TextView>(R.id.comment_tv_userNick).text = it.nickname
                }
            )
            itemView.findViewById<TextView>(R.id.comment_tv_comment).text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_comment_list_item,parent,false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                //itemClickListener.onClick(it, position, list[position].userNick)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}