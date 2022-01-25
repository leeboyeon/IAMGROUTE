package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.util.BoardViewModel
import com.ssafy.groute.util.MainViewModel
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "BoardFragment"
class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding
    private lateinit var mainActivity: MainActivity
    private var magazineAdapter: MagazineAdapter = MagazineAdapter()
    lateinit var boardFreeAdapter : BoardAdapter
    lateinit var boardQuestionAdapter : BoardAdapter
    val magazines = arrayListOf<Magazine>()
    lateinit var userId: String
    private lateinit var boardViewModel: BoardViewModel

    companion object{
        const val BOARD_FREE_TYPE = 1 // 자유게시판 타입
        const val BOARD_QUESTION_TYPE = 2 // 질문게시판 타입
    }
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
        userId = ApplicationClass.sharedPreferencesUtil.getUser().id
        boardViewModel = ViewModelProvider(mainActivity).get(BoardViewModel::class.java)
        initAdapter()
        binding.boardTvMoreFree.setOnClickListener {
            Log.d(TAG, "onViewCreated: ")
            mainActivity.moveFragment(5, "boardId", BOARD_FREE_TYPE)
        }

        binding.boardTvMoreQuestion.setOnClickListener {
            Log.d(TAG, "onViewCreated: ")
            mainActivity.moveFragment(5, "boardId", BOARD_QUESTION_TYPE)
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

        //var boardFreeList = BoardService().getBoardDetailList(BOARD_FREE_TYPE)
//        boardFreeList.observe(
//            viewLifecycleOwner,
//            { boardFreeList ->
//                boardFreeList.let {
//                    boardFreeAdapter = BoardAdapter(requireContext(), viewLifecycleOwner)
//                    boardFreeAdapter.boardList = boardFreeList
//                }
//
//
//            }
//        )
        //Log.d(TAG, "initAdapter getBoardDetailList: $boardFreeList")
        boardFreeAdapter = BoardAdapter(requireContext(), viewLifecycleOwner)

        boardViewModel.boardFreeList.observe(viewLifecycleOwner, Observer { boardFreeAdapter.setBoardList(it) })
        boardViewModel.getBoardDetailList(BOARD_FREE_TYPE)
        binding.boardRvFree.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = boardFreeAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
                binding.boardRvFree.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = boardFreeAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                boardFreeAdapter.setItemClickListener(object: BoardAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, id: Int) {
                        mainActivity.moveFragment(6,"boardDetailId", boardFreeList[position].id)
                    }

                })
            }
        )

        val boardQuestionList = BoardService().getBoardDetailList(BOARD_QUESTION_TYPE)
        boardQuestionList.observe(
            viewLifecycleOwner,
            {   boardQuestionList ->
                boardQuestionList.let {
                    boardQuestionAdapter = BoardAdapter(viewLifecycleOwner, boardQuestionList)
                }
                binding.boardRvQuestion.apply{
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    adapter = boardQuestionAdapter
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                boardQuestionAdapter.setItemClickListener(object:BoardAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, id: Int) {
                        mainActivity.moveFragment(6,"boardDetailId", boardQuestionList[position].id)
                    }

                })
            }
        )
    }

    fun boardLike(boardDetailId: Int, userId: String) {
        BoardService().boardLike(boardDetailId, userId, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 게시판 찜하기 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                boardViewModel.boardFreeList.observe(viewLifecycleOwner, Observer { boardFreeAdapter.setBoardList(it) })
                boardViewModel.getBoardDetailList(BOARD_FREE_TYPE)
                boardViewModel.boardQuestionList.observe(viewLifecycleOwner, Observer { boardQuestionAdapter.setBoardList(it) })
                boardViewModel.getBoardDetailList(BOARD_QUESTION_TYPE)
                boardFreeAdapter.notifyDataSetChanged()
                boardQuestionAdapter.notifyDataSetChanged()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

}