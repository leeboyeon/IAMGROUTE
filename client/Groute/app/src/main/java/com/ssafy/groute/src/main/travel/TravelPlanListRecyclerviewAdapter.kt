package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R

class TravelPlanListRecyclerviewAdapter() : RecyclerView.Adapter<TravelPlanListRecyclerviewAdapter.TravelPlanListHolder>(){

    inner class TravelPlanListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numTv = itemView.findViewById<TextView>(R.id.item_travelplan_num_tv)
        val placeTv = itemView.findViewById<TextView>(R.id.item_travelplan_day_list_place_tv)
        val locTv = itemView.findViewById<TextView>(R.id.item_travelplan_day_list_loc_tv)
        val dottedLine1 = itemView.findViewById<RelativeLayout>(R.id.item_travelplan_dotted_line1)
        val dottedLine2 = itemView.findViewById<RelativeLayout>(R.id.item_travelplan_dotted_line2)

        fun bindInfo(position: Int, flag: Int) {
            numTv.text = "${position+1}"
            placeTv.text = "협재 해수욕장"
            locTv.text = "애월 한림"
            if(flag == 0) {
                dottedLine1.visibility = View.GONE
                dottedLine2.visibility = View.VISIBLE
            } else if(flag == 1){
                dottedLine1.visibility = View.VISIBLE
                dottedLine2.visibility = View.GONE
            } else if(flag == 2) {
                dottedLine1.visibility = View.VISIBLE
                dottedLine2.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelPlanListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travelplan_day_list, parent, false)
        return TravelPlanListHolder(view)
    }

    override fun onBindViewHolder(holder: TravelPlanListHolder, position: Int) {
        holder.apply {
            if(position == 0) {
                bindInfo(position, 0)
            } else if(position == 3){
                bindInfo(position, 1)
            } else {
                bindInfo(position, 2)
            }

        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}