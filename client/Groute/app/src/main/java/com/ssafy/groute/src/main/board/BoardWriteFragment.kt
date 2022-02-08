package com.ssafy.groute.src.main.board

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
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
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList


private const val TAG = "BoardWriteF_Groute"
class BoardWriteFragment : BaseFragment<FragmentBoardWriteBinding>(FragmentBoardWriteBinding::bind, R.layout.fragment_board_write) {
//    private lateinit var binding: FragmentBoardWriteBinding
    private lateinit var mainActivity:MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private val boardViewModel: BoardViewModel by activityViewModels()
    private var boardDetailId = -1
    private var boardId = -1
    private var placeId = -1

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
            placeId = it.getInt("placeId",-1)
            Log.d(TAG, "onCreate: boardDetailId ${boardDetailId}")
            Log.d(TAG, "onCreate: boardId ${boardId}")
            Log.d(TAG, "onCreate: placeId ${placeId}")
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 게시글 작성인 경우(BoardDetailFragment에서 boardId가 넘어옴)
        if(boardId == 1) {  // 질문 게시판
            binding.boardWriteLLPlaceSerach.visibility = View.GONE
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

        Log.d(TAG, "onViewCreated: $boardId")

    }

    // 수정인 경우 게시판 구분해서 view에 기존 데이터 setting
    private fun initModifyView() {
        Log.d(TAG, "onViewCreated: $placeId")

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
        var boardDetailId = 0
        if(chk) {
            boardDetailId = 0
        } else {
            boardDetailId = boardViewModel.boardDetail.value!!.id
        }
        binding.boardDetailBtnComplete.setOnClickListener {
            val title = binding.boardWriteEtTitle.text.toString()
            val content = binding.boardWriteEtContent.text.toString()
            var boardId = boardId
            val img = boardDetailImg
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id

            if (boardId == 1) { // Free Board
                Log.d(TAG, "initButton: ${boardId}")
                val boardDetail = BoardDetail(
                    id = boardDetailId,
                    title = title,
                    content = content,
                    img = img,
                    boardId = boardId,
                    userId = userId
                )
                setData(boardDetail, 1, chk)
            } else if(boardId == 2) {    // Question Board

                Log.d(TAG, "initButton: ${boardId}")
                Log.d(TAG, "initButton_Place: ${placeId}")
                boardId = 2
                val boardDetail = BoardDetail(
                    id = boardDetailId,
                    title = title,
                    content = content,
                    img = img,
                    boardId = boardId,
                    userId = userId,
                    placeId = placeId
                )
                Log.d(TAG, "initButton: ${boardDetail}")
                setData(boardDetail, 2, chk)
            }
        }
    }

    /**
     * 수정해야됨. -> dialog 등 fragment 전환 없도록 구현
     */
    // 장소 선택 레이아웃 클릭 이벤트
    private fun selectPlaceBtnEvent() {
        binding.boardWriteLLPlaceSerach.setOnClickListener {
            mainActivity.moveFragment(9)
        }
    }

    /**
     * 사진 선택 버튼 클릭 이벤트
     */
    private fun selectImgBtnEvent() {

        if (::imgUri.isInitialized) {
            Log.d(TAG, "onCreate: $imgUri")
        } else {
            imgUri = Uri.EMPTY
            Log.d(TAG, "fileUri가 초기화  $imgUri")
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
                var currentImageUri = it.data?.data

                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                currentImageUri
                            )

                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.boardWriteIvSelectImg)

                            // 파일 이름 set
                            binding.boardWriteTvImgName.text = currentImageUri.lastPathSegment.toString()

                            imgUri = currentImageUri

                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                            Log.d(TAG, "filterActivityLauncher_1:$currentImageUri\n$imgUri\n$fileExtension")

                        } else {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            Log.d(TAG, "filterActivityLauncher_2 :$currentImageUri ${bitmap.width} ")
                            //binding.profileEditImg.setImageBitmap(bitmap)

                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.boardWriteIvSelectImg)

                            // 파일 이름 set
                            binding.boardWriteTvImgName.text = currentImageUri.lastPathSegment.toString()

                            imgUri = currentImageUri
//                            Log.d(TAG, "filterAL: ${imgUri}")
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                            Log.d(TAG, "filterActivityLauncher_1:$currentImageUri\n$imgUri\n" +
                                    "$fileExtension")
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
    private fun setData(boardDetail: BoardDetail, boardId: Int, chk: Boolean) {

        // 게시글만 작성한 경우
        if(imgUri == Uri.EMPTY) {
            Log.d(TAG, "insertBoard: ${boardDetail}")
            Log.d(TAG, "insertBoard: ${boardDetail.img}")
            val gson : Gson = Gson()
            var json = gson.toJson(boardDetail)
            var rBody_boardDetail = RequestBody.create(MediaType.parse("text/plain"), json)
            Log.d(TAG, "updateUser_requestBodyUser: ${rBody_boardDetail.contentType()}, ${json}")
            if(chk) {   // 게시글 작성인 경우
                boardWrite(boardDetail = rBody_boardDetail, null, boardId)
            } else {    // 게시글 수정인 경우
                boardModify(rBody_boardDetail, null, boardId)
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
                boardWrite(boardDetail = rBody_boardDetail, uploadFile, boardId)
            } else {    // 게시글 수정인 경우
                boardModify(rBody_boardDetail, uploadFile, boardId)
            }

        }
    }

    /**
     * 게시글 작성 callback
     */
    private fun boardWrite(boardDetail: RequestBody, img: MultipartBody.Part?, boardId:Int){
        Log.d(TAG, "boardWrite: ${boardDetail} ${img}")
        BoardService().insertBoardDetail(boardDetail, img, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: 글쓰기 에러")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(5,"boardId", boardId)
                showCustomToast("글쓰기 성공")
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    /**
     * 게시글 수정 callback
     */
    private fun boardModify(boardDetail:RequestBody, img: MultipartBody.Part?, boardId: Int){
        BoardService().modifyBoardDetail(boardDetail, img, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                mainActivity.moveFragment(5,"boardId", boardId)
                showCustomToast("수정 성공")
//                Toast.makeText(requireContext(),"수정 성공",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    /**
     * id에 해당하는 게시글 1개 조회
     * 수정 버튼 클릭으로 넘어왔을 때 title, content img, place setting
     */
//    fun getListBoardDetail(id:Int){
//        BoardService().getListBoardDetail(id, object : RetrofitCallback<Map<String,Any>> {
//            override fun onError(t: Throwable) {
//                Log.d(TAG, "onError: ")
//            }
//
//            override fun onSuccess(code: Int, responseData: Map<String, Any>) {
//                Log.d(TAG, "onSuccess: ${JSONObject(responseData).getJSONObject("boardDetail")}")
//                Log.d(TAG, "onSuccess: ${JSONObject(responseData)}")
//
//                val boardDetail = JSONObject(responseData).getJSONObject("boardDetail")
//                val title = boardDetail.get("title").toString()
//                val content = boardDetail.get("content").toString()
//                val img = boardDetail.get("img").toString()
//                placeId = boardDetail.get("placeId").toString().substring(0,1).toInt()
//                boardId = boardDetail.get("boardId").toString().substring(0,1).toInt()
//                Log.d(TAG, "onSuccess: ${title} $content $img $placeId $boardId")
//                Log.d(TAG, "initView: $boardId")
//                if(boardId == 1) {  // 자유게시판이면
//                    binding.boardWriteLLPlaceSerach.visibility = View.GONE  // 장소 선택 layout gone
//                } else {    // 질문 게시판이면
//                    binding.boardWriteLLPlaceSerach.visibility = View.VISIBLE
//                    if(placeId > 0) {
//                        runBlocking {
//                            placeViewModel.getPlace(placeId)
//                            binding.boardWriteTvPlaceName.text = placeViewModel.place.value?.name
//                        }
//                    }
//                }
//                binding.boardWriteEtTitle.setText(title.toString())
//                binding.boardWriteEtContent.setText(content.toString())
//
//                if(!(img.equals("null") || img.equals("") || img == null)) {
//                    binding.boardWriteLLayoutSetImg.visibility = View.VISIBLE
//                    // 사진 set
//                    Glide.with(requireContext())
//                        .load("${ApplicationClass.IMGS_URL}${img}")
//                        .into(binding.boardWriteIvSelectImg)
//
//                    // 파일 이름 set
//                    binding.boardWriteTvImgName.text = img.substring(img.lastIndexOf("/") + 1, img.length).toString()
//                } else {
//                    binding.boardWriteLLayoutSetImg.visibility = View.GONE
//                }
//                registerBtnEvent(false)
//            }
//
//            override fun onFailure(code: Int) {
//                Log.d(TAG, "onFailure: ")
//            }
//        })
//    }


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