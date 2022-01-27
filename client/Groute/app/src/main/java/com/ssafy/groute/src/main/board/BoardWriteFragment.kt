package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentBoardWriteBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.util.RetrofitCallback
import org.json.JSONObject


private const val TAG = "BoardWriteFragment"
class BoardWriteFragment : Fragment() {
    private lateinit var binding: FragmentBoardWriteBinding
    private lateinit var mainActivity:MainActivity

    private var boardDetailId = -1
    private var boardId = -1
    private var placeId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let{
            boardDetailId = it.getInt("boardDetailId", -1)
            boardId = it.getInt("boardId",-1)
            placeId = it.getInt("placeId",-1)
            Log.d(TAG, "onCreate: ${boardDetailId}")
        }

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

        if(boardDetailId > 0){
            Log.d(TAG, "onViewCreated: ${boardDetailId}")
            binding.boardDetailBtnComplete.setText("수정")
            getListBoardDetail(boardDetailId)

            Log.d(TAG, "initButton: 글수정")
            binding.boardDetailBtnComplete.setOnClickListener {
                val title = binding.boardWriteEtTitle.text.toString()
                val content = binding.boardWriteEtContent.text.toString()
                var boardId = boardId
                var img = ""
                var userId = ApplicationClass.sharedPreferencesUtil.getUser().id

                val boardDetail = BoardDetail(
                    title = title,
                    content = content,
                    img = img,
                    boardId=boardId,
                    userId = userId,
                    id = boardDetailId
                )
                boardModify(boardDetail)
            }
        }
        if(placeId > 0){
            binding.boardWriteTvPlaceName.text = placeId.toString()
        }

    }

    fun initButton(){
        binding.boardSearchIbtnCancle.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }

            binding.boardDetailBtnComplete.setOnClickListener {
                val title = binding.boardWriteEtTitle.text.toString()
                val content = binding.boardWriteEtContent.text.toString()
                var boardId = boardId
                var img = ""
                var userId = ApplicationClass.sharedPreferencesUtil.getUser().id

                val boardDetail = BoardDetail(

                    title = title,
                    content = content,
                    img = img,
                    boardId=boardId,
                    userId = userId
                )
                boardWrite(boardDetail)
            }
        binding.searchLayout.setOnClickListener {
            mainActivity.moveFragment(9)
        }

    }
    fun boardWrite(boardDetail:BoardDetail){
        BoardService().insertBoardDetail(boardDetail, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 글쓰기 에러")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(5,"boardId",boardId)
                Toast.makeText(requireContext(),"글쓰기 성공",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    fun boardModify(boardDetail:BoardDetail){
        BoardService().modifyBoardDetail(boardDetail, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(5,"boardId",boardId)
                Toast.makeText(requireContext(),"수정 성공",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    fun getListBoardDetail(id:Int){
        BoardService().getListBoardDetail(id, object : RetrofitCallback<Map<String,Any>> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Map<String, Any>) {
                Log.d(TAG, "onSuccess: ${JSONObject(responseData).getJSONObject("boardDetail")}")
                Log.d(TAG, "onSuccess: ${JSONObject(responseData)}")
                
                val boardDetail = JSONObject(responseData).getJSONObject("boardDetail")
                val title = boardDetail.get("title")
                val content = boardDetail.get("content")
                boardId = boardDetail.get("boardId").toString().substring(0,1).toInt()
                Log.d(TAG, "onSuccess: ${title}")

                binding.boardWriteEtTitle.setText(title.toString())
                binding.boardWriteEtContent.setText(content.toString())
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
    companion object {
        @JvmStatic
        fun newInstance(key:String, value:Int) =
            BoardWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key,value)
                }
            }
    }
}