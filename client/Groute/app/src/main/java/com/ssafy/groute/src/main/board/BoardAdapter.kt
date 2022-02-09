package com.ssafy.groute.src.main.board

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import androidx.recyclerview.widget.ListAdapter
import com.ssafy.groute.databinding.RecyclerviewBoardListItemBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.main.home.PlaceFilterAdapter

private const val TAG = "BoardAdapter_groute"
//class BoardAdapter(val context: Context, var lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BoardAdapter.BoardHolder>(){
class BoardAdapter(var boardList : MutableList<BoardDetail>, val context: Context, var lifecycleOwner: LifecycleOwner)
    : ListAdapter<BoardDetail, BoardAdapter.BoardHolder>(DiffCallback) {

//    var boardList = mutableListOf<BoardDetail>()

//    fun setList(list: List<BoardDetail>?) {
//        if (list == null) {
//            this.boardList = ArrayList()
//        } else {
//            this.boardList = list.toMutableList()!!
//            notifyDataSetChanged()
//        }
//    }

    // 현재 로그인한 유저의 아이디
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

    inner class BoardHolder(private var binding: RecyclerviewBoardListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val goodBtn = itemView.findViewById<ImageView>(R.id.gooBtn)

        fun bindInfo(data : BoardDetail){
            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(lifecycleOwner, {
                var tmpUser = User(it.id, it.nickname, it.img.toString())
                binding.user = tmpUser
//                    Glide.with(itemView)
//                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
//                        .circleCrop()
//                        .into(itemView.findViewById(R.id.board_iv_userImg))
//                    itemView.findViewById<TextView>(R.id.board_tv_write_userNick).text = it.nickname
                }
            )

            binding.boardDetail = data
            binding.executePendingBindings()
//            itemView.findViewById<TextView>(R.id.board_tv_writeTitle).text = data.title
//            itemView.findViewById<TextView>(R.id.board_tv_writeContent).text = data.content
//            itemView.findViewById<TextView>(R.id.board_tv_writeDate).text = data.createDate
//            itemView.findViewById<TextView>(R.id.board_tv_goodCnt).text = data.heartCnt.toString()
//            itemView.findViewById<TextView>(R.id.board_tv_chatCnt).text = data.hitCnt.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder{
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_board_list_item,parent,false)
//        return BoardHolder(view)
        return BoardHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_board_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: BoardHolder, position: Int) {
        val data = boardList[position]
        holder.apply {
            bindInfo(data)

            BoardService().isBoardLike(data.id, userId, object : RetrofitCallback<Boolean> {
                override fun onError(t: Throwable) {
                    Log.d(TAG, "onError: 찜하기 여부 에러")
                }

                override fun onSuccess(code: Int, responseData: Boolean) {
                    if(responseData) {
                        goodBtn.setColorFilter(context.resources.getColor(R.color.blue_500))
                    } else {
                        goodBtn.setColorFilter(context.resources.getColor(R.color.black))
                    }
                }
                override fun onFailure(code: Int) {
                    Log.d(TAG, "onFailure: ")
                }
            })

            itemView.findViewById<TextView>(R.id.board_tv_writeTitle).setOnClickListener {
                itemClickListener.onClick(it, position, data.id)
            }

            itemView.findViewById<TextView>(R.id.board_tv_writeContent).setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, data.id)
            }

            goodBtn.setOnClickListener {
                goodBtnClickListener.onClick(it, layoutPosition, data.id)

            }
        }
    }

//    override fun getItemId(position: Int): Long {
//        return boardList.get(position).id.toLong()
//    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    private lateinit var goodBtnClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    fun setLikeBtnClickListener(itemClickListener: ItemClickListener) {
        this.goodBtnClickListener = itemClickListener
    }


    object DiffCallback : DiffUtil.ItemCallback<BoardDetail>() {
        override fun areItemsTheSame(oldItem: BoardDetail, newItem: BoardDetail): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BoardDetail, newItem: BoardDetail): Boolean {
            return oldItem.id == newItem.id
        }
    }
}