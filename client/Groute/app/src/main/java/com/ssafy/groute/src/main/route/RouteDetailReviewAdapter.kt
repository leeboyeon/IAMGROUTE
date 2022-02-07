package com.ssafy.groute.src.main.route

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.PlanReview
import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.src.dto.UserPlan
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "RouteDetailReviewAdapter"
class RouteDetailReviewAdapter(var owner: LifecycleOwner, var context: Context) : RecyclerView.Adapter<RouteDetailReviewAdapter.RouteDetailReviewHolder>(){
    var list = mutableListOf<PlanReview>()
    inner class RouteDetailReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImg = itemView.findViewById<ImageView>(R.id.routedetail_review_recycler_item_iv_userimg)
        val userName = itemView.findViewById<TextView>(R.id.routedetail_review_recycler_item_tv_username)
        val reviewImg = itemView.findViewById<ImageView>(R.id.routedetail_review_recycler_item_iv_reviewimg)
        val rating = itemView.findViewById<RatingBar>(R.id.routedetail_review_recycler_item_rb_rating)
        val content = itemView.findViewById<TextView>(R.id.routedetail_review_recycler_item_tv_content)
        val more = itemView.findViewById<ImageButton>(R.id.routedetail_review_recycler_item_ib_more)

        fun bindInfo(data: PlanReview) {
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                owner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(userImg)
                    userName.text = it.nickname
                }
            )
            rating.rating = data.rate.toFloat()
            if(data.img != null){
                reviewImg.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL_PLACEREVIEW}${data.img}")
                    .into(reviewImg)
            }
            if(data.img == null || data.img == ""){
                reviewImg.visibility = View.GONE
            }
            content.text = data.content
            more.isVisible = data.userId == ApplicationClass.sharedPreferencesUtil.getUser().id

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_routedetail_review_item, parent, false)
        return RouteDetailReviewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteDetailReviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])

            more.setOnClickListener{
                val popup = PopupMenu(context, more)
                MenuInflater(context).inflate(R.menu.board_menu_item,popup.menu)
                popup.show()
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            modifyClickListener.onClick(bindingAdapterPosition)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_delete -> {
                            UserPlanService().deletePlanReview(list[position].id, DeleteCallback(position))
                            return@setOnMenuItemClickListener true
                        }
                        else ->{
                            return@setOnMenuItemClickListener false
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    interface ModifyClickListener{
        fun onClick(position: Int)
    }
    private lateinit var modifyClickListener: ModifyClickListener
    fun setModifyClickListener(modifyClickListener: ModifyClickListener){
        this.modifyClickListener = modifyClickListener
    }

    @SuppressLint("LongLogTag")
    inner class DeleteCallback(var position: Int): RetrofitCallback<Boolean> {

        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){
                Toast.makeText(context,"삭제되었습니다.", Toast.LENGTH_SHORT).show()
                list.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }

    }
}