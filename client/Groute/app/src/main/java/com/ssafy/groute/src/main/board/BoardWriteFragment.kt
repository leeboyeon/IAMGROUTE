package com.ssafy.groute.src.main.board

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardWriteBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.viewmodel.BoardViewModel
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import android.view.Display
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.WindowManager
import android.graphics.Point
import android.text.TextUtils
import android.widget.ImageButton
import android.widget.SearchView


private const val TAG = "BoardWriteF_Groute"
class BoardWriteFragment : BaseFragment<FragmentBoardWriteBinding>(FragmentBoardWriteBinding::bind, R.layout.fragment_board_write) {
    private lateinit var mainActivity:MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private val boardViewModel: BoardViewModel by activityViewModels()
    private var boardDetailId = -1
    private var boardId = -1
    private var placeId = -1

    private lateinit var searchAdapter: SearchAdapter

    private var boardDetailImg : String = ""

    // 사진 선택
    private lateinit var imgUri: Uri    // 파일 uri
    private var fileExtension : String? = ""    // 파일 확장자

    // 권한 허가
    private var permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() { // 권한 허가시 실행 할 내용
            selectImg()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            showCustomToast("Permission Denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
        arguments?.let{
            boardDetailId = it.getInt("boardDetailId", -1)
            boardId = it.getInt("boardId",-1)
            Log.d(TAG, "onCreate: boardDetailId ${boardDetailId}")
            Log.d(TAG, "onCreate: boardId ${boardId}")
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 게시글 작성인 경우(BoardDetailFragment에서 boardId가 넘어옴)
        if(boardId == 1) {  // 자유 게시판
            binding.boardWriteLLPlaceSerach.visibility = View.GONE
            binding.view15.visibility = View.GONE
        } else if(boardId == 2) {    // 질문 게시판
            binding.boardWriteLLPlaceSerach.visibility = View.VISIBLE
        }

        // 게시글 수정인 경우(BoardDetailFragment에서 boardDetailId가 넘어옴)
        if(boardDetailId > 0) {
            runBlocking {
                boardViewModel.getBoardDetail(boardDetailId)
            }
            initModifyView()
            registerBtnEvent(false)
        } else {
            registerBtnEvent(true)
        }

        cancelButtonEvent()
        selectPlaceBtnEvent()
        selectImgBtnEvent()

    }

    // 수정인 경우 게시판 구분해서 view에 기존 데이터 setting
    private fun initModifyView() {

        binding.boardDetailBtnComplete.setText("수정")

        boardViewModel.boardDetail.observe(viewLifecycleOwner) {
            boardDetailId = it.id
            boardId = it.boardId
            placeId = it.placeId

            if(it.boardId == 1) {
                binding.boardWriteLLPlaceSerach.visibility = View.GONE  // 장소 선택 layout gone
                setView(it)
            } else if(it.boardId == 2) {    // it.boardId == 2
                binding.boardWriteLLPlaceSerach.visibility = View.VISIBLE
                if(it.placeId > 0) {
                    runBlocking {
                        placeViewModel.getPlace(it.placeId)
                    }
                    binding.boardWriteTvPlaceName.text = placeViewModel.place.value?.name
                    placeId = it.placeId
                }
                setView(it)
            }
        }
    }

    // 수정인 경우 set BoardWrite Layout
    private fun setView(boardDetail: BoardDetail) {
        binding.boardWriteEtTitle.setText(boardDetail.title)
        binding.boardWriteEtContent.setText(boardDetail.content)

        val img = boardDetail.img
        if(!(img == "null" || img == "" || img == null)) {
            binding.boardWriteLLayoutSetImg.visibility = View.VISIBLE
            boardDetailImg = img
            // 사진 set
            Glide.with(requireContext())
                .load("${ApplicationClass.IMGS_URL}${img}")
                .into(binding.boardWriteIvSelectImg)

            // 파일 이름 set
            binding.boardWriteTvImgName.text = img.substring(img.lastIndexOf("/") + 1, img.length)
        } else {
            binding.boardWriteLLayoutSetImg.visibility = View.GONE
        }
    }

    // 게시글 작성 취소(우측 상단 'x') 버튼 클릭 이벤트
    private fun cancelButtonEvent() {
        binding.boardSearchIbtnCancle.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    // 게시글 작성 완료(우측 하단 '완료') 버튼 클릭 이벤트
    private fun registerBtnEvent(chk: Boolean) {
        var boardDetailId  = if(chk) {
            0
        } else {
            boardViewModel.boardDetail.value!!.id
        }
        binding.boardDetailBtnComplete.setOnClickListener {
            val title = binding.boardWriteEtTitle.text.toString()
            val content = binding.boardWriteEtContent.text.toString()
            val boardId = boardId
            val img = boardDetailImg
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

            if (boardId == 1) { // Free Board
                val boardDetail = BoardDetail(
                    id = boardDetailId,
                    title = title,
                    content = content,
                    img = img,
                    boardId = boardId,
                    userId = userId
                )
                setData(boardDetail, chk)
            } else if(boardId == 2) {    // Question Board
                val boardDetail = BoardDetail(
                    id = boardDetailId,
                    title = title,
                    content = content,
                    img = img,
                    boardId = boardId,
                    userId = userId,
                    placeId = placeId
                )
                setData(boardDetail, chk)
            }
        }
    }

    /**
     * 질문 게시판인 경우
     * 장소 선택 레이아웃 클릭 이벤트
     */
    private fun selectPlaceBtnEvent() {
        binding.boardWriteLLPlaceSerach.setOnClickListener {
            runBlocking {
                placeViewModel.getPlaceList()
            }
            showSelectPlaceDialog()
        }
    }

    /**
     * show place selection dialog
     */
    private fun showSelectPlaceDialog() {
//        val dialog = Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)    // fullscreen
        val dialog = Dialog(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_place, null)
        val dialogRecycleView = dialogView.findViewById<RecyclerView>(R.id.dialogSelectPlace_rv)

        val display: Display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val lp = WindowManager.LayoutParams()

        lp.copyFrom(dialog.window?.attributes)

        lp.width = size.x * 80 / 100 // 사용자 화면의 80%
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT // 높이는 내용 전체 높이만큼
        dialog.window?.attributes = lp

        placeViewModel.placeList.observe(viewLifecycleOwner, Observer {
            searchAdapter = SearchAdapter(it)

            dialogRecycleView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                adapter = searchAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            searchAdapter.setItemClickListener(object : SearchAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, id: Int) {
                    placeId = it[position].id
                    binding.boardWriteTvPlaceName.text = it[position].name
                    dialog.dismiss()
                }
            })
        })

        dialog.setContentView(dialogView)
        dialog.setCanceledOnTouchOutside(true)

        dialogView.findViewById<ImageButton>(R.id.dialogSelectPlace_ibtn_cancle).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<SearchView>(R.id.dialogSelectPlace_sv_placeName).setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(TextUtils.isEmpty(newText)){
                    searchAdapter.filter.filter("")
                }else{
                    searchAdapter.filter.filter(newText.toString())
                    searchAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        dialog.show()
    }


    /**
     * 사진 선택 버튼 클릭 이벤트
     */
    private fun selectImgBtnEvent() {

        if (::imgUri.isInitialized) {
            Log.d(TAG, "onCreate: $imgUri")
        } else {
            imgUri = Uri.EMPTY
            Log.d(TAG, "fileUri 초기화  $imgUri")
        }

        binding.boardWriteIbtnImg.setOnClickListener {
            checkPermissions()
        }
    }

    /**
     * 갤러리 사진 선택 intent launch
     */
    private fun selectImg() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        filterActivityLauncher.launch(intent)
    }

    /**
     * read gallery 권한 체크
     */
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 26) { // 출처를 알 수 없는 앱 설정 화면 띄우기
            val pm: PackageManager = requireContext().packageManager
            if (!pm.canRequestPackageInstalls()) {
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:${context?.packageName}")
                    )
                )
            }
        }

        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                .setDeniedMessage("If you reject permission,you can not use this service\n" +
                        "\n\nPlease turn on permissions at [Setting] > [Permission] ")
                .setPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).check()
        } else {
            selectImg()
        }
    }

    /**
     * 갤러리 사진 선택 result
     */
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                binding.boardWriteLLayoutSetImg.visibility = View.VISIBLE
                val currentImageUri = it.data?.data

                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.boardWriteIvSelectImg)

                            // 파일 이름 set
                            binding.boardWriteTvImgName.text = currentImageUri.lastPathSegment.toString()

                            imgUri = currentImageUri
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                        } else {
                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.boardWriteIvSelectImg)

                            // 파일 이름 set
                            binding.boardWriteTvImgName.text = currentImageUri.lastPathSegment.toString()

                            imgUri = currentImageUri
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                        }
                    }
                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == AppCompatActivity.RESULT_CANCELED){
                showCustomToast("사진 선택 취소")
                if(binding.boardWriteTvImgName.length() > 0) {  // 기존 사진이 있으면 visible
                    binding.boardWriteLLayoutSetImg.visibility = View.VISIBLE
                } else {
                    binding.boardWriteLLayoutSetImg.visibility = View.GONE
                    imgUri = Uri.EMPTY
                }
            } else{
                binding.boardWriteLLayoutSetImg.visibility = View.GONE
                Log.d(TAG,"filterActivityLauncher 실패")
            }
        }

    /**
     * insert & update BoardDetail
     * call server
     */
    private fun setData(boardDetail: BoardDetail, chk: Boolean) {

        // 게시글만 작성한 경우
        if(imgUri == Uri.EMPTY) {
            Log.d(TAG, "insertBoard: ${boardDetail}")
            Log.d(TAG, "insertBoard: ${boardDetail.img}")
            val gson : Gson = Gson()
            var json = gson.toJson(boardDetail)
            var rBody_boardDetail = RequestBody.create(MediaType.parse("text/plain"), json)
            Log.d(TAG, "updateUser_requestBodyUser: ${rBody_boardDetail.contentType()}, ${json}")
            if(chk) {   // 게시글 작성인 경우
                BoardService().insertBoardDetail(rBody_boardDetail, null, InsertBoardDetailCallback())
            } else {    // 게시글 수정인 경우
                BoardService().modifyBoardDetail(rBody_boardDetail, null, UpdateBoardDetailCallback())
            }
        }
        // 게시글 작성 + 사진 선택한 경우
        else {
            val file = File(imgUri.path!!)
            Log.d(TAG, "filePath: ${file.path} \n${file.name}\n${fileExtension}")

            var inputStream: InputStream? = null
            try {
                inputStream = requireActivity().contentResolver.openInputStream(imgUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)  // 압축해서 저장
            val requestBody = RequestBody.create(MediaType.parse("image/*"), byteArrayOutputStream.toByteArray())
            val extension = fileExtension!!.substring(fileExtension!!.lastIndexOf("/") + 1, fileExtension!!.length)
            val uploadFile = MultipartBody.Part.createFormData("img", "${file.name}.${extension}", requestBody)
            val gson : Gson = Gson()
            val json = gson.toJson(boardDetail)
            val rBody_boardDetail = RequestBody.create(MediaType.parse("text/plain"), json)
            if(chk) {   // 게시글 작성인 경우
                BoardService().insertBoardDetail(rBody_boardDetail, uploadFile, InsertBoardDetailCallback())
            } else {    // 게시글 수정인 경우
                BoardService().modifyBoardDetail(rBody_boardDetail, uploadFile, UpdateBoardDetailCallback())
            }
        }
    }

    /**
     * 게시글 작성 callback
     */
    inner class InsertBoardDetailCallback:RetrofitCallback<Int>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Int) {
            if(responseData > 0) {
                mainActivity.moveFragment(5,"boardId", responseData)
                showCustomToast("게시글 작성 성공")
                runBlocking {
                    boardViewModel.getBoardPostList(responseData)
                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }

    /**
     * 게시글 수정 callback
     */
    inner class UpdateBoardDetailCallback:RetrofitCallback<Int> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Int) {
            if(responseData > 0) {
                mainActivity.moveFragment(5,"boardId", responseData)
                showCustomToast("게시글 수정 성공")
                runBlocking {
                    boardViewModel.getBoardPostList(responseData)
                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
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