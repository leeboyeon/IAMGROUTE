package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewBestrouteItemBinding
import com.ssafy.groute.src.dto.Place

class BestPlaceAdapter(var list:MutableList<Place>)  : RecyclerView.Adapter<BestPlaceAdapter.BestPlaceHolder>(){
    inner class BestPlaceHolder(private val binding: RecyclerviewBestrouteItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data : Place){
            binding.place = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestPlaceHolder {
        return BestPlaceHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_bestroute_item, parent, false))
    }

    override fun onBindViewHolder(holder: BestPlaceHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}