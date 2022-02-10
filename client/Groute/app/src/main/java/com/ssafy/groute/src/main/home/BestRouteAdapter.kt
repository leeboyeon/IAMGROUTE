package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.UserPlan

class BestRouteAdapter(var list: MutableList<UserPlan>) : RecyclerView.Adapter<BestRouteAdapter.BestRouteHolder>(){

    inner class BestRouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : UserPlan){
            itemView.findViewById<TextView>(R.id.home_best_title).text = "[제주도] ${data.title}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRouteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bestroute_item,parent,false)
        return BestRouteHolder(view)
    }

    override fun onBindViewHolder(holder: BestRouteHolder, position: Int) {
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