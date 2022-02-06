package com.ssafy.groute.src.main.route

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.PlanReview
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.service.UserService

class RouteDetailReviewAdapter(var owner: LifecycleOwner) : RecyclerView.Adapter<RouteDetailReviewAdapter.RouteDetailReviewHolder>(){
    var list = mutableListOf<PlanReview>()
    inner class RouteDetailReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImg = itemView.findViewById<ImageView>(R.id.routedetail_review_recycler_item_iv_userimg)
        val userName = itemView.findViewById<TextView>(R.id.routedetail_review_recycler_item_tv_username)
        val reviewImg = itemView.findViewById<ImageView>(R.id.routedetail_review_recycler_item_iv_reviewimg)
        val rating = itemView.findViewById<RatingBar>(R.id.routedetail_review_recycler_item_rb_rating)
        val content = itemView.findViewById<TextView>(R.id.routedetail_review_recycler_item_tv_content)
        val more = itemView.findViewById<ImageButton>(R.id.routedetail_review_recycler_item_ib_more)

        fun bindInfo(data: PlanReview) {
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                owner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(userImg)
                    userName.text = it.nickname
                }
            )
            rating.rating = data.rate.toFloat()
            if(data.img != null){
                reviewImg.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL_PLACEREVIEW}${data.img}")
                    .into(reviewImg)
            }
            if(data.img == null || data.img == ""){
                reviewImg.visibility = View.GONE
            }
            content.text = data.content

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_routedetail_review_item, parent, false)
        return RouteDetailReviewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteDetailReviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}