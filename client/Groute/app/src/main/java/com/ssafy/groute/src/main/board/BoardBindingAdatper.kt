package com.ssafy.groute.src.main.board

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment

class BoardBindingAdatper {
    companion object {
        @JvmStatic
        @BindingAdapter("listData")
        fun bindRecyclerView(recyclerView: RecyclerView, data: List<BoardDetail>?) {
            val adapter = recyclerView.adapter as BoardAdapter
            adapter.setList(data)
            adapter.notifyDataSetChanged()
        }

        @JvmStatic
        @BindingAdapter("listCommentData")
        fun bindCommentRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
            val adapter = recyclerView.adapter as CommentAdapter
            adapter.setCommentList(data)
            adapter.notifyDataSetChanged()
        }


    }
}