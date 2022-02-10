package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewCategoryItemBinding
import com.ssafy.groute.src.dto.Area

class HomeAreaAdapter(private val areaList: MutableList<Area>) : RecyclerView.Adapter<HomeAreaAdapter.HomeAreaViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAreaViewHolder {
        return HomeAreaViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_category_item, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAreaViewHolder, position: Int) {
        val dto = areaList[position]
        holder.apply {
            itemView.findViewById<ConstraintLayout>(R.id.rvItem_main_cL_areaAll).setOnClickListener {
                itemClickListener.onClick(it, position, dto.name, dto.id)
            }
            bind(dto)
        }
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    inner class HomeAreaViewHolder(private var binding:RecyclerviewCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(area: Area) {
            binding.area = area
            binding.executePendingBindings()
        }

    }

//    class OnClickListener(val clickListener: (area: Area) -> Unit) {
//        fun onClick(area: Area) = clickListener(area)
//    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, name: String, id: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

}