package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Place

class PlaceShopAdapter : RecyclerView.Adapter<PlaceShopAdapter.PlaceShopHolder>(){
    var list = listOf<Place>()
    inner class PlaceShopHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindInfo(data: Place){
            itemView.findViewById<TextView>(R.id.placeshop_tv_placeName).text = data.name
            itemView.findViewById<TextView>(R.id.placeShop_tv_placeType).text = data.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceShopHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_place_shop_list_item,parent,false)
        return PlaceShopHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceShopHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,list[position].id)
            }
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