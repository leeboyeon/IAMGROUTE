package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardDetailBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.MainViewModel
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.viewmodel.BoardViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "BoardDetailF_Groute"
class BoardDetailFragment : BaseFragment<FragmentBoardDetailBinding>(FragmentBoardDetailBinding::bind, R.layout.fragment_board_detail) {
//    private lateinit var binding: FragmentBoardDetailBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var boardRecyclerAdapter: BoardRecyclerviewAdapter
    private var boardDetailList = mutableListOf<BoardDetail>()
    val boardViewModel: BoardViewModel by activityViewModels()
    lateinit var userId: String
    private var boardId = -1
    private var boardDetailId = -1
    private var viewModel: MainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        arguments?.let {
            boardId = it.getInt("boardId", -1)
            Log.d(TAG, "onCreate: $boardId")
        }
        mainActivity.hideBottomNav(true)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        userId = ApplicationClass.sharedPreferencesUtil.getUser().id
        if(boardId == BOARD_FREE_TYPE) {
            binding.boardDetailBoardNameTv.text = "자유게시판"
        } else if(boardId == BOARD_QUESTION_TYPE) {
            binding.boardDetailBoardNameTv.text = "질문게시판"
        }
        initAdapter()

        //그냥 글쓰기
        binding.boardDetailBtnWrite.setOnClickListener {
            Log.d(TAG, "onViewCreated: ${boardId}")
            mainActivity.moveFragment(8,"boardId",boardId)
        }

        binding.backBtn.setOnClickListener {
            Log.d(TAG, "onViewCreated: CLICK")
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }

    }
    fun initViewModel(id : Int){
//        boardViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)

        if(id == 1){
            boardViewModel.freeBoardPostList.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    boardRecyclerAdapter.setBoardList(it)
                    boardRecyclerAdapter.notifyDataSetChanged()
                }
                boardRecyclerAdapter.setItemClickListener(object:BoardRecyclerviewAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, name: String) { // BoardPostDetailFragment
                        mainActivity.moveFragment(6,"boardDetailId", it[position].id)
                    }

                    override fun isLIke(view: View, position: Int, id: Int) {
                        boardLike(id, userId)
                    }
                })
                boardRecyclerAdapter.setModifyClickListener(object : BoardRecyclerviewAdapter.ItemModifyListener{   // BoardWriteFragment
                    override fun onClick(position: Int) {
                        mainActivity.moveFragment(8,"boardDetailId", it[position].id)
                    }

                })
            })
        } else if(id == 2){
            boardViewModel.qnaBoardPostList.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    boardRecyclerAdapter.setBoardList(it)
                    boardRecyclerAdapter.notifyDataSetChanged()
                }

                boardRecyclerAdapter.setItemClickListener(object:BoardRecyclerviewAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, name: String) {
                        mainActivity.moveFragment(6,"boardDetailId", it[position].id)
                    }
                    override fun isLIke(view: View, position: Int, id: Int) {
                        boardLike(id, userId)
                    }
                })

                boardRecyclerAdapter.setModifyClickListener(object : BoardRecyclerviewAdapter.ItemModifyListener{
                    override fun onClick(position: Int) {
                        mainActivity.moveFragment(8,"boardDetailId",it[position].id)
                    }
                })
            })
        }

    }
    fun initAdapter(){

        binding.boardDetailRvListitem.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        boardRecyclerAdapter = BoardRecyclerviewAdapter(viewLifecycleOwner, boardDetailList, boardId, requireContext())
        boardRecyclerAdapter.setHasStableIds(true)
        binding.boardDetailRvListitem.adapter = boardRecyclerAdapter


        initViewModel(boardId)

    }
    fun refreshFragment(){
        val ft:FragmentTransaction = requireFragmentManager().beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    fun boardLike(boardDetailId: Int, userId: String) {
        BoardService().boardLike(boardDetailId, userId, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 게시판 찜하기 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                Log.d(TAG, "onSuccess: BoardDetail 찜하기 성공")

                if(boardId == BOARD_FREE_TYPE) {
                    runBlocking {
                        boardViewModel.getBoardPostList(boardId)
                    }
                    boardRecyclerAdapter.setBoardList(boardViewModel.freeBoardPostList.value)
//                    boardViewModel.getBoardFreeList(viewLifecycleOwner)
//                    boardRecyclerAdapter.setBoardList(boardViewModel.boardFreeList.value)
//                    boardRecyclerAdapter.notifyDataSetChanged()
                            //Log.d(TAG, "onSuccess: ${it}")

                } else if(boardId == BOARD_QUESTION_TYPE) {
                    runBlocking {
                        boardViewModel.getBoardPostList(boardId)
                    }
                    boardRecyclerAdapter.setBoardList(boardViewModel.qnaBoardPostList.value)

//                    boardViewModel.getBoardQuestionList(viewLifecycleOwner)
//                    boardRecyclerAdapter.setBoardList(boardViewModel.boardQuestionList.value)
//                    boardRecyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    companion object {
        @JvmStatic
        fun newInstance(key: String, value: Int) =
            BoardDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
    override fun onResume() {
        super.onResume()
        initAdapter()
        refreshFragment()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
}