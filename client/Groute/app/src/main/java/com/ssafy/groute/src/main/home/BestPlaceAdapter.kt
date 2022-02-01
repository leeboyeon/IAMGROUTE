package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Place

class BestPlaceAdapter(var list:MutableList<Place>)  : RecyclerView.Adapter<BestPlaceAdapter.BestPlaceHolder>(){
    inner class BestPlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Place){
            Glide.with(itemView)
                .load("${ApplicationClass.IMGS_URL_PLACE}${data.img}")
                .circleCrop()
                .into(itemView.findViewById(R.id.home_best_img))

            itemView.findViewById<TextView>(R.id.home_best_title).text = data.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestPlaceHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bestroute_item,parent,false)
        return BestPlaceHolder(view)
    }

    override fun onBindViewHolder(holder: BestPlaceHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}