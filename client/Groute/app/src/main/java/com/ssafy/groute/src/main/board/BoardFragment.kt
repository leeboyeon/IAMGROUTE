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
//    private lateinit var boardViewModel: BoardViewModel

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

    fun initViewModel(){
        //자유게시판
        val boardViewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        boardViewModel.getFreelist().observe(viewLifecycleOwner, Observer {
            if(it != null){
                val tmpList:ArrayList<BoardDetail> = arrayListOf()
                for(i in 0..4){
                    tmpList.add(it.get(i))
                }
                boardFreeAdapter.setBoardList(tmpList)
                boardFreeAdapter.notifyDataSetChanged()
            }else{
                Toast.makeText(requireContext(),"Error List null",Toast.LENGTH_SHORT).show()
            }

            boardFreeAdapter.setItemClickListener(object: BoardAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(6,"boardDetailId", boardViewModel.freeList.value!!.get(position).id)
                }
            })

            boardFreeAdapter.setLikeBtnClickListener(object : BoardAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, id: Int) {
                    boardLike(id, userId)
                }
            })
        })
        //질문게시판
        boardViewModel.getQuestionlist().observe(viewLifecycleOwner, Observer {
            if(it != null){
                val tmpList:ArrayList<BoardDetail> = arrayListOf()
                for(i in 0..4){
                    tmpList.add(it.get(i))
                }
                boardQuestionAdapter.setBoardList(tmpList)
                boardQuestionAdapter.notifyDataSetChanged()
            }

            boardQuestionAdapter.setItemClickListener(object:BoardAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: Int) {
                    mainActivity.moveFragment(6,"boardDetailId", boardViewModel.questionList.value!!.get(position).id)
                }
            })

            boardQuestionAdapter.setLikeBtnClickListener(object : BoardAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, id: Int) {
                    boardLike(id, userId)
                }
            })

        })
    }
    fun initFreeRecyclerview(){
        binding.boardRvFree.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        boardFreeAdapter = BoardAdapter(requireContext(),viewLifecycleOwner)
        binding.boardRvFree.adapter = boardFreeAdapter
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
    fun initQuestionRecyclerView(){
        binding.boardRvQuestion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        boardQuestionAdapter = BoardAdapter(requireContext(),viewLifecycleOwner)
        binding.boardRvQuestion.adapter = boardQuestionAdapter
    }
    fun initAdapter(){
        initMagazineRecyclerview()
        initFreeRecyclerview()
        initQuestionRecyclerView()
        initViewModel()
    }

    fun boardLike(boardDetailId: Int, userId: String) {
        BoardService().boardLike(boardDetailId, userId, object : RetrofitCallback<Any> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 게시판 찜하기 에러")
            }

            override fun onSuccess(code: Int, responseData: Any) {
                initAdapter()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

}