package com.ssafy.groute.src.main.my

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewTravleingItemBinding
import com.ssafy.groute.src.dto.BestRoute
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.CommonUtils
import kotlinx.coroutines.runBlocking

class MyTravelAdapter(val planViewModel: PlanViewModel) : RecyclerView.Adapter<MyTravelAdapter.MyTravelHolder>(){
    var list = mutableListOf<UserPlan>()
    inner class MyTravelHolder(private val binding: RecyclerviewTravleingItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindInfo(data:UserPlan){
            runBlocking {
                planViewModel.getPlanById(data.id, 2)
            }
            var imgUrl = ""
//            for(i in 0 until planViewModel.routeList.value!!.size) {
//                for(j in 0 until planViewModel.routeList.value!!.get(i).routeDetailList.size) {
//                    var type = planViewModel.routeList.value!!.get(i).routeDetailList.get(j).place.type
//                    if(type == "관광지" || type == "레포츠" || type == "문화시설") {
//                        imgUrl = planViewModel.routeList.value!!.get(i).routeDetailList.get(j).place.img
//                        break
//                    }
//                }
//            }

            binding.mytravelTvDue.text = CommonUtils.getFormattedDueDate(data.startDate, data.endDate)

            val tmp = BestRoute(imgUrl, data.title)
            binding.tmp = tmp
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTravelHolder {
        return MyTravelHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_travleing_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyTravelHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ItemClickListener{
        fun onClick(view: View, position: Int, id: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}