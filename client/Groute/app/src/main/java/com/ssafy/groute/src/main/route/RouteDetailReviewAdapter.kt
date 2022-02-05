package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class RouteDetailReviewAdapter() : RecyclerView.Adapter<RouteDetailReviewAdapter.RouteDetailReviewHolder>(){

    inner class RouteDetailReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImg = itemView.findViewById<ImageView>(R.id.routedetail_review_recycler_item_iv_userimg)
        val userName = itemView.findViewById<TextView>(R.id.routedetail_review_recycler_item_tv_username)
        val reviewImg = itemView.findViewById<ImageView>(R.id.routedetail_review_recycler_item_iv_reviewimg)
        val rating = itemView.findViewById<RatingBar>(R.id.routedetail_review_recycler_item_rb_rating)
        val content = itemView.findViewById<TextView>(R.id.routedetail_review_recycler_item_tv_content)
        val more = itemView.findViewById<ImageButton>(R.id.routedetail_review_recycler_item_ib_more)

        fun bindInfo() {
            userImg.setImageResource(R.drawable.profile)
            userName.text = "김싸피"
            reviewImg.setImageResource(R.drawable.defaultimg)
            content.text = "가기전부터 너무 가고싶었던 곳인데 별로였어요...왜 이렇게 다들 불친절하고 나는 너무 하기싫은지....진짜 진짜 정말 하기"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_routedetail_review_item, parent, false)
        return RouteDetailReviewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteDetailReviewHolder, position: Int) {
        holder.apply {
            bindInfo()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}