package com.ssafy.groute.src.main.my

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewSharedtravelItemBinding
import com.ssafy.groute.src.dto.BestRoute
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class SharedTravelAdapter(var list : MutableList<UserPlan>, val planViewModel: PlanViewModel)  : RecyclerView.Adapter<SharedTravelAdapter.SharedHolder>(){
//    var list = mutableListOf<UserPlan>()

//    fun setShareTravelList(list: List<UserPlan>?) {
//        if(list == null) {
//            this.list = ArrayList()
//        } else {
//            this.list = list.toMutableList()!!
//            notifyDataSetChanged()
//        }
//    }

    inner class SharedHolder(private val binding:RecyclerviewSharedtravelItemBinding) : RecyclerView.ViewHolder(binding.root){
        val routeImg = itemView.findViewById<ImageView>(R.id.home_best_img)
        fun bindInfo(data : UserPlan){
            runBlocking {
                planViewModel.getPlanById(data.id, 2)
            }
            var imgUrl = ""
            for(i in 0 until planViewModel.routeList.value!!.size) {
                for(j in 0 until planViewModel.routeList.value!!.get(i).routeDetailList.size) {
                    val type = planViewModel.routeList.value!!.get(i).routeDetailList.get(j).place.type
                    if(type == "관광지" || type == "레포츠" || type == "문화시설") {
                        imgUrl = planViewModel.routeList.value!!.get(i).routeDetailList.get(j).place.img
                        break
                    }
                }
            }
            val tmp = BestRoute(imgUrl, data.title)
            binding.tmp = tmp
            binding.executePendingBindings()
//            if(imgUrl == "") {
//                routeImg.setImageResource(R.drawable.defaultimg)
//            } else {
//                Glide.with(itemView)
//                    .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
//                    .into(routeImg)
//            }
//            itemView.findViewById<TextView>(R.id.home_best_title).text = "[제주도] ${data.title}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_bestroute_item,parent,false)
//        return SharedHolder(view)
        return SharedHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_sharedtravel_item, parent, false))
    }

    override fun onBindViewHolder(holder: SharedHolder, position: Int) {
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