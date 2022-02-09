package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.databinding.FragmentBoardDetailBinding
import com.ssafy.groute.src.dto.Magazine
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.viewmodel.BoardViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import java.security.acl.Owner

private const val TAG = "BoardFragment_Groute"
class BoardFragment : BaseFragment<FragmentBoardBinding>(FragmentBoardBinding::bind, R.layout.fragment_board) {
//    lateinit var binding: FragmentBoardBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var magazineAdapter: MagazineAdapter
    private lateinit var boardAdapter : BoardAdapter

    val magazines = arrayListOf<Magazine>()

    lateinit var userId: String

    val boardViewModel : BoardViewModel by activityViewModels()

    companion object{
        const val BOARD_FREE_TYPE = 1 // 자유게시판 타입
        const val BOARD_QUESTION_TYPE = 2 // 질문게시판 타입
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardViewModels = boardViewModel

        initAdapter()

        userId = ApplicationClass.sharedPreferencesUtil.getUser().id

        binding.boardTvMoreFree.setOnClickListener {
            mainActivity.moveFragment(5, "boardId", BOARD_FREE_TYPE)
        }

        binding.boardTvMoreQuestion.setOnClickListener {
            mainActivity.moveFragment(5, "boardId", BOARD_QUESTION_TYPE)
        }
    }

    // 전체 adapter 초기화
    private fun initAdapter(){
        initDataBinding()
        initMagazineRecyclerview()
        initFreeRecyclerview()
        initQuestionRecyclerView()
    }

    // 데이터바인딩 초기화
    private fun initDataBinding() {
//        binding.boardViewModels = boardViewModel
    }

    // board 화면 상단 magazine recyclerView Init
    private fun initMagazineRecyclerview() {
        magazineAdapter = MagazineAdapter()

        magazines.apply {
            add(Magazine(img= R.drawable.jejucafe1, title="제주여행 꼭 들려야하는 카페 5곳", content=""))
            add(Magazine(img= R.drawable.jejuexcite1, title="제주여행 오늘은 어디갈까!", content=""))
            add(Magazine(img= R.drawable.jejushop2, title="SNS 핫플레이스 제주편", content=""))
            magazineAdapter.list = magazines
            magazineAdapter.notifyDataSetChanged()
        }

        binding.boardRvMagazine.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = magazineAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    // FreeBoard recyclerView Init
    private fun initFreeRecyclerview() {
        runBlocking {
            boardViewModel.getBoardPostList(BOARD_FREE_TYPE)
        }

        boardViewModel.freeBoardPostList.observe(viewLifecycleOwner,  {

            binding.boardRvFree.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            boardAdapter = BoardAdapter(it, requireContext(), viewLifecycleOwner)
            boardAdapter.setHasStableIds(true)
            binding.boardRvFree.adapter = boardAdapter

            boardAdapter.setItemClickListener(object: BoardAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(6,"boardDetailId", it[position].id)
                }
            })
//            boardAdapter.setLikeBtnClickListener(object : BoardAdapter.ItemClickListener {
//                override fun onClick(view: View, position: Int, id: Int) {
//                    boardLike(id, userId)
//                }
//            })
        })

    }

    // QnA Board recyclerView init
    private fun initQuestionRecyclerView() {
        runBlocking {
            boardViewModel.getBoardPostList(BOARD_QUESTION_TYPE)
        }

        boardViewModel.qnaBoardPostList.observe(viewLifecycleOwner, {
            binding.boardRvQuestion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            boardAdapter = BoardAdapter(it, requireContext(),viewLifecycleOwner)
            boardAdapter.setHasStableIds(true)
            binding.boardRvQuestion.adapter = boardAdapter

            boardAdapter.setItemClickListener(object:BoardAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(6,"boardDetailId", it[position].id)
                }
            })
//            boardAdapter.setLikeBtnClickListener(object : BoardAdapter.ItemClickListener {
//                override fun onClick(view: View, position: Int, id: Int) {
//                    boardLike(id, userId)
//                }
//            })
        })

    }


//    private fun boardLike(boardDetailId: Int, userId: String) {
//        BoardService().boardLike(boardDetailId, userId, object : RetrofitCallback<Any> {
//            override fun onError(t: Throwable) {
//                Log.d(TAG, "onError: 게시판 찜하기 에러")
//            }
//
//            override fun onSuccess(code: Int, responseData: Any) {
//                runBlocking {
//                    boardViewModel.getBoardPostList(BOARD_FREE_TYPE)
//                    boardViewModel.getBoardPostList(BOARD_QUESTION_TYPE)
//                }
////                boardViewModel.getBoardFreeListFive(viewLifecycleOwner)
////                boardViewModel.getBoardQuestionListFive(viewLifecycleOwner)
//            }
//
//            override fun onFailure(code: Int) {
//                Log.d(TAG, "onFailure: ")
//            }
//
//        })
//    }

}