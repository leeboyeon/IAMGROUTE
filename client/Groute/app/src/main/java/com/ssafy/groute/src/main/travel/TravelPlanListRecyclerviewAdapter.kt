package com.ssafy.groute.src.main.travel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.ItemTravelplanDayListBinding
import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TPlanListRvAd_Groute"
class TravelPlanListRecyclerviewAdapter(val context: Context,var list:MutableList<Route>,var planViewModel: PlanViewModel,val owner:LifecycleOwner,var planId:Int) : RecyclerView.Adapter<TravelPlanListRecyclerviewAdapter.TravelPlanListHolder>(),
    Filterable {
    private var dayFilterList = list
    private var route:Route = Route()
    var routeDetailList = mutableListOf<RouteDetail>()

    inner class TravelPlanListHolder(private val binding: ItemTravelplanDayListBinding) : RecyclerView.ViewHolder(binding.root) {

        val dottedLine1 = binding.itemTravelplanDottedLine1
        val dottedLine2 = binding.itemTravelplanDottedLine2

        fun bindInfo(data: RouteDetail, position: Int, flag: Int) {
            binding.routeDetail = data
            binding.executePendingBindings()

            binding.itemTravelplanNumTv.text = "${this.layoutPosition + 1}"


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

            binding.itemSwipeDeleteTv.setOnClickListener {
                Log.d(TAG, "bindInfo: ${this.layoutPosition}, $data")
                removeData(this.layoutPosition)
                Toast.makeText(context, "삭제했습니다.", Toast.LENGTH_SHORT).show()
            }

            binding.itemSwipeMemoTv.setOnClickListener {
                memoClickListener.onClick(it,position,routeDetailList[position].placeId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelPlanListHolder {
        return TravelPlanListHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_travelplan_day_list, parent, false))
    }

    override fun onBindViewHolder(holder: TravelPlanListHolder, position: Int) {
        holder.apply {
            if(position == 0) {
                bindInfo(routeDetailList[position], position,0)
            } else if(position == routeDetailList.size-1){
                bindInfo(routeDetailList[position], position, 1)
            } else {
                bindInfo(routeDetailList[position], position, 2)
            }
        }
    }

    override fun getItemCount(): Int {
        return routeDetailList.size
    }

    fun removeData(position: Int) {
        routeDetailList.removeAt(position)
        notifyItemRemoved(position)
    }


    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(routeDetailList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)

        val detailList = arrayListOf<RouteDetail>()
        for(i in 0..routeDetailList.size-1){
            val details = RouteDetail(
                routeDetailList[i].id,
                i+1
            )
            detailList.add(details)
        }
        UserPlanService().updatePriority(detailList, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: Update Success")
                runBlocking {
                    planViewModel.getPlanById(planId, 2)
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
        swapListener.onSwap(fromPos, toPos)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                dayFilterList = if(charString.isEmpty()){
                    list
                }else{
                    var result = Route()
                    val resultList = ArrayList<Route>()

                    val size = list.size
                    for(item in 0..size-1){
                        if(list[item].day == charString.toInt()){

                            result = list[item]
                            route = result
                            routeDetailList = result.routeDetailList.toMutableList()
                        }
                    }
                    resultList
                }
                val filteredResult = FilterResults()
                filteredResult.values = dayFilterList
                return filteredResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dayFilterList = mutableListOf()
                notifyDataSetChanged()
            }

        }
    }

    interface SwapListener{
        fun onSwap(fromPos: Int, toPos: Int)
    }

    private lateinit var swapListener: SwapListener
    fun setSwapListener(swapListener: SwapListener) {
        this.swapListener = swapListener
    }

    interface MemoClickListener {
        fun onClick(view:View, position: Int,placeId:Int)
    }

    private lateinit var memoClickListener : MemoClickListener

    fun setMemoClickListener(memoClickListener: MemoClickListener) {
        this.memoClickListener = memoClickListener
    }
}