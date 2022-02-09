package com.ssafy.groute.src.main.route

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Theme
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "BottomSheetDaysRecyclerviewAdapter"
class BottomSheetDaysRecyclerviewAdapter() : RecyclerView.Adapter<BottomSheetDaysRecyclerviewAdapter.BottomSheetDaysRecyclerviewHolder>(){
    var list = mutableListOf<Int>()

    fun setDayList(list: List<Int>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }
    inner class BottomSheetDaysRecyclerviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTxt = itemView.findViewById<TextView>(R.id.recycler_bottom_sheet_days_day)
        fun bindInfo(day : Int) {
            dayTxt.text = "DAY$day"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetDaysRecyclerviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bottom_sheet_days_item, parent, false)
        return BottomSheetDaysRecyclerviewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BottomSheetDaysRecyclerviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}