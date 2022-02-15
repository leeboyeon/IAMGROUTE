package com.ssafy.groute.src.main.route

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewCategoryItemBinding
import com.ssafy.groute.src.dto.Area

class RoutePageAreaAdapter(private val areaList: MutableList<Area>, val selectList: ArrayList<Int>, val context: Context) : RecyclerView.Adapter<RoutePageAreaAdapter.RoutePageAreaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutePageAreaViewHolder {
        return RoutePageAreaViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_category_item, parent, false))
    }

    override fun onBindViewHolder(holder: RoutePageAreaViewHolder, position: Int) {
        val dto = areaList[position]
        holder.apply {
            areaLayout.setOnClickListener {
                if(selectList[position] == 0) {
                    for(i in 0 until areaList.size) {
                        if(selectList[i] == 1) {
                            selectList[i] = 0
                        }
                    }
                    selectList[position] = 1
                    itemClickListener.onClick(it, position, dto.name, dto.id)
                } else if(selectList[position] == 1) {
                    for(i in 0 until areaList.size) {
                        if(selectList[i] == 1) {
                            selectList[i] = 0
                        }
                    }
                    itemClickListener.onClick(it, position, dto.name, dto.id)
                }
            }
            bind(dto, position)
        }
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    inner class RoutePageAreaViewHolder(private var binding:RecyclerviewCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val areaLayout = itemView.findViewById<ConstraintLayout>(R.id.rvItem_main_cL_areaAll)
        val areaTxt = itemView.findViewById<TextView>(R.id.main_tv_category)
        fun bind(area: Area, position: Int) {
            binding.area = area
            binding.executePendingBindings()

            if(selectList[position] == 1) {
                areaTxt.setTextColor(ContextCompat.getColor(context, R.color.main_500))
            } else {
                areaTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, name: String, id: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

}