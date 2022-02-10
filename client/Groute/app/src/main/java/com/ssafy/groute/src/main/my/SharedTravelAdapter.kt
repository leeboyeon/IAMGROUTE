package com.ssafy.groute.src.main.my

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.main.home.BestRoute
import com.ssafy.groute.src.main.home.BestRouteAdapter

class SharedTravelAdapter()  : RecyclerView.Adapter<SharedTravelAdapter.SharedHolder>(){
    var list = mutableListOf<UserPlan>()

    fun setShareTravelList(list: List<UserPlan>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }
    inner class SharedHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : UserPlan){
            itemView.findViewById<TextView>(R.id.home_best_title).text = data.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bestroute_item,parent,false)
        return SharedHolder(view)
    }

    override fun onBindViewHolder(holder: SharedHolder, position: Int) {
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