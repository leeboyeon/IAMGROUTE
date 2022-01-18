package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.src.main.home.Category

class MemberAdapter : RecyclerView.Adapter<MemberAdapter.MemberHolder>(){
    var list = mutableListOf<Member>()
    inner class MemberHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Member){
            val img = itemView.findViewById<ImageView>(R.id.route_iv_memberimg)
            img.clipToOutline = true
            Glide.with(itemView)
                .load(data.img)
                .into(img)

            itemView.findViewById<TextView>(R.id.route_tv_membername).text = data.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_route_addmember_item,parent,false)
        return MemberHolder(view)
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].name)
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