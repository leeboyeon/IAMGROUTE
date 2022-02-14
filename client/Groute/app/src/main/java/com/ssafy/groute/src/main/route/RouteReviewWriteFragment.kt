package com.ssafy.groute.src.main.route

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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentRouteReviewWriteBinding
import com.ssafy.groute.src.dto.PlanReview
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jakewharton.rxbinding3.widget.textChanges
import com.ssafy.groute.src.dto.PlaceReview
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.main.home.ReviewWriteFragment
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.service.UserPlanService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

private const val TAG = "RouteReviewWriteFrament"
class RouteReviewWriteFragment : BaseFragment<FragmentRouteReviewWriteBinding>(FragmentRouteReviewWriteBinding::bind, R.layout.fragment_route_review_write) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    private var reviewId = -1

    private lateinit var editTextSubscription: Disposable // edit text subscribe

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
        arguments?.let {
            planId = it.getInt("planId",-1)
            reviewId = it.getInt("reviewId",-1)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.planViewModel = planViewModel

        runBlocking {
            planViewModel.getPlanById(planId, 2)
            planViewModel.getPlanReviewById(reviewId)
        }

        binding.routeReviewWriteIbtnBack.setOnClickListener{
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }

        // 리뷰 수정인 경우
        if(reviewId > 0) {
            var beforeImg = ""
            binding.routeReviewWriteBtnWrite.text = "리뷰 수정"
            planViewModel.review.observe(viewLifecycleOwner, Observer {
                binding.planReview = it // 화면에 planReview data setting

                beforeImg = it.img
                binding.planReviewWriteTvImgName.text = beforeImg.substring(beforeImg.lastIndexOf("/") + 1, beforeImg.length)
                binding.planReviewWriteLLayoutSetImg.visibility = View.VISIBLE
//                binding.routeReviewWriteEtContent.setText(it.content)
//                binding.routeReviewWriteRatingBar.rating = it.rate.toFloat()
                initModifyButton(beforeImg)
            })
        } else {
            initInsertButton()
        }

        selectImgBtnEvent()
        initTiedListener()

    }

    // init TextInputEditText Listener
    private fun initTiedListener() {
        editTextSubscription = binding.planReviewWriteTietContent
            .textChanges()
            .subscribe {
                textLengthChk(it.toString())
            }
    }

    // planReview Content Text 길이 체크
    private fun textLengthChk(str : String) : Boolean {
        Log.d(TAG, "textLengthChk: $str")
        if(str.trim().isEmpty()){
            binding.planReviewWriteTilContent.error = "Required Field"
            binding.planReviewWriteTietContent.requestFocus()
            return false
        } else if(str.length <= 30 || str.length >= 255) {
            binding.planReviewWriteTilContent.error = "30자 이상 255자 이하로 작성해주세요."
            binding.planReviewWriteTietContent.requestFocus()
            return false
        }
        else {
            binding.planReviewWriteTilContent.error = null
            return true
        }
    }

    // 수정 버튼 클릭 이벤트
    private fun initModifyButton(beforeImg : String){
        binding.routeReviewWriteBtnWrite.setOnClickListener {
            if(textLengthChk(binding.planReviewWriteTietContent.text.toString()) == true) {

                val content = binding.planReviewWriteTietContent.text.toString()
                val rate = binding.routeReviewWriteRatingBar.rating.toDouble()
                val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
                val review = PlanReview(
                    planId,
                    userId,
                    content,
                    rate,
                    beforeImg,
                    reviewId
                )
                setData(review, false)  // 수정되는 planReview data 서버로 전송하기 위해 데이터 변경
            } else {
                showCustomToast("글자 수를 확인해 주세요")
            }
        }

    }

    // review 등록 버튼 클릭 이벤트
    private fun initInsertButton(){
        binding.routeReviewWriteBtnWrite.setOnClickListener {
            val content = binding.routeReviewWriteEtContent.text.toString()
            var rate = binding.routeReviewWriteRatingBar.rating.toDouble()
            val userId = ApplicationClass.sharedPreferencesUtil.getUser().id
            val review = PlanReview(
                planId,
                userId,
                content,
                rate,
                ""
            )
            insertReview(review)
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

        binding.routeReviewWriteButtonAddImg.setOnClickListener {
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
                binding.planReviewWriteLLayoutSetImg.visibility = View.VISIBLE
                val currentImageUri = it.data?.data

                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            imgUri = currentImageUri
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.planReviewWriteIvSelectImg)
                            val extension = fileExtension!!.substring(fileExtension!!.lastIndexOf("/") + 1, fileExtension!!.length)
                            // 파일 이름 set
                            binding.planReviewWriteTvImgName.text = "${currentImageUri.lastPathSegment}.$extension"

                        } else {
                            imgUri = currentImageUri
                            fileExtension = requireActivity().contentResolver.getType(currentImageUri)
                            // 사진 set
                            Glide.with(this)
                                .load(currentImageUri)
                                .into(binding.planReviewWriteIvSelectImg)
                            val extension = fileExtension!!.substring(fileExtension!!.lastIndexOf("/") + 1, fileExtension!!.length)

                            // 파일 이름 set
                            binding.planReviewWriteTvImgName.text = "${currentImageUri.lastPathSegment}.$extension"

                        }
                    }
                } catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == AppCompatActivity.RESULT_CANCELED){
                showCustomToast("사진 선택 취소")
                if(binding.planReviewWriteTvImgName.length() > 0) {  // 기존 사진이 있으면 visible
                    binding.planReviewWriteLLayoutSetImg.visibility = View.VISIBLE
                } else {
                    binding.planReviewWriteLLayoutSetImg.visibility = View.GONE
                    imgUri = Uri.EMPTY
                }
            } else{
                binding.planReviewWriteLLayoutSetImg.visibility = View.GONE
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
            val rBody_planReivew = RequestBody.create(MediaType.parse("text/plain"), json)
            if(chk) {   // placeReview 작성인 경우
//                UserPlanService().insertPlanReview(rBody_planReivew, null, ReviewWriteFragment.InsertPlaceReviewCallback())
            } else {    // placeReview 수정인 경우
                UserPlanService().updatePlanReview(rBody_planReivew, null, UpdatePlaceReviewCallback())
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
            val rBody_planReivew = RequestBody.create(MediaType.parse("text/plain"), json)
            if(chk) {   // 게시글 작성인 경우
//                UserPlan. insertPlaceReview(rBody_placeReivew, uploadFile, InsertPlaceReviewCallback())
            } else {    // 게시글 수정인 경우
                UserPlanService().updatePlanReview(rBody_planReivew, uploadFile, UpdatePlaceReviewCallback())
            }
        }
    }


    // update planReview Callback
    inner class UpdatePlaceReviewCallback : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if (responseData == true) {
                mainActivity.moveFragment(12, "planIdDetail", planId, "planIdUser", -1)
                showCustomToast("리뷰 수정 성공")
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }

    }


//    fun modifyReview(review: PlanReview){
//        UserPlanService().updatePlanReview(review, object : RetrofitCallback<Boolean> {
//            override fun onError(t: Throwable) {
//                Log.d(TAG, "onError: ")
//            }
//
//            override fun onSuccess(code: Int, responseData: Boolean) {
//                Log.d(TAG, "onSuccess: ")
//                mainActivity.moveFragment(12, "planIdDetail", planId, "planIdUser", -1)
//                Toast.makeText(requireContext(),"리뷰수정 성공", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(code: Int) {
//                Log.d(TAG, "onFailure: ")
//            }
//
//        })
//    }
//
//    fun insertReview(review: PlanReview){
//        UserPlanService().insertPlanReview(review, object : RetrofitCallback<Boolean> {
//            override fun onError(t: Throwable) {
//                Log.d(TAG, "onError: ")
//            }
//
//            override fun onSuccess(code: Int, responseData: Boolean) {
//                Log.d(TAG, "onSuccess: ")
//                mainActivity.moveFragment(12, "planIdDetail", planId, "planIdUser", -1)
//                Toast.makeText(requireContext(),"리뷰작성 성공", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(code: Int) {
//                Log.d(TAG, "onFailure: ")
//            }
//        })
//    }

    companion object {

        @JvmStatic
        fun newInstance(key1: String, value1: Int, key2: String, value2: Int) =
            RouteReviewWriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(key1, value1)
                    putInt(key2, value2)
                }
            }
    }
}