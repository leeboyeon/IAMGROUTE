package com.ssafy.groute.src.main.home

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
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.dto.Review
import com.ssafy.groute.src.main.board.BoardRecyclerviewAdapter
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "ReviewAdapter"
class ReviewAdapter(var owner: LifecycleOwner, var context:Context) : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>(){
    var list = mutableListOf<PlaceReview>()
    inner class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val more = itemView.findViewById<ImageButton>(R.id.reviewItem_ib_more)
        fun bindInfo(data : PlaceReview){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                owner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(itemView.findViewById(R.id.review_iv_userimg))
                    itemView.findViewById<TextView>(R.id.review_tv_username).text = it.nickname
                    Log.d(TAG, "bindInfo: ${it.nickname}")
                }
            )
            itemView.findViewById<RatingBar>(R.id.review_rb_rating).rating = data.rate.toFloat()
            if(data.img != null){
                itemView.findViewById<ImageView>(R.id.review_iv_reviewimg).visibility = View.VISIBLE
                Glide.with(itemView)
                    .load("${ApplicationClass.IMGS_URL}${data.img}")
                    .into(itemView.findViewById(R.id.review_iv_reviewimg))
            }
            if(data.img == null || data.img == ""){
                itemView.findViewById<ImageView>(R.id.review_iv_reviewimg).visibility = View.GONE
            }
            itemView.findViewById<TextView>(R.id.review_tv_content).text = data.content
            more.isVisible = data.userId == ApplicationClass.sharedPreferencesUtil.getUser().id

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_placereview_item,parent,false)
        return ReviewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
//            itemView.setOnClickListener {
//                itemClickListener.onClick(it, position, list[position].id)
//            }
            more.setOnClickListener{
                val popup = PopupMenu(context,more)
                MenuInflater(context).inflate(R.menu.board_menu_item,popup.menu)
                popup.show()
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            modifyClickListener.onClick(bindingAdapterPosition)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_delete -> {
                            PlaceService().deletePlaceReview(list[position].id, DeleteCallback(position))
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

    inner class DeleteCallback(var position: Int):RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){
                Toast.makeText(context,"삭제되었습니다.",Toast.LENGTH_SHORT).show()
                list.removeAt(position)
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }

    }
}