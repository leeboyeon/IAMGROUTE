package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentBoardWriteBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

private const val TAG = "BoardWriteFragment"
class BoardWriteFragment : Fragment() {
    private lateinit var binding: FragmentBoardWriteBinding
    private lateinit var mainActivity:MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
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
        binding = FragmentBoardWriteBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
    }
    fun initButton(){
        binding.boardWriteIbtnCancle.setOnClickListener {
            mainActivity.onBackPressed()
        }
        binding.boardDetailBtnComplete.setOnClickListener {
            val title = binding.boardWriteEtTitle.text.toString()
            val content = binding.boardWriteEtContent.text.toString()
            val now:Long = System.currentTimeMillis()
            val date: Date = Date(now)
            val createTime =  SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)
            val heartCnt = 0
            var boardId = 1
            var img = ""
            var updateDate = createTime
            var userId = ApplicationClass.sharedPreferencesUtil.getUser().id

            val boardDetail = BoardDetail(
                id = 0,
                boardId = boardId,
                title = title,
                content = content,
                img = img,
                createDate = createTime,
                updateDate = updateDate,
                heartCnt = heartCnt,
                hitCnt = 0,
                userId = userId
            )
            boardWrite(boardDetail)
        }
    }
    fun boardWrite(boardDetail:BoardDetail){
        BoardService().insertBoardDetail(boardDetail, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 글쓰기 에러")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(5)
                Toast.makeText(requireContext(),"글쓰기 성공",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoardWriteFragment().apply {

            }
    }
}