package com.ssafy.groute.src.main.my

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.CommonUtils
import kotlinx.coroutines.runBlocking

class MyTravelAdapter(val planViewModel: PlanViewModel) : RecyclerView.Adapter<MyTravelAdapter.MyTravelHolder>(){
    var list = mutableListOf<UserPlan>()
    inner class MyTravelHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val routeImg = itemView.findViewById<ImageView>(R.id.mytravel_iv_img)
        fun bindInfo(data:UserPlan){
            runBlocking {
                planViewModel.getPlanById(data.id, false)
            }
            var imgUrl = ""
            for(i in 0 until planViewModel.routeList.value!!.size) {
                for(j in 0 until planViewModel.routeList.value!!.get(i).routeDetailList.size) {
                    var type = planViewModel.routeList.value!!.get(i).routeDetailList.get(j).place.type
                    if(type == "관광지" || type == "레포츠" || type == "문화시설") {
                        imgUrl = planViewModel.routeList.value!!.get(i).routeDetailList.get(j).place.img
                        break
                    }
                }
            }
            if(imgUrl == "") {
                routeImg.setImageResource(R.drawable.defaultimg)
            } else {
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
                    .into(routeImg)
            }

            itemView.findViewById<TextView>(R.id.mytravel_tv_title).text = data.title
            itemView.findViewById<TextView>(R.id.mytravel_tv_due).text = CommonUtils.getFormattedDueDate(data.startDate,data.endDate)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTravelHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_travleing_item,parent,false)
        return MyTravelHolder(view)
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