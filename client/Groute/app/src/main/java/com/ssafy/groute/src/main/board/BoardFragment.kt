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
import com.ssafy.groute.src.main.MainActivity

private const val TAG = "BoardFragment"
class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding
    private lateinit var mainActivity: MainActivity
    private var magazineAdapter: MagazineAdapter = MagazineAdapter()
    private var boardAdapter : BoardAdapter = BoardAdapter()

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
        boardAdapter = BoardAdapter()

        boards.apply {
            add(Board(userImg = R.drawable.user,
                userNick = "김싸피",
                title = "OO카페 어때요?",
                content = "갈려고 하는데 평이 너무 안좋아서 ㅠㅠ 혹시 커피맛 괜찮ㅎ나....",
            createDate = "01/19 13:19",
            goodCnt = 50,
            chatCnt = 11))
            add(Board(userImg = R.drawable.user,
                userNick = "김싸피",
                title = "OO카페 어때요?",
                content = "갈려고 하는데 평이 너무 안좋아서 ㅠㅠ 혹시 커피맛 괜찮ㅎ나....",
                createDate = "01/19 13:19",
                goodCnt = 50,
                chatCnt = 11))
            add(Board(userImg = R.drawable.user,
                userNick = "김싸피",
                title = "OO카페 어때요?",
                content = "갈려고 하는데 평이 너무 안좋아서 ㅠㅠ 혹시 커피맛 괜찮ㅎ나....",
                createDate = "01/19 13:19",
                goodCnt = 50,
                chatCnt = 11))

            boardAdapter.list = boards
            boardAdapter.notifyDataSetChanged()
        }
        binding.boardRvFree.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = boardAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.boardRvQuestion.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = boardAdapter
            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
}