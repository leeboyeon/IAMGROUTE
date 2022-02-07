package com.ssafy.groute.src.main.route

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
        val themeTv = itemView.findViewById<TextView>(R.id.themeTxt)
        val cardView = itemView.findViewById<CardView>(R.id.cardview)

        fun bindInfo(theme : Theme, position: Int) {
            themeTv.text = theme.name

            if(selectCheck[position] == 1) {
                cardView.background.setTint(ContextCompat.getColor(context, R.color.main_500))
            } else {
                cardView.background.setTint(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteThemeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_theme, parent, false)
        return RouteThemeHolder(view)
    }

    override fun onBindViewHolder(holder: RouteThemeHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

            itemView.setOnClickListener{
                if(selectCheck[position] == 0) {
                    selectCheck[position] = 1
                    itemClickListener.onClick(position, list[position].id, selectCheck)
                } else if(selectCheck[position] == 1){
                    selectCheck[position] = 0
                    itemClickListener.onClick(position, list[position].id, selectCheck)
                }
            }
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