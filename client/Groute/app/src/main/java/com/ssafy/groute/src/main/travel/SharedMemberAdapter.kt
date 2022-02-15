package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewSharedmemberListItemBinding
import com.ssafy.groute.src.dto.User

class SharedMemberAdapter : RecyclerView.Adapter<SharedMemberAdapter.SharedMemberHolder>(){
    var list = arrayListOf<User>()
    inner class SharedMemberHolder(private val binding: RecyclerviewSharedmemberListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(data : User){
            binding.user = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SharedMemberAdapter.SharedMemberHolder {
        return SharedMemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_sharedmember_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: SharedMemberAdapter.SharedMemberHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.findViewById<ConstraintLayout>(R.id.sharedMember_cLayout).setOnClickListener {
                itemClickListener.onClick(it,position,list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view:View, position: Int,id:String)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}