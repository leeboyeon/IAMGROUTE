package com.ssafy.groute.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class BestRouteAdapter(var list: MutableList<UserPlan>, val planViewModel: PlanViewModel) : RecyclerView.Adapter<BestRouteAdapter.BestRouteHolder>(){

    inner class BestRouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val routeImg = itemView.findViewById<ImageView>(R.id.home_best_img)
        fun bindInfo(data : UserPlan){
            runBlocking {
                planViewModel.getPlanById(data.id, 2)
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
            var option2 = MultiTransformation(CenterCrop(),RoundedCorners(10))
            if(imgUrl == "") {
                routeImg.setImageResource(R.drawable.defaultimg)
            } else {
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
                    .apply(RequestOptions.bitmapTransform(option2))
                    .into(routeImg)
            }
            itemView.findViewById<TextView>(R.id.home_best_title).text = "[제주도] ${data.title}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRouteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bestroute_item,parent,false)
        return BestRouteHolder(view)
    }

    override fun onBindViewHolder(holder: BestRouteHolder, position: Int) {
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