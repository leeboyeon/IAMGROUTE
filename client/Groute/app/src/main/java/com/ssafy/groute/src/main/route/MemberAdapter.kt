package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewRouteAddmemberItemBinding
import com.ssafy.groute.src.dto.Member
import com.ssafy.groute.src.dto.User

class MemberAdapter : RecyclerView.Adapter<MemberAdapter.MemberHolder>(){
    var list = mutableListOf<User>()
    inner class MemberHolder(private val binding: RecyclerviewRouteAddmemberItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data : User){
//            val user = User(data.id, data.name, data.img)
            binding.user = data
            binding.executePendingBindings()
//            val img = itemView.findViewById<ImageView>(R.id.route_iv_memberimg)
//            img.clipToOutline = true
//            Glide.with(itemView)
//                .load(data.img)
//                .into(img)
//
//            itemView.findViewById<TextView>(R.id.route_tv_membername).text = data.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        return MemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_route_addmember_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].nickname)
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