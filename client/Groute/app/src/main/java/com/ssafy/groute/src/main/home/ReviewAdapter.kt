package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>(){
    var list = mutableListOf<Review>()
    inner class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Review){
            Glide.with(itemView)
                .load(data.userimg)
                .into(itemView.findViewById(R.id.review_iv_userimg))
            itemView.findViewById<TextView>(R.id.review_tv_username).text = data.username
            itemView.findViewById<RatingBar>(R.id.review_rb_rating).rating = data.rating.toFloat()

            Glide.with(itemView)
                .load(data.reviewimg)
                .into(itemView.findViewById(R.id.review_iv_reviewimg))
            itemView.findViewById<TextView>(R.id.review_tv_content)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_placereview_item,parent,false)
        return ReviewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].username)
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