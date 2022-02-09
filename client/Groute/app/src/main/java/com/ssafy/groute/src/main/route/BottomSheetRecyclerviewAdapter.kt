package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Theme
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.CommonUtils
import kotlinx.coroutines.runBlocking

private const val TAG = "BottomSheetRecyclerviewAdapter"
class BottomSheetRecyclerviewAdapter(val viewLifecycleOwner: LifecycleOwner, val planViewModel: PlanViewModel) : RecyclerView.Adapter<BottomSheetRecyclerviewAdapter.BottomSheetRecyclerviewHolder>(){
    var list = mutableListOf<UserPlan>()

    @SuppressLint("LongLogTag")
    fun setUserPlanList(list: List<UserPlan>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            Log.d(TAG, "setUserPlanList: $list")
            this.list = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }
    inner class BottomSheetRecyclerviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planImg = itemView.findViewById<ImageView>(R.id.recycler_bottom_sheet_plan_img)
        val planTitle = itemView.findViewById<TextView>(R.id.recycler_bottom_sheet_plan_title)
        val planPeriod = itemView.findViewById<TextView>(R.id.recycler_bottom_sheet_plan_period)
        val planDaysRecyclerview = itemView.findViewById<RecyclerView>(R.id.recycler_bottom_sheet_plan_recyclerview)
        @SuppressLint("LongLogTag")
        fun bindInfo(data : UserPlan) {
            Glide.with(itemView)
                .load(R.drawable.jeju)
                .circleCrop()
                .into(planImg)
            planTitle.text = data.title
            planPeriod.text = CommonUtils.getFormattedDueDate(data.startDate,data.endDate)

            Log.d(TAG, "bindInfo: ${data.title}")
            var dayList = mutableListOf<Int>()
            for(i in 1..data.totalDate) {
                dayList.add(i)
            }
            var bottomSheetDaysRecyclerviewAdapter = BottomSheetDaysRecyclerviewAdapter()
            bottomSheetDaysRecyclerviewAdapter.setDayList(dayList)
            planDaysRecyclerview.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                adapter = bottomSheetDaysRecyclerviewAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetRecyclerviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bottom_sheet_userplan_item, parent, false)
        return BottomSheetRecyclerviewHolder(view)
    }

//    override fun getItemViewType(position: Int): Int {
//        return position
//    }

    override fun onBindViewHolder(holder: BottomSheetRecyclerviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}