package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewRouteAreaitemBinding
import com.ssafy.groute.src.dto.Area

class RouteAreaAdapter(private val areaList:MutableList<Area>) : RecyclerView.Adapter<RouteAreaAdapter.RouteAreaHolder>(){

    inner class RouteAreaHolder(private var binding:RecyclerviewRouteAreaitemBinding)  : RecyclerView.ViewHolder(binding.root){
        fun bind(data : Area){
            binding.area = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteAreaHolder {
        return RouteAreaHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.recyclerview_route_areaitem, parent, false))
    }

    override fun onBindViewHolder(holder: RouteAreaHolder, position: Int) {
        val dto = areaList[position]
        holder.apply {
            var clickFlag = false

            itemView.setOnClickListener {
                if(clickFlag == false){
                    itemClickListener.onClick(it, position, dto.id)
                    itemView.findViewById<LottieAnimationView>(R.id.checklottie_create).visibility = View.VISIBLE
                    clickFlag = true
                }else{
                    itemView.findViewById<LottieAnimationView>(R.id.checklottie_create).visibility = View.INVISIBLE
                    clickFlag = false

                }

            }
            bind(dto)
        }
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    interface ItemClickListener{
        fun onClick(view:View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}