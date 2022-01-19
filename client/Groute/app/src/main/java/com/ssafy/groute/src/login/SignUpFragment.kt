package com.ssafy.groute.src.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentSignBinding
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import java.util.regex.Pattern

private const val TAG = "SignFragment_싸피"
class SignFragment : Fragment() {
    private lateinit var binding: FragmentSignBinding
    private var isAvailableId = false
    private var userEmail : String = ""
    private var userBirth : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        binding.joinBtnJoin.setOnClickListener {
            Log.d(TAG, "onViewCreated: ${validatedEmail()}")

        }
    }


    /**
     * TextInputEditText listener 등록
     */
    private fun initListeners() {
        binding.joinEditId.addTextChangedListener(TextFieldValidation(binding.joinEditId))
        binding.joinEditPw.addTextChangedListener(TextFieldValidation(binding.joinEditPw))
        binding.joinEditNick.addTextChangedListener(TextFieldValidation(binding.joinEditNick))
        binding.joinEditPhone.addTextChangedListener(TextFieldValidation(binding.joinEditPhone))
        binding.joinEditEmail.addTextChangedListener(TextFieldValidation(binding.joinEditEmail))
        binding.joinAutoTvDomain.addTextChangedListener(TextFieldValidation(binding.joinAutoTvDomain))

        initDomain() // email domain list adapter
        selectedBirth() // birth spinner selectedItem listener
    }

    /**
     * 입력된 id 유효성 검사 및 id 중복 체크
     * 유효성 검사 통과하면 id 중복 확인
     * @return id 중복 통과 시 true 반환
     */
    private fun validatedId() : Boolean {
        val inputId = binding.joinEditId.text.toString()

        if (inputId.trim().isEmpty()) {   // 값이 비어있으면
            binding.joinTilId.error = "Required Field"
            binding.joinEditId.requestFocus()
            return false
        } else if(!Pattern.matches("^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{4,20}\$", inputId)) {
            binding.joinTilId.error = "아이디 형식을 확인해주세요.(특수 문자 제외)"
            binding.joinEditId.requestFocus()
            return false
        } else {
            binding.joinTilId.isErrorEnabled = false
            Log.d(TAG, "validatedId: id = $inputId")
            UserService().isUsedId(inputId, isUsedIdCallBack())
            return isAvailableId
        }
    }

    /**
     * 입력된 password 유효성 검사
     * @return 유효성 검사 통과 시 true 반환
     */
    private fun validatedPw() : Boolean {
        val inputPw = binding.joinEditPw.text.toString()

        if(inputPw.trim().isEmpty()){   // 값이 비어있으면
            binding.joinTilPw.error = "Required Field"
            binding.joinEditPw.requestFocus()
            return false
        } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@!%*#?&]).{8,50}.\$", inputPw)) {
            binding.joinTilPw.error = "비밀번호 형식을 확인해주세요."
            binding.joinEditPw.requestFocus()
            return false
        }
        else {
            binding.joinTilPw.isErrorEnabled = false
            return true
        }
    }

    /**
     * #S06P12D109-25
     * 입력된 nickname 길이 및 빈 칸 체크
     * @return 통과 시 true 반환
     */
    private fun validatedNickname() : Boolean{
        val inputNickname = binding.joinEditNick.text.toString()

        if(inputNickname.trim().isEmpty()){
            binding.joinTilNick.error = "Required Field"
            binding.joinEditNick.requestFocus()
            return false
        } else if(inputNickname.length > 100) {
            binding.joinTilNick.error = "Nickname 길이를 100자 이하로 설정해 주세요."
            binding.joinEditNick.requestFocus()
            return false
        }
        else {
            binding.joinTilNick.error = null
            return true
        }
    }

    /**
     * 입력된 phone number 유효성 검사
     * @return 유효성 검사 통과 시 true 반환
     */
    private fun validatedPhone() : Boolean{
        val inputPhone = binding.joinEditPhone.text.toString()

        if(inputPhone.trim().isEmpty()){
            binding.joinTilPhone.error = "Required Field"
            binding.joinEditPhone.requestFocus()
            return false
        } else if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", inputPhone)) {
            binding.joinTilPhone.error = "휴대폰 번호 형식을 확인해주세요."
            binding.joinEditPhone.requestFocus()
            return false
        }
        else {
            binding.joinTilPhone.error = null
            return true
        }
    }


    /**
     * #S06P12D109-79
     * email 입력 데이터 검사
     * @return email 형식이면 email(String), 아니면 null
     */
    private fun validatedEmail(){
        val inputEmail = binding.joinEditEmail.text.toString()
        val inputDomain = binding.joinAutoTvDomain.text.toString()

        var email = "$inputEmail@$inputDomain"

        val pattern = Patterns.EMAIL_ADDRESS
        if(inputEmail.trim().isEmpty()) {
            binding.joinTilEmail.error = "Required Email Field"
        } else if(inputDomain.trim().isEmpty()) {
            binding.joinTilEmail.error = "Required Domain Field"
        }
        else if(!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}\$", email)) {
//        else if(pattern.matcher(email).matches()) {
            binding.joinTilEmail.error = "이메일 형식을 확인해주세요."
        }
        else {
            binding.joinTilEmail.error = null
            binding.joinTilDomain.error = null
            userEmail = email
            Log.d(TAG, "validatedEmail: $userEmail")
        }

//        // 이메일 패턴 체크
//        val pattern = Patterns.EMAIL_ADDRESS
//        Log.d(TAG, "validatedEmail: $email")
//        if (pattern.matcher(email).matches()) {
//            return email
//        } else {
//            return null
//        }
    }

    /**
     * #S06P12D109-79
     * email domain list set Adapter
     */
    private fun initDomain() {
        // 자동완성으로 보여줄 내용들
        var domains = arrayOf("gmail.com", "naver.com", "nate.com", "daum.net")

        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, domains)
        binding.joinAutoTvDomain.setAdapter(adapter)
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

        val yearSpinner = binding.joinSpinnerYear
        val monthSpinner = binding.joinSpinnerMonth
        val daySpinner = binding.joinSpinnerDay

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


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignFragment().apply {

            }
    }


    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when(view.id){
                R.id.join_edit_id -> {
                    validatedId()
                }
                R.id.join_edit_pw -> {
                    validatedPw()
                }
                R.id.join_edit_nick -> {
                    validatedNickname()
                }
                R.id.join_edit_phone -> {
                    validatedPhone()
                }
                R.id.join_edit_email -> {
                    validatedEmail()
                }
                R.id.join_autoTv_domain -> {
                    validatedEmail()
                }
            }
        }
    }

    /**
     * id 중복 확인 callback
     */
    inner class isUsedIdCallBack : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            Log.d(TAG, "onSuccess: $responseData")  // true : 중복 O <-> false : 중복 X, 사용 가능
            if(responseData) {   // true - DB 내에 중복되는 ID가 있으면
                binding.joinTilId.error = "이미 존재하는 아이디입니다."
                binding.joinEditId.requestFocus()
                isAvailableId = false
            } else {// DB 내에 중복되는 ID가 없으면
                binding.joinTilId.error = null
                isAvailableId = true
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }
}