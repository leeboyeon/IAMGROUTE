package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.RouteDetail

class RouteDetailDayPlanAdapter() : RecyclerView.Adapter<RouteDetailDayPlanAdapter.RouteDetailDayPlanHolder>(){
    var list = mutableListOf<RouteDetail>()
    inner class RouteDetailDayPlanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName = itemView.findViewById<TextView>(R.id.routedetail_recycler_item_item_day_placeName_tv)
        val placeType = itemView.findViewById<TextView>(R.id.routedetail_recycler_item_item_day_placeType_tv)
        val placeImg = itemView.findViewById<ImageView>(R.id.routedetail_recycler_item_item_day_place_img)
        val placeBtn = itemView.findViewById<ImageView>(R.id.routedetail_recycler_item_item_day_place_iv)

        fun bindInfo(data : RouteDetail, position: Int) {
            Glide.with(itemView)
                .load("${ApplicationClass.IMGS_URL_PLACE}${data.place.img}")
                .into(placeImg)
            placeName.text = "${data.place.name}"
            placeType.text = "${data.place.type}"

            placeBtn.setOnClickListener{
                itemClickListener.onClick(position, data.placeId)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailDayPlanHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_route_detail_day_per_item_item, parent, false)
        return RouteDetailDayPlanHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RouteDetailDayPlanHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(position: Int, placeId: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}