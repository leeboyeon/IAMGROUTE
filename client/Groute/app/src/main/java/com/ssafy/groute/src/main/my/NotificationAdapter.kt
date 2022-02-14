package com.ssafy.groute.src.main.my

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewNotificationListItemBinding
import com.ssafy.groute.src.dto.Notification
import com.ssafy.groute.src.viewmodel.NotificationViewModel

class NotificationAdapter(var notificationList : MutableList<Notification>, var lifecycleOwner: LifecycleOwner, val notificationViewModel: NotificationViewModel) : RecyclerView.Adapter<NotificationAdapter.NotificationHolder>(){

    inner class NotificationHolder(private val binding: RecyclerviewNotificationListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(data : Notification) {
            binding.notification = data
            if(data.category.equals("User")){
                Glide.with(itemView)
                    .load(R.drawable.usernoti)
                    .circleCrop()
                    .into(itemView.findViewById<ImageView>(R.id.noti_icon))
            }
            else{
                Glide.with(itemView)
                    .load(R.drawable.eventnoti)
                    .circleCrop()
                    .into(itemView.findViewById<ImageView>(R.id.noti_icon))
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recyclerview_notification_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val data = notificationList[position]
        holder.apply {
            bindInfo(data)
            itemView.findViewById<ImageButton>(R.id.ibtn_delete).setOnClickListener {
                itemClickListener.onClick(it, position, data.id)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return notificationList[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}