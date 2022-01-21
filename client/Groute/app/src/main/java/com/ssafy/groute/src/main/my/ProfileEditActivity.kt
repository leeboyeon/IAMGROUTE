package com.ssafy.groute.src.main.my

import android.R
import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.ActivityProfileEditBinding
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import java.util.regex.Pattern

private const val TAG = "ProfileEditActivity_groute"
class ProfileEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileEditBinding
    private var userEmail : String = ""
    private var userBirth : String = ""
    private var userGender : String? = ""
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
        userGender = userData.gender

        binding.profileEditFinish.setOnClickListener {
            var user = isAvailable(userData)
            if(user != null) {
                finish()
                updateUser(user)
            } else {
                Toast.makeText(this, "입력 값을 다시 확인해 주세요", Toast.LENGTH_LONG).show()
            }
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

    @SuppressLint("LongLogTag")
    fun initData(user: UserInfoResponse) {
        if(user.img != null) {
            Glide.with(this)
                .load("${ApplicationClass.IMGS_URL}${user.img}")
                .circleCrop()
                .into(binding.profileEditImg)
        }
        binding.profileEditIdEt.setText(user.id)
        binding.profileEditPasswordEt.setText(user.password)
        binding.profileEditNicknameEt.setText(user.nickname)
        binding.profileEditPhoneEt.setText(user.phone)

        if(user.email != null) {
            if (user.email == "") {
                binding.profileEditEmailEt.hint = "이메일을 입력해주세요."
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
                Log.d(TAG, "initData: ${birthStr.get(0)}  ${birthStr.get(1)}  ${birthStr.get(2)}")
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
        UserService().updateUserInfo(user.id, user, userUpdateCallback())

    }

    inner class userUpdateCallback: RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData) {
                Toast.makeText(this@ProfileEditActivity, "프로필 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()

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
     * #S06P12D109-24
     * 필수 데이터 id 유효성 검사 및 중복 확인, pw, nickname, phone 유효성 통과 여부와
     * 선택 데이터 email, birth, gender 데이터 형식 확인 후 가입 가능한 상태인지 최종 판단
     * @return 가입 가능한 상태이면 user 객체를 반환
     */
    @SuppressLint("LongLogTag")
    private fun isAvailable(user: UserInfoResponse) : User? {
        if(validatedNickname() && validatedPhone()) {
            val nickname = binding.profileEditNicknameEt.text.toString()
            val phone = binding.profileEditPhoneEt.text.toString()
            Log.d(TAG, "isAvailable: ${phone}")
            Log.d(TAG, "isAvailable: ${userEmail}")
            Log.d(TAG, "isAvailable: ${userBirth}")
            Log.d(TAG, "isAvailable: ${userGender}")
            if(userEmail == null)
                userEmail = ""
            if(userBirth == null)
                userBirth = ""
            if(userGender == null)
                userGender = ""
            return User(user.id, user.password, nickname, phone, userEmail, userBirth, userGender, "none")

            //phone, birth, gender sns

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
     * #S06P12D109-25
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
     * #S06P12D109-79
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
     * #S06P12D109-79
     * email domain list set Adapter
     */
    private fun initDomain() {
        // 자동완성으로 보여줄 내용들
        var domains = arrayOf("gmail.com", "naver.com", "nate.com", "daum.net")

        var adapter = ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, domains)
        binding.profileEditEmailDomain.setAdapter(adapter)
    }

    /**
     * #S06P12D109-25
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
     * #S06P12D109-80
     * radio group 내 선택된 gender item 데이터 변환
     */
    private fun selectedGender() {
        binding.profilEditGenderGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                com.ssafy.groute.R.id.join_radio_man -> {
                    userGender = "M"
                }
                com.ssafy.groute.R.id.join_radio_woman -> {
                    userGender = "F"
                }
            }
        }
    }
}