package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Theme
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "BottomSheetDaysRecyclerviewAdapter"
class BottomSheetDaysRecyclerviewAdapter( val planViewModel: PlanViewModel, val context: Context) : RecyclerView.Adapter<BottomSheetDaysRecyclerviewAdapter.BottomSheetDaysRecyclerviewHolder>(){
    var list = mutableListOf<Int>()
    var selectCheck: ArrayList<Int> = arrayListOf()
    fun setDayList(list: List<Int>?) {
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
    inner class BottomSheetDaysRecyclerviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTxt = itemView.findViewById<TextView>(R.id.recycler_bottom_sheet_days_day)
        val dayBtn = itemView.findViewById<CardView>(R.id.recycler_bottom_sheet_days_btn)
        fun bindInfo(day : Int, position: Int) {
            dayTxt.text = "DAY$day"

            if(selectCheck[position] == 1) {
                dayBtn.background.setTint(ContextCompat.getColor(context, R.color.blue_700))
                dayTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                dayBtn.background.setTint(ContextCompat.getColor(context, R.color.grey))
                dayTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetDaysRecyclerviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bottom_sheet_days_item, parent, false)
        return BottomSheetDaysRecyclerviewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: BottomSheetDaysRecyclerviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

            var totalDate = planViewModel.planList.value!!.totalDate // 현재 내 일정에 담으려는 루트의 totalDate
            itemView.setOnClickListener{
                if(selectCheck[position] == 0) {
                    if(position + totalDate > list.size) { // 만약에 현재 day를 선택했을때 일정 기간을 초과하면 day끝까지 선택
                        for(i in 0 until list.size) {
                            if(selectCheck[i] == 1) {
                                selectCheck[i] = 0
                            }
                        }
                        for(i in position until list.size) {
                            selectCheck[i] = 1
                            Log.d(TAG, "onBindViewHolder: $i")
                        }
                        itemClickListener.onClick(position, list[position])
                    } else {
                        for(i in 0 until list.size) {
                            if(selectCheck[i] == 1) {
                                selectCheck[i] = 0
                            }

                        }
                        for(i in position until (position + totalDate)) {
                            selectCheck[i] = 1
                            Log.d(TAG, "onBindViewHolder: $i")
                        }
                        itemClickListener.onClick(position, list[position])
                    }

                } else if(selectCheck[position] == 1){
                    for(i in 0 until list.size) {
                        if(selectCheck[i] == 1) {
                            selectCheck[i] = 0
                        }

                    }
                    itemClickListener.onClick(position, 0)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(position: Int, day: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}