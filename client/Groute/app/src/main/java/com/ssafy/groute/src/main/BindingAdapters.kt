package com.ssafy.groute.src.main

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load("${ApplicationClass.IMGS_URL_AREA}${imgUrl}")
        .into(imgView)
}
//
//@BindingAdapter("listData")
//fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?) {
//    val adapter = recyclerView.adapter as PhotoGridAdapter
//    adapter.submitList(data)
//}

