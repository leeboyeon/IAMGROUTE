package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewCartListItemBinding
import com.ssafy.groute.src.dto.Place

class PlaceShopAdapter : RecyclerView.Adapter<PlaceShopAdapter.PlaceShopHolder>(){
    var list = listOf<Place>()
    inner class PlaceShopHolder(private var binding:RecyclerviewCartListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: Place){
            binding.place = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceShopHolder {
        return PlaceShopHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.recyclerview_cart_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlaceShopHolder, position: Int) {
        val dto = list[position]
        holder.apply {
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,list[position].id)
            }
            bindInfo(dto)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ItemClickListener{
        fun onClick(view:View,position: Int,id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}