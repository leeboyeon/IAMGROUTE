package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>(){
    var list = listOf<Category>()
    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Category){
            Glide.with(itemView)
                .load("${ApplicationClass.IMGS_URL_AREA}${data.img}")
                .into(itemView.findViewById(R.id.main_iv_category))

            itemView.findViewById<TextView>(R.id.main_tv_category).text = data.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_category_item,parent,false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
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
        fun onClick(view:View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}