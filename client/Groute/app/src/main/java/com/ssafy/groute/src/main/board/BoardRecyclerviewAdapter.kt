package com.ssafy.groute.src.main.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.route.RouteThemeRecyclerviewAdapter
import com.ssafy.groute.src.service.UserService
import de.hdodenhof.circleimageview.CircleImageView

class BoardRecyclerviewAdapter(var lifecycleOwner: LifecycleOwner, var boardList: List<BoardDetail>) : RecyclerView.Adapter<BoardRecyclerviewAdapter.BoardHolder>(){
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    inner class BoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileIv = itemView.findViewById<ImageView>(R.id.item_board_profile_iv)
        val uidTv = itemView.findViewById<TextView>(R.id.item_board_uid_tv)
        val thumbnailIv = itemView.findViewById<ImageView>(R.id.item_board_iv)
        val titleTv = itemView.findViewById<TextView>(R.id.item_board_title_tv)
        val contentTv = itemView.findViewById<TextView>(R.id.item_board_content_tv)
        val reviewTv = itemView.findViewById<TextView>(R.id.item_board_review_num_tv)
        val likeTv = itemView.findViewById<TextView>(R.id.item_board_like_num_tv)
        val themeRv = itemView.findViewById<RecyclerView>(R.id.item_board_theme_rv)
        val themeList = arrayListOf("#힐링", "#로맨틱")

        fun bindInfo(data: BoardDetail) {
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                lifecycleOwner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(profileIv)
                    uidTv.text = it.nickname
                }
            )
            Glide.with(itemView)
                .load(data.img)
                .into(thumbnailIv)
            titleTv.text = data.title
            contentTv.text = data.content
            reviewTv.text = data.hitCnt.toString()
            likeTv.text = data.heartCnt.toString()
            ThemeAdapter = RouteThemeRecyclerviewAdapter(themeList)
            themeRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ThemeAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)
        return BoardHolder(view)
    }

    override fun onBindViewHolder(holder: BoardHolder, position: Int) {
        holder.apply {
            bindInfo(boardList[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,"name")
            }
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }
    interface ItemClickListener{
        fun onClick(view:View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}