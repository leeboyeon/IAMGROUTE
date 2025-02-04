package com.ssafy.groute.src.main

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.*
import com.ssafy.groute.src.main.board.*
import com.ssafy.groute.src.main.home.PlaceFilterAdapter
import com.ssafy.groute.src.main.home.ReviewAdapter
import com.ssafy.groute.src.main.my.MyTravelAdapter
import com.ssafy.groute.src.main.my.NotificationAdapter
import com.ssafy.groute.src.main.my.SharedTravelAdapter
import com.ssafy.groute.src.main.route.*
import com.ssafy.groute.src.main.travel.PlaceShopAdapter
import com.ssafy.groute.src.main.travel.SharedMemberAdapter
import com.ssafy.groute.src.main.travel.TravelPlanListRecyclerviewAdapter
import com.ssafy.groute.util.CommonUtils

@BindingAdapter("imageUrlArea")
fun bindImageArea(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load("${ApplicationClass.IMGS_URL_AREA}${imgUrl}")
        .into(imgView)
}

@BindingAdapter("imageUrlPlace")
fun bindImagePlace(imgView: ImageView, imgUrl: String?) {
    if (imgUrl == "null" || imgUrl == null || imgUrl.isEmpty()) {
        Glide.with(imgView.context)
            .load(R.drawable.defaultimg)
            .into(imgView)
    } else {
        Glide.with(imgView.context)
            .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
            .into(imgView)
    }
}

@BindingAdapter("imageUrlBestPlace")
fun bindImageBestPlace(imgView: ImageView, imgUrl: String?) {
    if (imgUrl == "null" || imgUrl == null || imgUrl.isEmpty()) {
        Glide.with(imgView.context)
            .load(R.drawable.defaultimg)
            .circleCrop()
            .into(imgView)
    } else {
        Glide.with(imgView.context)
            .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
            .circleCrop()
            .into(imgView)
    }
}

@BindingAdapter("imageUrlUser")
fun bindImageUser(imgView: ImageView, imgUrl: String?) {
    if(imgUrl != null) {
        if(imgUrl.contains("https://")) {
            Glide.with(imgView.context)
                .load(imgUrl)
                .circleCrop()
                .into(imgView)
        } else {
            Glide.with(imgView.context)
                .load("${ApplicationClass.IMGS_URL_USER}${imgUrl}")
                .circleCrop()
                .into(imgView)
        }
    }
}

@BindingAdapter("imageUrlBoardPost")    // + PlaceReview
fun bindImageBoardPost(imgView: ImageView, imgUrl: String?) {

    if (imgUrl == "null" || imgUrl == null || imgUrl.isEmpty()) {
        imgView.visibility = View.GONE
    } else {
        imgView.visibility = View.VISIBLE
        Glide.with(imgView.context)
            .load("${ApplicationClass.IMGS_URL}${imgUrl}")
            .into(imgView)
    }
}

@BindingAdapter("imageUrlAccount")
fun bindImageAccount(imgView:ImageView, imgUrl:String?){
    Glide.with(imgView.context)
        .load("${ApplicationClass.IMGS_URL}${imgUrl}")
        .into(imgView)
}

@BindingAdapter("imageUrlPlaceRoundCorner")
fun bindPlaceImgRoundCorner(imgView: ImageView, imgUrl: String?) {
    val option2 = MultiTransformation(CenterCrop(), RoundedCorners(10))
    if (imgUrl == "null" || imgUrl == null || imgUrl.isEmpty()) {
        Glide.with(imgView.context)
            .load(R.drawable.defaultimg)
            .apply(RequestOptions.bitmapTransform(option2))
            .into(imgView)
    } else {
        Glide.with(imgView.context)
            .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
            .apply(RequestOptions.bitmapTransform(option2))
            .into(imgView)
    }
}

@BindingAdapter("makeComma")
fun bindMakeComma(textview:TextView, price:Int){
    textview.text = CommonUtils.makeComma(price)
}

@BindingAdapter("setRating")
fun bindRatingBar(ratingView:RatingBar, rating : Double) {
//    val rate = rating.toFloat()
    ratingView.rating = rating.toFloat()
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
fun bindPlanDetailRecyclerView(recyclerView:RecyclerView, data: List<Route>?){
    var adapter = recyclerView.adapter as TravelPlanListRecyclerviewAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as TravelPlanListRecyclerviewAdapter
    }
    adapter.list = data as MutableList<Route>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("placeshopListData")
fun bindPlaceShopRecyclerView(recyclerView: RecyclerView, data : List<Place>?){
    var adapter = recyclerView.adapter as PlaceShopAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as PlaceShopAdapter
    }
    adapter.list = data as List<Place>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("planReviewListData")
fun bindPlanReviewRecyclerView(recyclerView:RecyclerView, data: List<PlanReview>){
    var adapter = recyclerView.adapter as RouteDetailReviewAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as RouteDetailReviewAdapter
    }
    adapter.list = data as MutableList<PlanReview>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("planThemeListData")
fun bindThemeReviewRecyclerView(recyclerView:RecyclerView, data: List<Theme>?){
    var adapter = recyclerView.adapter as RouteDetailThemeAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as RouteDetailThemeAdapter
    }
    adapter.list = data as MutableList<Theme>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("routeListData")
fun bindRouteListRecyclerView(recyclerView: RecyclerView, data: List<UserPlan>?) {
    var adapter = recyclerView.adapter as RouteListRecyclerviewAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as RouteListRecyclerviewAdapter
    }
    adapter.list = data as MutableList<UserPlan>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("routeThemeListData")
fun bindRouteThemeRecyclerView(recyclerView: RecyclerView, data: List<Theme>?) {
    var adapter = recyclerView.adapter as RouteThemeRecyclerviewAdapter
    adapter.setThemeList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("shareMamberListData")
fun bindShareMemberRecyclerView(recyclerView: RecyclerView, data:List<User>?){
    var adapter = recyclerView.adapter as SharedMemberAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as SharedMemberAdapter
    }
    adapter.list = data as ArrayList<User>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("bottomSheetListData")
fun bindbottomSheetRecyclerView(recyclerView: RecyclerView, data: List<UserPlan>?) {
    var adapter = recyclerView.adapter as BottomSheetRecyclerviewAdapter
    adapter.setUserPlanList(data)
    adapter.notifyDataSetChanged()
}


/**
 * board 관련 bindingAdapter
 */
@BindingAdapter("listData") // BoardFragment + BoardAdapter
fun bindBoardRecyclerView(recyclerView: RecyclerView, data: List<BoardDetail>?) {

    var adapter = recyclerView.adapter as BoardAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as BoardAdapter
    }

    val tmp = mutableListOf<BoardDetail>()
    if(data != null) {
        if (data.size >= 5) {
            for (i in 0 until 5) {
                tmp.add(data[i])
            }
            adapter.boardList = tmp as MutableList<BoardDetail>
        } else {
            adapter.boardList = data as MutableList<BoardDetail>
        }
    }
    adapter.notifyDataSetChanged()
//    adapter.submitList(data)
//            adapter.setList(data)
//            adapter.notifyDataSetChanged()
}

@BindingAdapter("boardPostListData")
fun bindBoardPostListRecyclerView(recyclerView: RecyclerView, data: List<BoardDetail>?) {
    var adapter = recyclerView.adapter as BoardRecyclerviewAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as BoardRecyclerviewAdapter
    }
    adapter.boardList = data as MutableList<BoardDetail>
    adapter.notifyDataSetChanged()
//    adapter.submitList(data)
}

@BindingAdapter("listCommentData")
fun bindCommentRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
    var adapter = recyclerView.adapter as CommentAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as CommentAdapter
    }
    adapter.setCommentListData(data)
    adapter.notifyDataSetChanged()
//    adapter.submitList(data)
//    adapter.setCommentList(data)
//    adapter.notifyDataSetChanged()
}

@BindingAdapter("listCommentNestedData")
fun bindCommentNestedRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
    var adapter = recyclerView.adapter as CommentNestedAdapter
    if(recyclerView.adapter == null){
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }else{
        adapter = recyclerView.adapter as CommentNestedAdapter
    }
    adapter.commentList = data as MutableList<Comment>
    adapter.notifyDataSetChanged()
}


@BindingAdapter("sharedPlanListData")
fun bindSharedPlanListRecyclerView(recyclerView: RecyclerView, data: List<UserPlan>?) {
    var adapter = recyclerView.adapter as SharedTravelAdapter
    if (recyclerView.adapter == null) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    } else {
        adapter = recyclerView.adapter as SharedTravelAdapter
    }
    adapter.list = data as MutableList<UserPlan>
    adapter.notifyDataSetChanged()
}

@BindingAdapter("sharedPlanTitle")
fun bindSharedPlanTitle(view: TextView, title: String?) {
    view.text = "[제주도] $title"
}

@BindingAdapter("notificationListData")
fun bindNotificationRecyclerView(recyclerView: RecyclerView, data: List<Notification>?) {
    var adapter = recyclerView.adapter as NotificationAdapter
    if (recyclerView.adapter == null) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    } else {
        adapter = recyclerView.adapter as NotificationAdapter
    }
    adapter.notificationList = data as MutableList<Notification>
    adapter.notifyDataSetChanged()
}
