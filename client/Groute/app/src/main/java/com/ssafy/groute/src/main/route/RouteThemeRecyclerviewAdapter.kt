package com.ssafy.groute.src.main.route

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Theme

class RouteThemeRecyclerviewAdapter(val context : Context) : RecyclerView.Adapter<RouteThemeRecyclerviewAdapter.RouteThemeHolder>(){
    var list = mutableListOf<Theme>()
    var isCheck : Boolean = false
    var selectCheck: ArrayList<Int> = arrayListOf()

    fun setThemeList(list: List<Theme>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            for(i in 0 until list.size) {
                selectCheck.add(0)
            }
            notifyDataSetChanged()
        }
    }

    inner class RouteThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip = itemView.findViewById<Chip>(R.id.chiplayout)
        fun bindInfo(theme : Theme, position: Int) {
            chip.text = theme.name
            if(selectCheck[position] == 1) {
                chip.isChecked = true
            } else {
                chip.isChecked = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_theme_list, parent, false)
        return RouteThemeHolder(view)
    }

    override fun onBindViewHolder(holder: RouteThemeHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

            chip.setOnClickListener{
                if(selectCheck[position] == 0) {
                    selectCheck[position] = 1
                    itemClickListener.onClick(position, list[position].id, selectCheck)
                } else if(selectCheck[position] == 1){
                    selectCheck[position] = 0
                    itemClickListener.onClick(position, list[position].id, selectCheck)
                }
            }
//            itemView.setOnClickListener{
//                itemClickListener.onClick(position,list[position].id, selectCheck)
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(position: Int, id: Int, selectCheck: ArrayList<Int>)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}