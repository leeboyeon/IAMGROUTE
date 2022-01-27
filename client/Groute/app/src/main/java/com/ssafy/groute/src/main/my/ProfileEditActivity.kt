package com.ssafy.groute.src.main.my

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.ActivityProfileEditBinding
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.login.SignFragment
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern


private const val TAG = "ProfileEditA_groute"
class ProfileEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileEditBinding
    private var userEmail : String = ""
    private var userBirth : String = ""
    private var userGender : String = ""
    private lateinit var imgUri: Uri
    private var fileExtension : String? = ""
    private val OPEN_GALLERY = 1
    private val PERMISSION_GALLERY = 101
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userData = intent.getSerializableExtra("userData") as UserInfoResponse
        Log.d(TAG, "onCreate: $userData")
        initData(userData)
        initListeners()
        userBirth = userData.birth
        userEmail = userData.email
        userGender = userData.gender.toString()

//        android:src="@drawable/profile"
//        imgUri = Uri.parse("content://com.android.providers.downloads.documents/document/${userData.img}")
//        imgUri = Uri.parse(userData.img)
//        Log.d(TAG, "onCreate: $imgUri")
        if (::imgUri.isInitialized) {
            //처리할 코드
            Log.d(TAG, "onCreate: $imgUri")
        } else {
            imgUri = Uri.EMPTY
            Log.d(TAG, "fileUri가 초기화  $imgUri")

        }

        binding.profileEditFinish.setOnClickListener {
            var user = isAvailable(userData)
            if(user != null) {
                updateUser(user)
            } else {
                Toast.makeText(this, "입력 값을 다시 확인해 주세요", Toast.LENGTH_LONG).show()
            }
        }

        // 프로필 이미지 수정 버튼 클릭
        binding.profileEditChangeImgTv.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }

        // 수정 취소 버튼
        binding.profileEditCancel.setOnClickListener {
            finish()
        }

    }

    // 갤러리에서 이미지 클릭한 후
    @SuppressLint("LongLogTag")
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data !=null) {
                var currentImageUri = it.data?.data

                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            Glide.with(this)
                                .load(currentImageUri)
                                .circleCrop()
                                .into(binding.profileEditImg)
                            imgUri = currentImageUri

                            fileExtension = contentResolver.getType(currentImageUri)
                            Log.d(TAG, "filterActivityLauncher_1:$currentImageUri\n$imgUri\n$fileExtension")

                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            Log.d(TAG, "filterActivityLauncher_2 :$currentImageUri ${bitmap.width} ")
                            //binding.profileEditImg.setImageBitmap(bitmap)
                            Glide.with(this)
                                .load(currentImageUri)
                                .circleCrop()
                                .into(binding.profileEditImg)
                            imgUri = currentImageUri
//                            Log.d(TAG, "filterAL: ${imgUri}")
                            fileExtension = contentResolver.getType(currentImageUri)
                            Log.d(TAG, "filterActivityLauncher_1:$currentImageUri\n$imgUri\n" +
                                    "$fileExtension")

                        }
                    }

                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                imgUri = Uri.EMPTY
            }else{
                Log.d("ActivityResult","something wrong")
            }
        }



    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if(focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            var x = ev?.x
            var y = ev?.y
            if(!rect.contains(x!!.toInt(), y!!.toInt())) {
                val imm : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 사용자 정보 화면에 초기화
    fun initData(user: UserInfoResponse) {

        var img = ""
        if(user.type.equals("sns")){
            if(user.img != null) {
                img = user.img!!
            }
        } else{
            if(user.img != null) {
                img = "${ApplicationClass.IMGS_URL_USER}${user.img}"
            }
        }
        var id = user.id
        var password = user.password
        var nickname = user.nickname
        var phone = user.phone
        val u = User(id, password, nickname, phone, img)
        binding.user = u

        if(user.email != null) {
            if (user.email == "") {
                binding.profileEditEmailEt.setText("")
                binding.profileEditEmailDomain.setText("")
            } else {
                val emailStr = user.email.split("@")
                binding.profileEditEmailEt.setText(emailStr.get(0))
                binding.profileEditEmailDomain.setText(emailStr.get(1))
            }
        }

        if(user.birth != null) {
            if (user.birth != "") {
                val birthStr = user.birth.split("-")
                binding.profileEditSpinnerYear.setSelection(
                    getSpinnerIndex(
                        binding.profileEditSpinnerYear,
                        birthStr.get(0)
                    )
                )
                binding.profileEditSpinnerMonth.setSelection(
                    getSpinnerIndex(
                        binding.profileEditSpinnerMonth,
                        birthStr.get(1)
                    )
                )
                binding.profileEditSpinnerDay.setSelection(
                    getSpinnerIndex(
                        binding.profileEditSpinnerDay,
                        birthStr.get(2)
                    )
                )
            }
        }

        if(user.gender != null) {
            if (user.gender != "") {
                if (user.gender == "M") {
                    binding.profileEditRadioMan.isChecked = true
                } else if (user.gender == "F") {
                    binding.profileEditRadioWoman.isChecked = true
                }
            }
        }
    }

    fun initListeners(){
        binding.profileEditNicknameEt.addTextChangedListener(TextFieldValidation(binding.profileEditNicknameEt))
        binding.profileEditPhoneEt.addTextChangedListener(TextFieldValidation(binding.profileEditPhoneEt))
        binding.profileEditEmailEt.addTextChangedListener(TextFieldValidation(binding.profileEditEmailEt))
        binding.profileEditEmailDomain.addTextChangedListener(TextFieldValidation(binding.profileEditEmailDomain))

        initDomain() // email domain list adapter
        selectedBirth() // birth spinner selectedItem listener
        selectedGender()    // gender radio group Item Selected
    }

    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when(view.id){
                com.ssafy.groute.R.id.profile_edit_nickname_et -> {
                    validatedNickname()
                }
                com.ssafy.groute.R.id.profile_edit_phone_et -> {
                    validatedPhone()
                }
                com.ssafy.groute.R.id.profile_edit_email_et -> {
                    validatedEmail()
                }
                com.ssafy.groute.R.id.profile_edit_email_domain -> {
                    validatedEmail()
                }
            }
        }
    }

    fun updateUser(user: User) {
        // 사진 선택 안하고 사용자 정보 수정 시 user 정보만 서버로 전송
        if(imgUri == Uri.EMPTY) {
            Log.d(TAG, "updateUser: ${user}")
            Log.d(TAG, "updateUser: ${user.img}")
            val gson : Gson = Gson()
            var json = gson.toJson(user)
            var requestBody_user = RequestBody.create(MediaType.parse("text/plain"), json)
            Log.d(TAG, "updateUser_requestBodyuser: ${requestBody_user.contentType()}")
            UserService().updateUserInfo(requestBody_user, userUpdateCallback())
        }
        // 사진 선택 + 사용자 정보 수정 시 사용자 정보와 파일 같이 서버로 전송
        else {
            val file = File(imgUri.path!!)
            Log.d(TAG, "filePath: ${file.path} \n${file.name}\n${fileExtension}")

            var inputStream: InputStream? = null
            try {
                inputStream = this.contentResolver.openInputStream(imgUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)  // 압축해서 저장
            val requestBody = RequestBody.create(MediaType.parse("image/*"), byteArrayOutputStream.toByteArray())
            fileExtension = fileExtension?.substring(6)
            val uploadFile = MultipartBody.Part.createFormData("img", "${file.name}.$fileExtension", requestBody)
            val gson : Gson = Gson()
            val json = gson.toJson(user)
            val requestBody_user = RequestBody.create(MediaType.parse("text/plain"), json)

            UserService().updateUserInfo(requestBody_user, uploadFile, userUpdateCallback())
        }


    }

    inner class userUpdateCallback: RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData) {
                Toast.makeText(this@ProfileEditActivity, "프로필 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onSuccess: 사용자 정보 수정 완료")
                finish()
            } else {
                Toast.makeText(this@ProfileEditActivity, "프로필 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        @SuppressLint("LongLogTag")
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "프로필 정보 수정 중 통신오류")
        }

        @SuppressLint("LongLogTag")
        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    /**
     * 필수 데이터 id 유효성 검사 및 중복 확인, pw, nickname, phone 유효성 통과 여부와
     * 선택 데이터 email, birth, gender 데이터 형식 확인 후 가입 가능한 상태인지 최종 판단
     * @return 가입 가능한 상태이면 user 객체를 반환
     */
    @SuppressLint("LongLogTag")
    private fun isAvailable(user: UserInfoResponse) : User? {
        if(validatedNickname() && validatedPhone()) {
            val nickname = binding.profileEditNicknameEt.text.toString()
            val phone = binding.profileEditPhoneEt.text.toString()
            Log.d(TAG, "isAvailable 사용자 비밀번호: ${user.password}")
            Log.d(TAG, "isAvailable: ${phone}")
            Log.d(TAG, "isAvailable: ${userEmail}")
            Log.d(TAG, "isAvailable: ${userBirth}")
            Log.d(TAG, "isAvailable: ${userGender}")
            if(userEmail == null)
                userEmail = ""
            if(userBirth == null)
                userBirth = ""
            if(!(userGender == "M" || userGender == "F"))
                userGender = ""

            var userImg : String = ""
            if(user.img == null) {
                userImg = ""
            } else {
                userImg = user.img!!
            }

            return User(user.id, user.password, nickname, phone, userEmail, userBirth, userGender, user.type, userImg)
        } else {
            return null
        }
    }

    /**
     * 사용자의 Birth가 null이 아니면 Spinner에 초기값 표시해줄 Index 반환
     */
    fun getSpinnerIndex(spinner: Spinner, str: String) : Int{
        for(i in 0 until spinner.count) {
            if(spinner.getItemAtPosition(i).toString().equals(str)) {
                return i
            }
        }
        return 0
    }

    /**
     * 입력된 nickname 길이 및 빈 칸 체크
     * @return 통과 시 true 반환
     */
    private fun validatedNickname() : Boolean{
        val inputNickname = binding.profileEditNicknameEt.text.toString()

        if(inputNickname.trim().isEmpty()){
            binding.profileEditNicknameLayout.error = "Required Field"
            binding.profileEditNicknameEt.requestFocus()
            return false
        } else if(inputNickname.length > 100) {
            binding.profileEditNicknameLayout.error = "Nickname 길이를 100자 이하로 설정해 주세요."
            binding.profileEditNicknameEt.requestFocus()
            return false
        }
        else {
            binding.profileEditNicknameLayout.error = null
            return true
        }
    }

    /**
     * 입력된 phone number 유효성 검사
     * @return 유효성 검사 통과 시 true 반환
     */
    private fun validatedPhone() : Boolean{
        val inputPhone = binding.profileEditPhoneEt.text.toString()

        if(inputPhone.trim().isEmpty()){
            binding.profileEditPhoneLayout.error = "Required Field"
            binding.profileEditPhoneEt.requestFocus()
            return false
        } else if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", inputPhone)) {
            binding.profileEditPhoneLayout.error = "휴대폰 번호 형식을 확인해주세요."
            binding.profileEditPhoneEt.requestFocus()
            return false
        }
        else {
            binding.profileEditPhoneLayout.error = null
            return true
        }
    }


    /**
     * email 입력 데이터 검사
     * @return email 형식이면 email(String), 아니면 null
     */
    private fun validatedEmail(){
        val inputEmail = binding.profileEditEmailEt.text.toString()
        val inputDomain = binding.profileEditEmailDomain.text.toString()

        val email = "$inputEmail@$inputDomain"

        if(inputEmail.trim().isEmpty()) {
            binding.profileEditEmailLayout.error = "Required Email Field"
        } else if(inputDomain.trim().isEmpty()) {
            binding.profileEditEmailLayout.error = "Required Domain Field"
        }
        else if(!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}\$", email)) {
            binding.profileEditEmailLayout.error = "이메일 형식을 확인해주세요."
        }
        else {
            binding.profileEditEmailLayout.error = null
            binding.profileEditDomainLayout.error = null
            userEmail = email
        }

    }

    /**
     * email domain list set Adapter
     */
    private fun initDomain() {
        // 자동완성으로 보여줄 내용들
        var domains = arrayOf("gmail.com", "naver.com", "nate.com", "daum.net")

        var adapter = ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, domains)
        binding.profileEditEmailDomain.setAdapter(adapter)
    }

    /**
     * birth spinner item 선택 이벤트
     * year, month, day가 선택되면 birth 형식으로 변환한다.
     */
    private fun selectedBirth() {
        var year = ""
        var month = ""
        var day = ""

        val yearSpinner = binding.profileEditSpinnerYear
        val monthSpinner = binding.profileEditSpinnerMonth
        val daySpinner = binding.profileEditSpinnerDay

        // year
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        year = ""
                    }
                    else -> {
                        year = yearSpinner.selectedItem.toString()
                        // birth 형식 맞추기
                        if(year != "" && month != "" && day != "") {  // 전부 선택 되어서 데이터가 들어있으면
                            userBirth = "$year-$month-$day"
                        }
                    }
                }
            }
        }

        // month
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        month = ""
                    }
                    else -> {
                        month = monthSpinner.selectedItem.toString()
                        // birth 형식 맞추기
                        if(year != "" && month != "" && day != "") {  // 전부 선택 되어 데이터가 들어있으면
                            userBirth = "$year-$month-$day"
                        }
                    }
                }
            }
        }

        // day
        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        day = ""
                    }
                    else -> {
                        day = daySpinner.selectedItem.toString()
                        // birth 형식 맞추기
                        if(year != "" && month != "" && day != "") {  // 전부 선택 되어 데이터가 들어있으면
                            userBirth = "$year-$month-$day"
                        }
                    }
                }
            }
        }
    }

    /**
     * radio group 내 선택된 gender item 데이터 변환
     */
    private fun selectedGender() {
        binding.profilEditGenderGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                com.ssafy.groute.R.id.profile_edit_radio_man -> {
                    userGender = "M"
                }
                com.ssafy.groute.R.id.profile_edit_radio_woman -> {
                    userGender = "F"
                }
            }
        }
    }
}