package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class RouteDetailDayPerAdapter(val viewLifecycleOwner: LifecycleOwner, val totalDate: Int, val planViewModel: PlanViewModel) : RecyclerView.Adapter<RouteDetailDayPerAdapter.RouteDetailDayPerThemeHolder>(){

    inner class RouteDetailDayPerThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTv = itemView.findViewById<TextView>(R.id.routedetail_recycler_item_day_tv)
        val routeDetailDayPlanRv = itemView.findViewById<RecyclerView>(R.id.routedetail_recycler_item_day_rv)

        fun bindInfo(day : Int) {
            dayTv.text = "$day DAY"

            runBlocking {
                planViewModel.getRouteDetailbyDay(day)
            }

            planViewModel.routeDetailList.observe(viewLifecycleOwner, Observer {
                var routeDetailDayPlanAdapter = RouteDetailDayPlanAdapter()
                routeDetailDayPlanAdapter.list = it

                routeDetailDayPlanRv.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = routeDetailDayPlanAdapter
                }

            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailDayPerThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_route_detail_day_per_item, parent, false)
        return RouteDetailDayPerThemeHolder(view)
    }

    override fun onBindViewHolder(holder: RouteDetailDayPerThemeHolder, position: Int) {
        holder.apply {
            bindInfo(position + 1)
        }
    }

    override fun getItemCount(): Int {
        return totalDate
    }
}