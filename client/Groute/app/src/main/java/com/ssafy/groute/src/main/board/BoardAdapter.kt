package com.ssafy.groute.src.main.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardHolder>(){
    var list = mutableListOf<Board>()
    inner class BoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Board){
            Glide.with(itemView)
                .load(data.userImg)
                .into(itemView.findViewById(R.id.board_iv_userImg))

            itemView.findViewById<TextView>(R.id.board_tv_write_userNick).text = data.userNick
            itemView.findViewById<TextView>(R.id.board_tv_writeTitle).text = data.title
            itemView.findViewById<TextView>(R.id.board_tv_writeContent).text = data.content
            itemView.findViewById<TextView>(R.id.board_tv_writeDate).text = data.createDate
            itemView.findViewById<TextView>(R.id.board_tv_goodCnt).text = data.goodCnt.toString()
            itemView.findViewById<TextView>(R.id.board_tv_chatCnt).text = data.chatCnt.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_board_list_item,parent,false)
        return BoardHolder(view)
    }

    override fun onBindViewHolder(holder: BoardHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it, position, list[position].title)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}