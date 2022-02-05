package com.ssafy.groute.src.main

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.*
import com.ssafy.groute.src.main.board.SearchAdapter
import com.ssafy.groute.src.main.home.PlaceFilterAdapter
import com.ssafy.groute.src.main.home.ReviewAdapter
import com.ssafy.groute.src.main.my.MyTravel
import com.ssafy.groute.src.main.my.MyTravelAdapter
import com.ssafy.groute.src.main.travel.TravelPlanListRecyclerviewAdapter

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

@BindingAdapter("reviewListData")
fun bindreviewRecyclerView(recyclerView: RecyclerView, data:List<PlaceReview>){
    var adapter = recyclerView.adapter as ReviewAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    } else {
        adapter = recyclerView.adapter as ReviewAdapter
    }

    adapter.list = data as MutableList<PlaceReview>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("myPlanListData")
fun bindMyPlanRecyclerView(recyclerView: RecyclerView,data:List<UserPlan>){
    var adapter = recyclerView.adapter as MyTravelAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as MyTravelAdapter
    }
    adapter.list = data as MutableList<UserPlan>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("plandetailListData")
fun bindPlanDetailRecyclerView(recyclerView:RecyclerView, data: List<RouteDetail>){
    var adapter = recyclerView.adapter as TravelPlanListRecyclerviewAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as TravelPlanListRecyclerviewAdapter
    }
    adapter.list = data as MutableList<RouteDetail>
    adapter.notifyDataSetChanged()
}

