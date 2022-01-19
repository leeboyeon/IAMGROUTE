package com.ssafy.groute.src.main.travel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TravelPlanListRecyclerviewAdapter_groute"
class TravelPlanListRecyclerviewAdapter(val context: Context, private val list: ArrayList<TravelPlan>) : RecyclerView.Adapter<TravelPlanListRecyclerviewAdapter.TravelPlanListHolder>(){

    inner class TravelPlanListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numTv = itemView.findViewById<TextView>(R.id.item_travelplan_num_tv)
        val placeTv = itemView.findViewById<TextView>(R.id.item_travelplan_day_list_place_tv)
        val locTv = itemView.findViewById<TextView>(R.id.item_travelplan_day_list_loc_tv)
        val dottedLine1 = itemView.findViewById<RelativeLayout>(R.id.item_travelplan_dotted_line1)
        val dottedLine2 = itemView.findViewById<RelativeLayout>(R.id.item_travelplan_dotted_line2)
        val removeTv = itemView.findViewById<TextView>(R.id.item_swipe_delete_tv)

        @SuppressLint("LongLogTag")
        fun bindInfo(data: TravelPlan, position: Int, flag: Int) {
            numTv.text = "${this.layoutPosition+1}"
            placeTv.text = "${data.title}"
            locTv.text = "${data.location}"

            // item의 위치에 따라 점선 보이거나 안보이거나 처리
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

            removeTv.setOnClickListener {
                removeData(this.layoutPosition)
                Toast.makeText(context, "삭제했습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "bindInfo: 리싸이클러뷰 아이템이 삭제되었습니다.")
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
                bindInfo(list[position], position,0)
            } else if(position == list.size-1){
                bindInfo(list[position], position, 1)
            } else {
                bindInfo(list[position], position, 2)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun removeData(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(list, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }
}