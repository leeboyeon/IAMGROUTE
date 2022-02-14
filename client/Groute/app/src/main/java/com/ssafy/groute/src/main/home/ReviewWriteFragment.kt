package com.ssafy.groute.src.main.home

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jakewharton.rxbinding3.widget.textChanges
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentReviewWriteBinding
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.board.BoardWriteFragment
import com.ssafy.groute.src.service.BoardService
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.RetrofitCallback
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

private const val TAG = "ReviewWriteFragment"
class ReviewWriteFragment : BaseFragment<FragmentReviewWriteBinding>(FragmentReviewWriteBinding::bind, R.layout.fragment_review_write) {
    private lateinit var mainActivity: MainActivity
    private val placeViewModel: PlaceViewModel by activityViewModels()
    private var placeId = -1
    private var reviewId = -1

    private lateinit var editTextSubscription: Disposable

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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            placeId = it.getInt("placeId",-1)
            reviewId = it.getInt("reviewId",-1)
            Log.d(TAG, "onAttach_ReviewId: ${reviewId}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = placeViewModel

        runBlocking {
            placeViewModel.getPlace(placeId)
            placeViewModel.getReviewById(reviewId)
            Log.d(TAG, "onViewCreated: ${placeViewModel.getReviewById(reviewId)}")
        }

        // 뒤로가기
        binding.reviewWriteIbtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }

        // review 수정인 경우
        if(reviewId > 0){
            var beforeImg = ""
            binding.reviewWriteBtnWrite.text = "리뷰 수정"
            placeViewModel.review.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "onViewCreated: ${it.toString()}")
                binding.reviewWriteTietContent.setText(it.content)
                binding.ratingBar.rating = it.rate.toFloat()
                if(!(it.img == "null" || it.img == "" || it.img == null)) {
                    binding.reviewWriteLLayoutSetImg.visibility = View.VISIBLE
                    beforeImg = it.img.toString()
                    // 사진 set
                    Glide.with(requireContext())
                        .load("${ApplicationClass.IMGS_URL}${beforeImg}")
                        .into(binding.reviewWriteIvSelectImg)

                    // 파일 이름 set
                    binding.reviewWriteTvImgName.text = beforeImg.substring(beforeImg.lastIndexOf("/") + 1, beforeImg.length)
                } else {
                    beforeImg = ""
                    binding.reviewWriteLLayoutSetImg.visibility = View.GONE
                }
                initModifyButton(beforeImg)
            })

        } else {
            initButton()
        }
        selectImgBtnEvent()
        initTiedListener()

    }

    // init TextInputEditText Listener
    private fun initTiedListener() {
        editTextSubscription = binding.reviewWriteTietContent
            .textChanges()
            .subscribe {
                textLengthChk(it.toString())
            }
//        binding.reviewWriteTietContent.setQueryDebounce {
//            Log.d(TAG, "initTiedListener: $it")
//            textLengthChk(it)
////            getQueryResult(it)
//        }
    }

    private fun textLengthChk(str : String) : Boolean {
        Log.d(TAG, "textLengthChk: $str")
        if(str.trim().isEmpty()){
            binding.reviewWriteTilContent.error = "Required Field"
            binding.reviewWriteTietContent.requestFocus()
            return false
        } else if(str.length <= 30 || str.length >= 255) {
            binding.reviewWriteTilContent.error = "30자 이상 255자 이하로 작성해주세요."
            binding.reviewWriteTietContent.requestFocus()
            return false
        }
        else {
            binding.reviewWriteTilContent.error = null
            return true
        }
    }

    // 수정하기 버튼 클릭 이벤트 초기화
    private fun initModifyButton(beforeImg : String) {
        binding.reviewWriteBtnWrite.setOnClickListener {
            if(textLengthChk(binding.reviewWriteTietContent.text.toString()) == true) {

                val content = binding.reviewWriteTietContent.text.toString()
                val rate = binding.ratingBar.rating.toDouble()
                val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
                val review = PlaceReview(
                    placeId,
                    userId,
                    content,
                    rate,
                    beforeImg,
                    reviewId
                )
    //            modifyReview(review)
                setData(review, false)  // false -> review 수정
            } else {
                showCustomToast("글자 수를 확인해 주세요")
            }
        }
    }

    // review 등록하기 버튼 클릭 이벤트
    private fun initButton() {
        binding.reviewWriteBtnWrite.setOnClickListener {
            if(textLengthChk(binding.reviewWriteTietContent.text.toString()) == true) {
                val content = binding.reviewWriteTietContent.text.toString()
                val rate = binding.ratingBar.rating.toDouble()
                val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
                val review = PlaceReview(
                    placeId,
                    userId,
                    content,
                    rate,
                    ""
                )
    //            insertReview(review)
                setData(review, true)
            } else {
                showCustomToast("글자 수를 확인해 주세요.")
            }
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
            Log.d(TAG, "fileUri 초기화  $imgUri")
        }

        binding.reviewWriteButtonAddImg.setOnClickListener {
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
                binding.reviewWriteLLayoutSetImg.visibility = View.VISIBLE
                val currentImageUri = it.data?.data

                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            imgUri = currentImageUri
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.reviewWriteIvSelectImg)
                            val extension = fileExtension!!.substring(fileExtension!!.lastIndexOf("/") + 1, fileExtension!!.length)
                            // 파일 이름 set
                            binding.reviewWriteTvImgName.text = "${currentImageUri.lastPathSegment}.$extension"

                        } else {
                            imgUri = currentImageUri
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.reviewWriteIvSelectImg)
                            val extension = fileExtension!!.substring(fileExtension!!.lastIndexOf("/") + 1, fileExtension!!.length)

                            // 파일 이름 set
                            binding.reviewWriteTvImgName.text = "${currentImageUri.lastPathSegment}.$extension"

                        }
                    }
                } catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == AppCompatActivity.RESULT_CANCELED){
                showCustomToast("사진 선택 취소")
                if(binding.reviewWriteTvImgName.length() > 0) {  // 기존 사진이 있으면 visible
                    binding.reviewWriteLLayoutSetImg.visibility = View.VISIBLE
                } else {
                    binding.reviewWriteLLayoutSetImg.visibility = View.GONE
                    imgUri = Uri.EMPTY
                }
            } else{
                binding.reviewWriteLLayoutSetImg.visibility = View.GONE
                Log.d(TAG,"filterActivityLauncher 실패")
            }
        }



    /**
     * insert & update placeReview
     * call server
     */
    private fun setData(placeReview: PlaceReview, chk: Boolean) {

        // 게시글만 작성한 경우
        if(imgUri == Uri.EMPTY) {
            val gson : Gson = Gson()
            val json = gson.toJson(placeReview)
            val rBody_placeReivew = RequestBody.create(MediaType.parse("text/plain"), json)
            if(chk) {   // placeReview 작성인 경우
                PlaceService().insertPlaceReview(rBody_placeReivew, null, InsertPlaceReviewCallback())
            } else {    // placeReview 수정인 경우
                PlaceService().updatePlaceReview(rBody_placeReivew, null, UpdatePlaceReviewCallback())
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
            val json = gson.toJson(placeReview)
            val rBody_placeReivew = RequestBody.create(MediaType.parse("text/plain"), json)
            if(chk) {   // 게시글 작성인 경우
                PlaceService().insertPlaceReview(rBody_placeReivew, uploadFile, InsertPlaceReviewCallback())
            } else {    // 게시글 수정인 경우
                PlaceService().updatePlaceReview(rBody_placeReivew, uploadFile, UpdatePlaceReviewCallback())
            }
        }
    }

    // review insert callback
    inner class InsertPlaceReviewCallback : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData == true) {
                mainActivity.moveFragment(4, "placeId", placeId)
                showCustomToast("리뷰 작성 성공")
//                runBlocking {
//                    placeViewModel.getPlaceReviewListbyId(placeId)
//                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }

    // update placeReview Callback
    inner class UpdatePlaceReviewCallback : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData == true) {
                mainActivity.moveFragment(4,"placeId",placeId)
                showCustomToast("리뷰 수정 성공")
//                runBlocking {
//                    placeViewModel.getPlaceReviewListbyId(placeId)
//                }
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }

    // TextInputEditText 에 쿼리 디바운싱 적용
    fun TextInputEditText.setQueryDebounce(queryFunction: (String) -> Unit): Disposable {
        val editTextChangeObservable = this.textChanges()
        val searchEditTextSubscription: Disposable =
            editTextChangeObservable
                // 마지막 글자 입력 0.8초 후에 onNext 이벤트로 데이터 발행
                .debounce(800, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                // 구독을 통해 이벤트 응답 처리
                .subscribeBy(
                    onNext = {
                        queryFunction(it.toString())
                        Log.d(TAG, "setQueryDebounce: onNext : $it")
                        textLengthChk(it.toString())
                    },
                    onComplete = {
                        Log.d(TAG, "setQueryDebounce: onComplete :")
                    },
                    onError = {
                        Log.d(TAG, "setQueryDebounce: onNext :")
                    }
                )
        return searchEditTextSubscription  // Disposable 반환
    }

    override fun onDestroy() {
        editTextSubscription.dispose()
        mainActivity.hideBottomNav(false)
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(key1:String, value1:Int, key2:String, value2:Int) =
            ReviewWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                    putInt(key2,value2)
                }
            }
    }


    inner class ReviewCallback():RetrofitCallback<PlaceReview>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: PlaceReview) {
            Log.d(TAG, "onSuccess: ")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }
}

