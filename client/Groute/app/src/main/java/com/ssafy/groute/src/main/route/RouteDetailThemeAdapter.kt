package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.ListItemRouteThemeBinding
import com.ssafy.groute.src.dto.Theme

class RouteDetailThemeAdapter(var list: MutableList<Theme>) : RecyclerView.Adapter<RouteDetailThemeAdapter.RouteDetailThemeHolder>(){
//    var list = mutableListOf<Theme>()
//
//    fun setThemeList(list: List<Theme>?) {
//        if(list == null) {
//            this.list = ArrayList()
//        } else {
//            this.list = list.toMutableList()!!
//        }
//    }
    inner class RouteDetailThemeHolder(private val binding: ListItemRouteThemeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(theme : Theme) {
            binding.theme = theme
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailThemeHolder {
        return RouteDetailThemeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_route_theme, parent, false))
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun onBindViewHolder(holder: RouteDetailThemeHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}