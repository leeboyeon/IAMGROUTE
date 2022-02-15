package com.ssafy.groute.src.main.route

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewRoutedetailReviewItemBinding
import com.ssafy.groute.src.dto.PlanReview
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "RDReviewAdapter"
class RouteDetailReviewAdapter(var owner: LifecycleOwner, var context: Context) : RecyclerView.Adapter<RouteDetailReviewAdapter.RouteDetailReviewHolder>(){
    var list = mutableListOf<PlanReview>()
    inner class RouteDetailReviewHolder(private val binding : RecyclerviewRoutedetailReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val more = binding.routeDetailReviewRecyclerItemIbMore

        fun bindInfo(data: PlanReview) {
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                owner, {
                    val user = User(it.id, it.nickname, it.img.toString())
                    binding.user = user
                }
            )
            binding.planReview = data
            binding.executePendingBindings()

            more.isVisible = data.userId == ApplicationClass.sharedPreferencesUtil.getUser().id

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDetailReviewHolder {
        return RouteDetailReviewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_routedetail_review_item, parent, false))
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