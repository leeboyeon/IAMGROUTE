//package com.ssafy.groute.src.main.board
//
//import android.annotation.SuppressLint
//import android.util.Log
//import android.widget.ImageView
//import androidx.databinding.BindingAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.bumptech.glide.request.RequestOptions
//import com.ssafy.groute.src.dto.BoardDetail
//import com.ssafy.groute.src.dto.Comment
//
//private const val TAG = "BindingAdapter_groute"
//class BoardBindingAdapter {
////    companion object {
////    @JvmStatic
//    @BindingAdapter("listData")
//    fun bindRecyclerView(recyclerView: RecyclerView, data: List<BoardDetail>?) {
//        val adapter = recyclerView.adapter as BoardAdapter
//        adapter.submitList(data)
////            adapter.setList(data)
////            adapter.notifyDataSetChanged()
//    }
//
////    @JvmStatic
//    @BindingAdapter("boardPostListData")
//    fun bindBoardRecyclerView(recyclerView: RecyclerView, data: List<BoardDetail>?) {
//        val adapter = recyclerView.adapter as BoardRecyclerviewAdapter
//        adapter.submitList(data)
//    }
//
////    @JvmStatic
//    @BindingAdapter("listCommentData")
//    fun bindCommentRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
//        val adapter = recyclerView.adapter as CommentAdapter
//        adapter.setCommentList(data)
//        adapter.notifyDataSetChanged()
//    }
//
////    @JvmStatic
//    @BindingAdapter("listCommentNestedData")
//    fun bindCommentNestedRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
//        val adapter = recyclerView.adapter as CommentNestedAdapter
//        Log.d(TAG, "bindCommentNestedRecyclerView: ${data}")
//        adapter.setCommentNestedList(data)
//        adapter.notifyDataSetChanged()
//    }
//
////    @JvmStatic
//    @BindingAdapter("listNestedData")
//    fun bindtNestedRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
//        val adapter = recyclerView.adapter as CommentNestedAdapter
//        Log.d(TAG, "bindCommentNestedRecyclerView: ${data}")
//        adapter.setCommentNestedList(data)
//        adapter.notifyDataSetChanged()
//    }
//
//
////    }
//
//}