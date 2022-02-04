package com.ssafy.groute.src.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.dto.Review
import com.ssafy.groute.src.service.UserService

private const val TAG = "ReviewAdapter"
class ReviewAdapter(var owner: LifecycleOwner) : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>(){
    var list = mutableListOf<PlaceReview>()
    inner class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : PlaceReview){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                owner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(itemView.findViewById(R.id.review_iv_userimg))
                    itemView.findViewById<TextView>(R.id.review_tv_username).text = it.nickname
                    Log.d(TAG, "bindInfo: ${it.nickname}")
                }
            )
            itemView.findViewById<RatingBar>(R.id.review_rb_rating).rating = data.rate.toFloat()
            if(data.img != null){
                itemView.findViewById<ImageView>(R.id.review_iv_reviewimg).visibility = View.VISIBLE
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL_PLACEREVIEW}${data.img}")
                    .into(itemView.findViewById(R.id.review_iv_reviewimg))
            }
            if(data.img == null || data.img == ""){
                itemView.findViewById<ImageView>(R.id.review_iv_reviewimg).visibility = View.GONE
            }
            itemView.findViewById<TextView>(R.id.review_tv_content).text = data.content


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_placereview_item,parent,false)
        return ReviewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
//            itemView.setOnClickListener {
//                itemClickListener.onClick(it, position, list[position].id)
//            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }


}