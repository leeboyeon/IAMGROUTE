package com.ssafy.groute.src.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class RouteListRecyclerviewAdapter() : RecyclerView.Adapter<RouteListRecyclerviewAdapter.RouteListHolder>(){

    inner class RouteListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeDate = itemView.findViewById<TextView>(R.id.item_route_date_tv)
        val routeArea = itemView.findViewById<TextView>(R.id.item_route_area_tv)
        val routeTitle = itemView.findViewById<TextView>(R.id.item_route_title_tv)

        fun bindInfo() {
            routeDate.text = "12 March, 20"
            routeArea.text = "[서울]"
            routeTitle.text = "경복궁, 당일치기 겨울여행"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_route, parent, false)
        return RouteListHolder(view)
    }

    override fun onBindViewHolder(holder: RouteListHolder, position: Int) {
        holder.apply {
            bindInfo()
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}