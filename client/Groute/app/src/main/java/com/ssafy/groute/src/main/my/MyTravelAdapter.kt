package com.ssafy.groute.src.main.my

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R

class MyTravelAdapter : RecyclerView.Adapter<MyTravelAdapter.MyTravelHolder>(){
    var list = mutableListOf<MyTravel>()
    inner class MyTravelHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data:MyTravel){
            Glide.with(itemView)
                .load(data.img)
                .into(itemView.findViewById(R.id.mytravel_iv_img))
            itemView.findViewById<TextView>(R.id.mytravel_tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.mytravel_tv_due).text = data.due
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTravelHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_travleing_item,parent,false)
        return MyTravelHolder(view)
    }

    override fun onBindViewHolder(holder: MyTravelHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].title)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ItemClickListener{
        fun onClick(view: View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}