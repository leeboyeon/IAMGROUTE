package com.ssafy.groute.src.main.board

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.service.UserService

private const val TAG = "BoardAdapter_groute"
class BoardAdapter(var lifecycleOwner: LifecycleOwner, var boardList: List<BoardDetail>) : RecyclerView.Adapter<BoardAdapter.BoardHolder>(){
    inner class BoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : BoardDetail){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                lifecycleOwner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(itemView.findViewById(R.id.board_iv_userImg))
                    Log.d(TAG, "bindInfo : ${it.img}  ${it.nickname}")
                    itemView.findViewById<TextView>(R.id.board_tv_write_userNick).text = it.nickname
                }
            )
            itemView.findViewById<TextView>(R.id.board_tv_writeTitle).text = data.title
            itemView.findViewById<TextView>(R.id.board_tv_writeContent).text = data.content
            itemView.findViewById<TextView>(R.id.board_tv_writeDate).text = data.createDate
            itemView.findViewById<TextView>(R.id.board_tv_goodCnt).text = data.heartCnt.toString()
            itemView.findViewById<TextView>(R.id.board_tv_chatCnt).text = data.hitCnt.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_board_list_item,parent,false)
        return BoardHolder(view)
    }

    override fun onBindViewHolder(holder: BoardHolder, position: Int) {
        holder.apply {
            bindInfo(boardList[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, boardList[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}