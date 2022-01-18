package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class RouteThemeRecyclerviewAdapter(var themeList: List<String>) : RecyclerView.Adapter<RouteThemeRecyclerviewAdapter.RouteThemeHolder>(){

    inner class RouteThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val themeTv = itemView.findViewById<TextView>(R.id.themeTxt)

        fun bindInfo(theme : String) {
            themeTv.text = theme
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_theme, parent, false)
        return RouteThemeHolder(view)
    }

    override fun onBindViewHolder(holder: RouteThemeHolder, position: Int) {
        holder.apply {
            bindInfo(themeList[position])
        }
    }

    override fun getItemCount(): Int {
        return themeList.size
    }
}