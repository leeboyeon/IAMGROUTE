package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewMemoListItemBinding
import com.ssafy.groute.src.dto.Route

class MemoAdapter : RecyclerView.Adapter<MemoAdapter.MemoHolder>(){
    var list = mutableListOf<Route>()
    inner class MemoHolder(private var binding:RecyclerviewMemoListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: Route){
            binding.route = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        return MemoHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_memo_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val dto = list[position]
        holder.apply {
            bindInfo(dto)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}