package com.ssafy.groute.src.main

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.main.board.SearchAdapter
import com.ssafy.groute.src.main.home.PlaceFilterAdapter

@BindingAdapter("imageUrlArea")
fun bindImageArea(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load("${ApplicationClass.IMGS_URL_AREA}${imgUrl}")
        .into(imgView)
}

@BindingAdapter("imageUrlPlace")
fun bindImagePlace(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
        .into(imgView)
}

//@BindingAdapter("placeListData")  -> recycler view Adapter 사용할 때 쓰는 databinding
//fun bindRecyclerView(recyclerView: RecyclerView, place: List<Place>?) {
//    var adapter = recyclerView.adapter as AreaFilterAdapter
//
//    if (recyclerView.adapter == null) {
//        adapter.setHasStableIds(true)
//        recyclerView.adapter = adapter
//    } else {
//        adapter = recyclerView.adapter as AreaFilterAdapter
//    }
//    adapter.placeList = place as MutableList<Place>
//    adapter.notifyDataSetChanged()
//}

@BindingAdapter("placeListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    val adapter = recyclerView.adapter as PlaceFilterAdapter
    adapter.submitList(data)
}


@BindingAdapter("searchListData")
fun bindSearchRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    var adapter = recyclerView.adapter as SearchAdapter

    if (recyclerView.adapter == null) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    } else {
        adapter = recyclerView.adapter as SearchAdapter
    }
    adapter.places = data as List<Place>
    adapter.notifyDataSetChanged()
}


