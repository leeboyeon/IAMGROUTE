package com.ssafy.groute.src.main.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.main.route.RouteThemeRecyclerviewAdapter
import de.hdodenhof.circleimageview.CircleImageView

class BoardRecyclerviewAdapter(val context: Context) : RecyclerView.Adapter<BoardRecyclerviewAdapter.BoardHolder>(){
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    inner class BoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileIv = itemView.findViewById<CircleImageView>(R.id.item_board_profile_iv)
        val uidTv = itemView.findViewById<TextView>(R.id.item_board_uid_tv)
        val thumbnailIv = itemView.findViewById<ImageView>(R.id.item_board_iv)
        val titleTv = itemView.findViewById<TextView>(R.id.item_board_title_tv)
        val contentTv = itemView.findViewById<TextView>(R.id.item_board_content_tv)
        val reviewTv = itemView.findViewById<TextView>(R.id.item_board_review_num_tv)
        val likeTv = itemView.findViewById<TextView>(R.id.item_board_like_num_tv)
        val themeRv = itemView.findViewById<RecyclerView>(R.id.item_board_theme_rv)
        val themeList = arrayListOf("#힐링", "#로맨틱")

        fun bindInfo() {
            profileIv.setBackgroundResource(R.drawable.profile)
            uidTv.text = "Ponyo"
            thumbnailIv.setBackgroundResource(R.drawable.flower)
            titleTv.text = "유채꽃밭이 너무 아름다웠던 제주~!!!"
            contentTv.text = "제주도 성산유채꽃밭이 유명하다는 얘기를 듣고 다녀왔습니다. 너무 아름다웠고 나중에 기회가 된다면 또 가고싶네요! 연인과의 유채꽃밭 데이트 추천합니다ㅎㅎ"
            reviewTv.text = "22"
            likeTv.text = "56"
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
            bindInfo()
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,"name")
            }
        }
    }

    override fun getItemCount(): Int {
        return 6
    }
    interface ItemClickListener{
        fun onClick(view:View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}