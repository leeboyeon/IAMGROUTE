package com.ssafy.groute.src.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewCategoryItemBinding
import com.ssafy.groute.src.dto.Area
import retrofit2.Call

//class HomeAreaAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<HomeAreaAdapter.HomeAreaViewHolder>(HomeAreaAdapter){
class HomeAreaAdapter(private val onClickListener: OnClickListener, private val areaList: MutableList<Area>) : RecyclerView.Adapter<HomeAreaAdapter.HomeAreaViewHolder>(){
//class HomeAreaAdapter(private val areaList: MutableList<Area>) : RecyclerView.Adapter<HomeAreaAdapter.HomeAreaViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAreaViewHolder {
        return HomeAreaViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_category_item, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAreaViewHolder, position: Int) {
        val dto = areaList[position]
//        Log.d("HomeAreaAdapter", "onBindViewHolder: $dto")
        holder.itemView.setOnClickListener {
            onClickListener.onClick(dto)
        }
        holder.bind(dto)
    }

    override fun getItemCount(): Int {
        return areaList.size
    }


    class HomeAreaViewHolder(private var binding:RecyclerviewCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(area: Area) {
            binding.area = area
            binding.executePendingBindings()
        }

    }

    class OnClickListener(val clickListener: (area: Area) -> Unit) {
        fun onClick(area: Area) = clickListener(area)
    }


}