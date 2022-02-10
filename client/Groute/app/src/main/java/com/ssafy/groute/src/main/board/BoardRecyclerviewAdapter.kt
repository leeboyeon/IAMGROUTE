package com.ssafy.groute.src.main.board

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.dto.BoardDetail

import com.ssafy.groute.src.main.route.RouteThemeRecyclerviewAdapter
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import androidx.recyclerview.widget.ListAdapter
import com.ssafy.groute.databinding.ListItemBoardBinding
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.viewmodel.BoardViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

private const val TAG = "BoardRecyclerviewAdapte"
class BoardRecyclerviewAdapter(var lifecycleOwner: LifecycleOwner, var boardList: MutableList<BoardDetail>, var boardType: Int, var context:Context, val boardViewModel: BoardViewModel) : RecyclerView.Adapter<BoardRecyclerviewAdapter.BoardRecyclerHolder>(){
//class BoardRecyclerviewAdapter(var lifecycleOwner: LifecycleOwner, var boardList: MutableList<BoardDetail>, var boardType: Int, var context:Context)
//    : ListAdapter<BoardDetail, BoardRecyclerviewAdapter.BoardRecyclerHolder>(DiffCallback) {

    lateinit var ThemeAdapter: RouteThemeRecyclerviewAdapter

    var isEdit = false

    // 현재 로그인한 유저의 아이디
    val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

//    @JvmName("setBoardList1")
//    fun setBoardList(list: List<BoardDetail>?){
//        if (list == null) {
//            this.boardList = ArrayList()
//        } else {
//            this.boardList = list.toMutableList()!!
//            notifyDataSetChanged()
//        }
//    }

    inner class BoardRecyclerHolder(private var binding: ListItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        val profileIv = itemView.findViewById<ImageView>(R.id.item_board_profile_iv)
        val uidTv = itemView.findViewById<TextView>(R.id.item_board_uid_tv)
        val thumbnailIv = itemView.findViewById<ImageView>(R.id.item_board_iv)
        //val themeRv = itemView.findViewById<RecyclerView>(R.id.item_board_theme_rv)
        //val themeList = arrayListOf("#힐링", "#로맨틱")
        val more = itemView.findViewById<ImageButton>(R.id.boardDetail_ibtn_more)
        val likeBtn = binding.itemIvBoardHeartCnt

        fun bindInfo(data: BoardDetail) {
            more.isVisible = data.userId == ApplicationClass.sharedPreferencesUtil.getUser().id
            if(boardType == 2 && data.placeId > 0) {
                Log.d(TAG, "bindInfo_Place: ${data.placeId}")
                val placeInfo = PlaceService().getPlace(data.placeId)
                placeInfo.observe(lifecycleOwner, {
                    binding.place = it
                })
            }

            val userInfo = UserService().getUserInfo(data.userId)
            userInfo.observe(lifecycleOwner, {
                val tmpUser = User(it.id, it.nickname, it.img.toString())
                binding.user = tmpUser
//                    Glide.with(itemView)
//                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
//                        .circleCrop()
//                        .into(profileIv)
//                    uidTv.text = it.nickname
//                    Log.d(TAG, "bindInfo: ${it.nickname}")
            })

            binding.board = data
            binding.executePendingBindings()

//            if(data.img == null || data.img == ""){
//                thumbnailIv.visibility = View.GONE
//            }else{
//                thumbnailIv.visibility = View.VISIBLE
//                Glide.with(itemView)
//                    .load("${ApplicationClass.IMGS_URL}${data.img}")
//                    .into(thumbnailIv)
//            }

//            Log.d(TAG, "bindInfo_DATA: ${data}")
//            if(boardType == 1) {
//                //freeBoard
//                itemView.findViewById<LinearLayout>(R.id.locationLayout).visibility = View.GONE
//            } else if(boardType == 2) {
//
//                Log.d(TAG, "bindInfo_Place: ${data.placeId}")
//                val placeInfo = PlaceService().getPlace(data.placeId)
//                placeInfo.observe(lifecycleOwner, {
//
//                    itemView.findViewById<TextView>(R.id.item_board_tv_location).text = it.name
//                })
//
//            }


//            titleTv.text = data.title
//            contentTv.text = data.content
//            reviewTv.text = data.hitCnt.toString()
//            likeTv.text = data.heartCnt.toString()
//            ThemeAdapter = RouteThemeRecyclerviewAdapter(themeList)
//            themeRv.apply {
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                adapter = ThemeAdapter
//            }
            runBlocking {
                boardViewModel.getBoardPostIsLike(data.id, userId)
            }
            val res = boardViewModel.isBoardPostLike.value
            Log.d(TAG, "bindInfo: $res")
            if(res == true) {
                likeBtn.setColorFilter(context.resources.getColor(R.color.red))
            } else {
                likeBtn.setColorFilter(context.resources.getColor(R.color.black))
            }
//            BoardService().isBoardLike(data.id, userId, object : RetrofitCallback<Boolean> {
//                override fun onError(t: Throwable) {
//                    Log.d(TAG, "onError: 찜하기 여부 에러")
//                }
//
//                override fun onSuccess(code: Int, responseData: Boolean) {
//                    if(responseData) {
//                        likeBtn.setColorFilter(context.resources.getColor(R.color.red))
//                    } else {
//                        likeBtn.setColorFilter(context.resources.getColor(R.color.black))
//                    }
//                }
//                override fun onFailure(code: Int) {
//                    Log.d(TAG, "onFailure: ")
//                }
//            })

//            likeBtn.setOnClickListener{
//                itemClickListener.isLIke(it, layoutPosition, data.id)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardRecyclerHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)
//        return BoardHolder(view)
        return BoardRecyclerHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), R.layout.list_item_board, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BoardRecyclerviewAdapter.BoardRecyclerHolder, position: Int) {
        holder.apply {
            bindInfo(boardList[position])

            itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
            }

            more.setOnClickListener {
                val popup:PopupMenu = PopupMenu(context,more)
                MenuInflater(context).inflate(R.menu.board_menu_item, popup.menu)

                popup.show()
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            itemModifyListener.onClick(bindingAdapterPosition)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.menu_delete ->{
                            BoardService().deleteBoardDetail(boardList[position].id, DeleteCallback(position))
                            Log.d(TAG, "onBindViewHolder: ${boardList[position].id}")
                            return@setOnMenuItemClickListener true
                        } else -> {
                            return@setOnMenuItemClickListener false
                        }
                    }
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return boardList.get(position).id.toLong()
    }

    override fun getItemCount(): Int {
        return boardList.size
    }
    interface ItemClickListener{
        fun onClick(view:View, position: Int)
//        fun isLIke(view: View, position: Int, id:Int)
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
                runBlocking {
                    boardViewModel.getBoardDetail(boardList[position].id)
                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ${code}")
        }
    }

    lateinit var itemModifyListener: ItemModifyListener

    interface ItemModifyListener{
        fun onClick(position: Int)
    }
    fun setModifyClickListener(itemModifyListener: ItemModifyListener){
        this.itemModifyListener = itemModifyListener
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