package com.ssafy.groute.src.main.route

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Theme
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteDetailThemeAdapter"
class RouteDetailThemeAdapter(val viewLifecycleOwner: LifecycleOwner, val planViewModel: PlanViewModel) : RecyclerView.Adapter<RouteDetailThemeAdapter.RouteDetailThemeHolder>(){
    var list = mutableListOf<Theme>()

    fun setThemeList(list: List<Theme>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
        }
    }
    inner class RouteDetailThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val themeTv = itemView.findViewById<TextView>(R.id.routeDetailThemeTxt)
        fun bindInfo(theme : Theme) {
            Log.d(TAG, "bindInfo: ${theme.name}")
            themeTv.text = theme.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_route_theme, parent, false)
        return RouteDetailThemeHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return list.get(position).id.toLong()
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