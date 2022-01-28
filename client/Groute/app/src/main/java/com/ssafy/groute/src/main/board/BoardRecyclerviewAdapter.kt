package com.ssafy.groute.src.main.board

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.route.RouteThemeRecyclerviewAdapter
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "BoardRecyclerviewAdapte"
class BoardRecyclerviewAdapter(var lifecycleOwner: LifecycleOwner, var boardList: MutableList<BoardDetail>, var boardType: Int, var context:Context) : RecyclerView.Adapter<BoardRecyclerviewAdapter.BoardHolder>(){
    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter
    var isEdit = false

    @JvmName("setBoardList1")
    fun setBoardList(list: List<BoardDetail>){
        if (list == null) {
            this.boardList = ArrayList()
        } else {
            this.boardList = list.toMutableList()!!
            notifyDataSetChanged()
        }
    }
    inner class BoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileIv = itemView.findViewById<ImageView>(R.id.item_board_profile_iv)
        val uidTv = itemView.findViewById<TextView>(R.id.item_board_uid_tv)
        val thumbnailIv = itemView.findViewById<ImageView>(R.id.item_board_iv)
        val titleTv = itemView.findViewById<TextView>(R.id.item_board_title_tv)
        val contentTv = itemView.findViewById<TextView>(R.id.item_board_content_tv)
        val reviewTv = itemView.findViewById<TextView>(R.id.item_board_review_num_tv)
        val likeTv = itemView.findViewById<TextView>(R.id.item_board_like_num_tv)
        //val themeRv = itemView.findViewById<RecyclerView>(R.id.item_board_theme_rv)
        //val themeList = arrayListOf("#힐링", "#로맨틱")
        val more = itemView.findViewById<ImageButton>(R.id.boardDetail_ibtn_more)

        fun bindInfo(data: BoardDetail) {
            if(data.img == null || data.img == ""){
                thumbnailIv.visibility = View.GONE
            }else{
                thumbnailIv.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load(data.img)
                    .into(thumbnailIv)
            }
            if(boardType == 1) {
                //freeBoard
                itemView.findViewById<LinearLayout>(R.id.locationLayout).visibility = View.GONE
            } else if(boardType == 2) {
                itemView.findViewById<TextView>(R.id.item_board_tv_location).text = data.placeId.toString()
            }
            Log.d(TAG, "bindInfo: ${data.userId}")
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(
                lifecycleOwner, {
                    Glide.with(itemView)
                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                        .circleCrop()
                        .into(profileIv)
                    uidTv.text = it.nickname
                    Log.d(TAG, "bindInfo: ${it.nickname}")
                }
            )
            titleTv.text = data.title
            contentTv.text = data.content
            reviewTv.text = data.hitCnt.toString()
            likeTv.text = data.heartCnt.toString()
//            ThemeAdapter = RouteThemeRecyclerviewAdapter(themeList)
//            themeRv.apply {
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                adapter = ThemeAdapter
//            }




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

            more.setOnClickListener {
                val popup:PopupMenu = PopupMenu(context,more)
                MenuInflater(context).inflate(R.menu.board_menu_item, popup.menu)

                popup.show()
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {

                            itemmodifyListener.onClick(bindingAdapterPosition)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_delete ->{
                            BoardService().deleteBoardDetail(boardList[position].id, DeleteCallback(position))
                            Log.d(TAG, "onBindViewHolder: ${boardList[position].id}")
                            return@setOnMenuItemClickListener true
                        }else->{
                        return@setOnMenuItemClickListener false
                    }
                    }
                }
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
    inner class DeleteCallback(var position:Int):RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){
                Toast.makeText(context,"삭제되었습니다.",Toast.LENGTH_SHORT).show()
                boardList.removeAt(position)
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }

    }
    lateinit var itemmodifyListener: ItemModifyListener
    interface ItemModifyListener{
        fun onClick(position: Int)
    }
    fun setModifyClickListener(itemModifyListener: ItemModifyListener){
        this.itemmodifyListener = itemModifyListener
    }

}