package com.ssafy.groute.src.main.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewRouteAddmemberItemBinding
import com.ssafy.groute.src.dto.User

class MemberAdapter : RecyclerView.Adapter<MemberAdapter.MemberHolder>(){
    var list = mutableListOf<User>()
    inner class MemberHolder(private val binding: RecyclerviewRouteAddmemberItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data : User){
            binding.user = data
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        return MemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_route_addmember_item, parent, false))
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
//            itemView.setOnClickListener {
//                itemClickListener.onClick(it, position, list[position].nickname)
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

//    interface ItemClickListener{
//        fun onClick(view: View, position: Int, name: String)
//    }
//    private lateinit var itemClickListener : ItemClickListener
//    fun setItemClickListener(itemClickListener: ItemClickListener){
//        this.itemClickListener = itemClickListener
//    }
}