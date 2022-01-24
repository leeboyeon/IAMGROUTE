package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService

private const val TAG = "BoardFragment"
class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding
    private lateinit var mainActivity: MainActivity
    private var magazineAdapter: MagazineAdapter = MagazineAdapter()
    lateinit var boardFreeAdapter : BoardAdapter
    lateinit var boardQuestionAdapter : BoardAdapter
    val boardFreeList = arrayListOf<BoardDetail>()
    val boardQuestionList = arrayListOf<BoardDetail>()

    val magazines = arrayListOf<Magazine>()
    val boards = arrayListOf<Board>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.boardTvMore.setOnClickListener {
            Log.d(TAG, "onViewCreated: ")
            mainActivity.moveFragment(5)
        }

    }
    fun initAdapter(){
        magazineAdapter = MagazineAdapter()

        magazines.apply {
            add(Magazine(img= R.drawable.jejucafe1, title="제주여행 꼭 들려야하는 카페 5곳", content=""))
            add(Magazine(img= R.drawable.jejucafe1, title="제주여행 꼭 들려야하는 카페 5곳", content=""))
            add(Magazine(img= R.drawable.jejucafe1, title="제주여행 꼭 들려야하는 카페 5곳", content=""))
            magazineAdapter.list = magazines
            magazineAdapter.notifyDataSetChanged()
        }
        binding.boardRvMagazine.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = magazineAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        }

        val boardList = BoardService().getBoardList()
        boardList.observe(
            viewLifecycleOwner,
            { boardList ->
                boardList.let {
                    Log.d(TAG, "initData boardList size : ${boardList.size}")
                    boardFreeList.clear()
                    boardQuestionList.clear()
                    for(i in 0 until boardList.size) {
                        if(boardList.get(i).boardId == 2) { // 자유게시판 글
                            boardFreeList.add(boardList.get(i))
                            Log.d(TAG, "initAdapter: ${boardList.get(i).title}")
                        } else if(boardList.get(i).boardId == 3) { // 질문게시판 글
                            boardQuestionList.add(boardList.get(i))
                            Log.d(TAG, "initAdapter: ${boardList.get(i).title}")
                        }
                    }
                }
                boardFreeAdapter = BoardAdapter(viewLifecycleOwner, boardFreeList)
                boardQuestionAdapter = BoardAdapter(viewLifecycleOwner, boardQuestionList)
                binding.boardRvFree.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = boardFreeAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                binding.boardRvQuestion.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = boardQuestionAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }

            }
        )
    }
}