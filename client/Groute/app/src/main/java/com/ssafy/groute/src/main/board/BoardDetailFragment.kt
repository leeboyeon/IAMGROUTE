package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentBoardDetailBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.MainViewModel
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.response.BoardDetailResponse
import com.ssafy.groute.src.service.BoardService

private const val TAG = "BoardDetailFragment"
class BoardDetailFragment : Fragment() {
    private lateinit var binding: FragmentBoardDetailBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var boardRecyclerAdapter:BoardRecyclerviewAdapter
    private var boardDetailList = mutableListOf<BoardDetail>()

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBoardDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        if(boardId == BOARD_FREE_TYPE) {
            binding.boardDetailBoardNameTv.text = "자유게시판"
        } else if(boardId == BOARD_QUESTION_TYPE) {
            binding.boardDetailBoardNameTv.text = "질문게시판"
        }
        initAdapter()
        //그냥 글쓰기
        binding.boardDetailBtnWrite.setOnClickListener {
            mainActivity.moveFragment(8,"boardId",boardId)
        }

    }
    fun initAdapter(){
        val boardDetailList = BoardService().getBoardDetailList(boardId)
        boardDetailList.observe(
            viewLifecycleOwner,
            {   boardDetailList ->
                boardDetailList.let {
                    boardRecyclerAdapter = BoardRecyclerviewAdapter(viewLifecycleOwner, boardDetailList, boardId, requireContext())
                }
                binding.boardDetailRvListitem.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = boardRecyclerAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                boardRecyclerAdapter.setModifyClickListener(object : BoardRecyclerviewAdapter.ItemModifyListener{
                    override fun onClick(position: Int) {
                        mainActivity.moveFragment(8,"boardDetailId",boardDetailList[position].id)
                    }

                })
                boardRecyclerAdapter.setItemClickListener(object:BoardRecyclerviewAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, name: String) {
                        mainActivity.moveFragment(6,"boardDetailId", boardDetailList[position].id)
                    }

                })
                Log.d(TAG, "initAdapter: ${boardRecyclerAdapter.isEdit}")

            }
        )


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
}