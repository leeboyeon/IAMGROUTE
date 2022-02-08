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
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.dto.Theme
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "RouteListRecyclerviewAdapter"
class RouteListRecyclerviewAdapter(val planViewModel: PlanViewModel, val viewLifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RouteListRecyclerviewAdapter.RouteListHolder>(){
    var list = mutableListOf<UserPlan>()

    fun setRouteList(list: List<UserPlan>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }
    inner class RouteListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeImg = itemView.findViewById<ImageView>(R.id.item_route_iv)
        val routeDate = itemView.findViewById<TextView>(R.id.item_route_date_tv)
        val routeArea = itemView.findViewById<TextView>(R.id.item_route_area_tv)
        val routeTitle = itemView.findViewById<TextView>(R.id.item_route_title_tv)
        val reviewCnt = itemView.findViewById<TextView>(R.id.item_route_comment_tv)
        val heartCnt = itemView.findViewById<TextView>(R.id.item_route_like_tv)

        @SuppressLint("LongLogTag")
        fun bindInfo(data: UserPlan) {
            routeDate.text = "12 March, 20"
            if(data.totalDate == 1) {
                routeDate.text = "당일치기"
            } else {
                routeDate.text = "${data.totalDate - 1}박 ${data.totalDate}일"
            }
            routeArea.text = "[제주도]"
            routeTitle.text = "${data.title}"
            heartCnt.text = "${data.heartCnt}"
            reviewCnt.text = "${data.reviewCnt}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_route, parent, false)
        return RouteListHolder(view)
    }

    override fun onBindViewHolder(holder: RouteListHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])

            itemView.setOnClickListener{
                itemClickListener.onClick(position, list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(position: Int, id: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}