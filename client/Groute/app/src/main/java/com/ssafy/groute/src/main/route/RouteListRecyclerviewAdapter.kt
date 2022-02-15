package com.ssafy.groute.src.main.route

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ssafy.groute.databinding.ListItemRouteBinding
import com.ssafy.groute.src.dto.*

private const val TAG = "RouteListRvAd_Groute"
class RouteListRecyclerviewAdapter(var list: MutableList<UserPlan>, val planViewModel: PlanViewModel) : ListAdapter<UserPlan, RouteListRecyclerviewAdapter.RouteListHolder>(
    DiffCallback
){
//    var list = mutableListOf<UserPlan>()
    var selectLike: ArrayList<Int> = arrayListOf()
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

//    fun setRouteList(list: List<UserPlan>?) {
//        if(list == null) {
//            this.list = ArrayList()
//        } else {
//            this.list = list.toMutableList()!!
//            for(i in 0 until list.size) {
//                selectLike.add(0)
//            }
//        }
//    }

    inner class RouteListHolder(private val binding: ListItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {

        val routeDate = binding.itemRouteDateTv
        val heart = binding.itemRouteLikeIv

        fun bindInfo(data: UserPlan, position: Int) {
            binding.userPlan = data
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
//            val option2 = MultiTransformation(CenterCrop(), RoundedCorners(10))
//
//            if(imgUrl == "") {
//                routeImg.setImageResource(R.drawable.defaultimg)
//            } else {
//                Glide.with(itemView)
//                    .load("${ApplicationClass.IMGS_URL_PLACE}${imgUrl}")
//                    .apply(RequestOptions.bitmapTransform(option2))
//                    .into(routeImg)
//            }
            val tmp = BestRoute(imgUrl, "")
            binding.tmp = tmp
            binding.executePendingBindings()

//            routeDate.text = "12 March, 20"
            if(data.totalDate == 1) {
                routeDate.text = "당일치기"
            } else {
                routeDate.text = "${data.totalDate - 1}박 ${data.totalDate}일"
            }
//            routeArea.text = "[제주도]"
//            routeTitle.text = "${data.title}"
//            heartCnt.text = "${data.heartCnt}"
//            reviewCnt.text = "${data.reviewCnt}"

            heart.progress = 0F
            UserPlanService().planIsLike(PlanLike(userId, data.id), object : RetrofitCallback<Boolean> {
                override fun onError(t: Throwable) {
                    Log.d(TAG, "onError: 찜하기 여부 에러")
                }

                override fun onSuccess(code: Int, responseData: Boolean) {
                    if(responseData) {
                        heart.progress = 0.5F
                    } else {
                        heart.progress = 0F
                    }
                }
                override fun onFailure(code: Int) {
                    Log.d(TAG, "onFailure: ")
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_route, parent, false)
//        return RouteListHolder(view)
        return RouteListHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_route, parent, false))
    }

    override fun onBindViewHolder(holder: RouteListHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

            itemView.setOnClickListener{
                itemClickListener.onClick(position, list[position].id, list[position].totalDate)
            }

        }
    }

    override fun getItemId(position: Int): Long {
        return list.get(position).id.toLong()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(position: Int, id: Int, totalDate: Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }


    object DiffCallback : DiffUtil.ItemCallback<UserPlan>() {
        override fun areItemsTheSame(oldItem: UserPlan, newItem: UserPlan): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserPlan, newItem: UserPlan): Boolean {
            return oldItem.id == newItem.id
        }
    }

}