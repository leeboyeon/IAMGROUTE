package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.src.dto.Magazine
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.util.BoardViewModel
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
    var boardViewModel: BoardViewModel = BoardViewModel()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        userId = ApplicationClass.sharedPreferencesUtil.getUser().id
        binding.boardTvMoreFree.setOnClickListener {
            Log.d(TAG, "onViewCreated: ")
            mainActivity.moveFragment(5, "boardId", BOARD_FREE_TYPE)
        }

        binding.boardTvMoreQuestion.setOnClickListener {
            Log.d(TAG, "onViewCreated: ")
            mainActivity.moveFragment(5, "boardId", BOARD_QUESTION_TYPE)
        }
    }
    fun initViewModels(){
        boardViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        binding.lifecycleOwner = this
        binding.boardViewModels = boardViewModel
    }
    fun initMagazineRecyclerview(){
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
    }
    //
    fun initFreeRecyclerview(){
        boardViewModel.getBoardFreeListFive(this)
        binding.boardRvFree.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        boardFreeAdapter = BoardAdapter(requireContext(),viewLifecycleOwner)
        boardFreeAdapter.setHasStableIds(true)
        binding.boardRvFree.adapter = boardFreeAdapter

        boardFreeAdapter.setItemClickListener(object: BoardAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, id: Int) {
                mainActivity.moveFragment(6,"boardDetailId", boardViewModel.boardFreeList.value!!.get(position).id)
            }
        })

        boardFreeAdapter.setLikeBtnClickListener(object : BoardAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, id: Int) {
                boardLike(id, userId)
            }
        })
    }
    fun initQuestionRecyclerView(){
        boardViewModel.getBoardQuestionListFive(this)
        binding.boardRvQuestion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        boardQuestionAdapter = BoardAdapter(requireContext(),viewLifecycleOwner)
        boardQuestionAdapter.setHasStableIds(true)
        binding.boardRvQuestion.adapter = boardQuestionAdapter

        boardQuestionAdapter.setItemClickListener(object:BoardAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, id: Int) {
                mainActivity.moveFragment(6,"boardDetailId", boardViewModel.boardQuestionList.value!!.get(position).id)
            }
        })

        boardQuestionAdapter.setLikeBtnClickListener(object : BoardAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, id: Int) {
                boardLike(id, userId)
            }
        })
    }
    fun initAdapter(){
        initViewModels()
        initMagazineRecyclerview()
        initFreeRecyclerview()
        initQuestionRecyclerView()
    }

    fun boardLike(boardDetailId: Int, userId: String) {
        BoardService().boardLike(boardDetailId, userId, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 게시판 찜하기 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                boardViewModel.getBoardFreeListFive(viewLifecycleOwner)
                boardViewModel.getBoardQuestionListFive(viewLifecycleOwner)
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

}