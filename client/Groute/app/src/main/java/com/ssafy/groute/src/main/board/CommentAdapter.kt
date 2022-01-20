package com.ssafy.groute.src.main.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>(){
    var list = mutableListOf<Comment>()
    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Comment){
            Glide.with(itemView)
                .load(data.userImg)
                .into(itemView.findViewById(R.id.comment_iv_userImg))

            itemView.findViewById<TextView>(R.id.comment_tv_userNick).text = data.userNick
            itemView.findViewById<TextView>(R.id.comment_tv_comment).text = data.comment

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
                itemClickListener.onClick(it, position, list[position].userNick)
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