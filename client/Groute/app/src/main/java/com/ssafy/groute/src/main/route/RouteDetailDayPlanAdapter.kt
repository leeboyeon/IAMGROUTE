package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewRouteDetailDayPerItemItemBinding
import com.ssafy.groute.src.dto.RouteDetail

class RouteDetailDayPlanAdapter() : RecyclerView.Adapter<RouteDetailDayPlanAdapter.RouteDetailDayPlanHolder>(){
    var list = mutableListOf<RouteDetail>()
    inner class RouteDetailDayPlanHolder(private val binding: RecyclerviewRouteDetailDayPerItemItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(data : RouteDetail, position: Int) {
            binding.routeDetail = data
            binding.executePendingBindings()

            binding.routedetailRecyclerItemItemDayPlaceIv.setOnClickListener{
                itemClickListener.onClick(position, data.placeId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailDayPlanHolder {
        return RouteDetailDayPlanHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_route_detail_day_per_item_item, parent, false))
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