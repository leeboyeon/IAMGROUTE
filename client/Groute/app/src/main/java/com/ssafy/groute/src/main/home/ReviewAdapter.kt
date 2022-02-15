package com.ssafy.groute.src.main.home

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
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewPlacereviewItemBinding
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "ReviewAdapter"
class ReviewAdapter(var owner: LifecycleOwner, var context:Context, val placeViewModel: PlaceViewModel) : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>(){
    var list = mutableListOf<PlaceReview>()
    inner class ReviewHolder(private var binding : RecyclerviewPlacereviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val more = itemView.findViewById<ImageButton>(R.id.reviewItem_ib_more)
        fun bindInfo(data : PlaceReview){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                owner, {
                    val user = User(it.id, it.nickname, it.img.toString())
                    binding.user = user
                }
            )
            binding.placeReview = data
            binding.executePendingBindings()

            more.isVisible = data.userId == ApplicationClass.sharedPreferencesUtil.getUser().id

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        return ReviewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_placereview_item, parent, false))
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])

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

    interface ItemClickListener {
        fun onClick(view: View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface ModifyClickListener{
        fun onClick(position: Int)
    }
    private lateinit var modifyClickListener: ModifyClickListener
    fun setModifyClickListener(modifyClickListener: ModifyClickListener) {
        this.modifyClickListener = modifyClickListener
    }

    inner class DeleteCallback(var position: Int):RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){
                runBlocking {
                    placeViewModel.getPlaceReviewListbyId(list[position].placeId)
                }
                Toast.makeText(context,"삭제되었습니다.",Toast.LENGTH_SHORT).show()
                list.removeAt(position)
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }

    }
}