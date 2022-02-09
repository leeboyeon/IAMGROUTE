package com.ssafy.groute.src.main.route

import android.animation.ValueAnimator
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
import com.airbnb.lottie.LottieAnimationView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ssafy.groute.src.dto.*
import com.ssafy.groute.src.main.home.PlaceFilterAdapter

private const val TAG = "RouteListRecyclerviewAdapter"
class RouteListRecyclerviewAdapter(val planViewModel: PlanViewModel, val viewLifecycleOwner: LifecycleOwner) : ListAdapter<UserPlan, RouteListRecyclerviewAdapter.RouteListHolder>(
    DiffCallback
){
    var list = mutableListOf<UserPlan>()
    var selectLike: ArrayList<Int> = arrayListOf()
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

    fun setRouteList(list: List<UserPlan>?) {
        if(list == null) {
            this.list = ArrayList()
        } else {
            this.list = list.toMutableList()!!
            for(i in 0 until list.size) {
                selectLike.add(0)
            }
        }
    }
    inner class RouteListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeImg = itemView.findViewById<ImageView>(R.id.item_route_iv)
        val routeDate = itemView.findViewById<TextView>(R.id.item_route_date_tv)
        val routeArea = itemView.findViewById<TextView>(R.id.item_route_area_tv)
        val routeTitle = itemView.findViewById<TextView>(R.id.item_route_title_tv)
        val reviewCnt = itemView.findViewById<TextView>(R.id.item_route_comment_tv)
        val heartCnt = itemView.findViewById<TextView>(R.id.item_route_like_tv)
        val heart = itemView.findViewById<LottieAnimationView>(R.id.item_route_like_iv)

        @SuppressLint("LongLogTag")
        fun bindInfo(data: UserPlan, position: Int) {
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


            heart.setOnClickListener{

                if(heart.progress > 0F){
                    heartClickListener.onClick(it, position, data.id)
                    Log.d(TAG, "onBindViewHolder: 이미 클릭됨 ")
                    heart.pauseAnimation()
                    heart.progress = 0F

                }else{
                    heartClickListener.onClick(it, position, data.id)
                    Log.d(TAG, "onBindViewHolder: 클릭할거얌 ")
                    val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
                    animator.addUpdateListener { animation ->
                        heart.progress = animation.animatedValue as Float
                    }
                    animator.start()

                }
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_route, parent, false)
        return RouteListHolder(view)
    }

    @SuppressLint("LongLogTag")
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

    interface HeartClickListener{
        fun onClick(view:View, position: Int, planId: Int)
    }
    private lateinit var heartClickListener : HeartClickListener
    fun setHeartClickListener(heartClickListener: HeartClickListener){
        this.heartClickListener = heartClickListener
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