package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.util.Log
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

class RouteDetailDayPerAdapter(val viewLifecycleOwner: LifecycleOwner, var list: MutableList<Int>, val planViewModel: PlanViewModel) : RecyclerView.Adapter<RouteDetailDayPerAdapter.RouteDetailDayPerHolder>(){

    inner class RouteDetailDayPerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTv = itemView.findViewById<TextView>(R.id.routedetail_recycler_item_day_tv)
        val routeDetailDayPlanRv = itemView.findViewById<RecyclerView>(R.id.routedetail_recycler_item_day_rv)

        fun bindInfo(day : Int) {
            dayTv.text = "$day DAY"

            runBlocking {
                planViewModel.getRouteDetailbyDay(day)
            }

            val routeDetailDayPlanAdapter = RouteDetailDayPlanAdapter()
            planViewModel.routeDetailList.observe(viewLifecycleOwner, Observer {
                routeDetailDayPlanAdapter.list = it
            })

            routeDetailDayPlanRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = routeDetailDayPlanAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            routeDetailDayPlanAdapter.setItemClickListener(object : RouteDetailDayPlanAdapter.ItemClickListener{
                override fun onClick(position: Int, placeId: Int) {
                    itemClickListener.onClick(position, placeId)
                }

            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailDayPerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_route_detail_day_per_item, parent, false)
        return RouteDetailDayPerHolder(view)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: RouteDetailDayPerHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
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