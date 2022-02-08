package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.User

class SharedMemberAdapter : RecyclerView.Adapter<SharedMemberAdapter.SharedMemberHolder>(){
    var list = arrayListOf<User>()
    inner class SharedMemberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindInfo(data : User){
            itemView.findViewById<TextView>(R.id.sharedMember_tv_userNick).text = data.nickname
            if(data.img == null || data.img==""){
                Glide.with(itemView)
                    .load(R.drawable.defaultimg)
                    .circleCrop()
                    .into(itemView.findViewById<ImageView>(R.id.sharedMember_iv_userImg))

            }
            if(data.img!=null){
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL_USER}${data.img}")
                    .circleCrop()
                    .into(itemView.findViewById<ImageView>(R.id.sharedMember_iv_userImg))

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SharedMemberAdapter.SharedMemberHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_sharedmember_list_item,parent,false)
        return SharedMemberHolder(view)
    }

    override fun onBindViewHolder(holder: SharedMemberAdapter.SharedMemberHolder, position: Int) {
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
        fun onClick(view:View, position: Int,id:String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}