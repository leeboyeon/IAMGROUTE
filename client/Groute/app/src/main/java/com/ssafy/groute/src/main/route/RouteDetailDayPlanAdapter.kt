package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class RouteDetailDayPlanAdapter() : RecyclerView.Adapter<RouteDetailDayPlanAdapter.RouteDetailDayPlanThemeHolder>(){

    inner class RouteDetailDayPlanThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName = itemView.findViewById<TextView>(R.id.routedetail_recycler_item_item_day_placeName_tv)
        val placeType = itemView.findViewById<TextView>(R.id.routedetail_recycler_item_item_day_placeType_tv)
        val placeImg = itemView.findViewById<ImageView>(R.id.routedetail_recycler_item_item_day_place_img)
        val placeBtn = itemView.findViewById<ImageView>(R.id.routedetail_recycler_item_item_day_place_iv)

        fun bindInfo() {
            placeImg.setImageResource(R.drawable.profile)
            placeName.text = "섭지코지"
            placeType.text = "관광지"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailDayPlanThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_route_detail_day_per_item_item, parent, false)
        return RouteDetailDayPlanThemeHolder(view)
    }

    override fun onBindViewHolder(holder: RouteDetailDayPlanThemeHolder, position: Int) {
        holder.apply {
            bindInfo()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}