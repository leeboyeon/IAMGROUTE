package com.ssafy.groute.src.main.travel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewAccountInBinding
import com.ssafy.groute.src.dto.Account

class AccountInAdapter(var context: Context, var list:MutableList<Account>) : RecyclerView.Adapter<AccountInAdapter.AccountInHolder>() {

    inner class AccountInHolder(private var binding:RecyclerviewAccountInBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Account){
            binding.account = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountInAdapter.AccountInHolder {
        return AccountInHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.recyclerview_account_in,parent,false))
    }

    override fun onBindViewHolder(holder: AccountInAdapter.AccountInHolder, position: Int) {
        val item = list[position]
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}