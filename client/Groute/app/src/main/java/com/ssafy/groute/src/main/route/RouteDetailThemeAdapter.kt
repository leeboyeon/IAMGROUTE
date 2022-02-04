package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class RouteDetailThemeAdapter(var themeList: List<String>) : RecyclerView.Adapter<RouteDetailThemeAdapter.RouteDetailThemeHolder>(){

    inner class RouteDetailThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val themeTv = itemView.findViewById<TextView>(R.id.routeDetailThemeTxt)

        fun bindInfo(theme : String) {
            themeTv.text = theme
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_route_theme, parent, false)
        return RouteDetailThemeHolder(view)
    }

    override fun onBindViewHolder(holder: RouteDetailThemeHolder, position: Int) {
        holder.apply {
            bindInfo(themeList[position])
        }
    }

    override fun getItemCount(): Int {
        return themeList.size
    }
}