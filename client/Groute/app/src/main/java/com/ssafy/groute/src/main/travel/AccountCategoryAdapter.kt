package com.ssafy.groute.src.main.travel


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewAccountCategoryListItemBinding
import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.src.dto.AccountCategory

class AccountCategoryAdapter: RecyclerView.Adapter<AccountCategoryAdapter.ACategoryHolder>(){
    var list = mutableListOf<AccountCategory>()
    inner class ACategoryHolder(private var binding:RecyclerviewAccountCategoryListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: AccountCategory){
            binding.category= item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ACategoryHolder {
        return ACategoryHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_account_category_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ACategoryHolder, position: Int) {
        val item = list[position]
        holder.apply {
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,list[position].id)
            }
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ItemClickListener{
        fun onClick(view:View,position: Int,id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}