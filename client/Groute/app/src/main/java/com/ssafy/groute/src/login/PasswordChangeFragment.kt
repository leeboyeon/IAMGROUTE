package com.ssafy.groute.src.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentPasswordChangeBinding
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import java.util.regex.Pattern

private const val TAG = "PasswordChangeFragment"
class PasswordChangeFragment : BaseFragment<FragmentPasswordChangeBinding>(FragmentPasswordChangeBinding::bind, R.layout.fragment_password_change) {
    private lateinit var loginActivity: LoginActivity
    private var userEmail : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        binding.userChangePassBtn.setOnClickListener {
            if(validatedId() && validatedPw() && userEmail != "") {
                val id = binding.userChangePassTe.text.toString()
                Log.d(TAG, "onViewCreated: $userEmail , $id")
                UserService().isUserEmailAndId(userEmail, id, object : RetrofitCallback<Boolean> {
                    override fun onError(t: Throwable) {
                        Log.d(TAG, "onError: ")
                    }

                    override fun onSuccess(code: Int, responseData: Boolean) {
                        Log.d(TAG, "onSuccess: $responseData")
                        if(responseData) {
                            updatePassword()
                        } else {
                            Toast.makeText(requireContext(), "email과 id에 맞는 정보가 없습니다.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(code: Int) {
                        Log.d(TAG, "onFailure: ")
                    }

                })
            } else {
                Toast.makeText(requireContext(), "입력 값을 다시 확인해 주세요", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initListeners() {
        binding.userChangePassTe.addTextChangedListener(TextFieldValidation(binding.userChangePassTe))
        binding.userChangePassPasswordTe.addTextChangedListener(TextFieldValidation(binding.userChangePassPasswordTe))
        binding.userChangePassEmailEt.addTextChangedListener(TextFieldValidation(binding.userChangePassEmailEt))
        binding.userChangePassDomainTe.addTextChangedListener(TextFieldValidation(binding.userChangePassDomainTe))

        initDomain() // email domain list adapter
    }

    private fun validatedId() : Boolean {
        val inputId = binding.userChangePassTe.text.toString()

        if (inputId.trim().isEmpty()) {   // 값이 비어있으면
            binding.userChangePassTl.error = "Required Field"
            binding.userChangePassTe.requestFocus()
            return false
        } else if(!Pattern.matches("^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{4,20}\$", inputId)) {
            binding.userChangePassTl.error = "아이디 형식을 확인해주세요.(특수 문자 제외)"
            binding.userChangePassTe.requestFocus()
            return false
        } else {
            binding.userChangePassTl.isErrorEnabled = false
            Log.d(TAG, "validatedId: id = $inputId")
            return true
        }
    }

    private fun validatedPw() : Boolean {
        val inputPw = binding.userChangePassPasswordTe.text.toString()

        if(inputPw.trim().isEmpty()){   // 값이 비어있으면
            binding.userChangePassPasswordTl.error = "Required Field"
            binding.userChangePassPasswordTe.requestFocus()
            return false
        } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@!%*#?&]).{8,50}.\$", inputPw)) {
            binding.userChangePassPasswordTl.error = "비밀번호 형식을 확인해주세요."
            binding.userChangePassPasswordTe.requestFocus()
            return false
        }
        else {
            binding.userChangePassPasswordTl.isErrorEnabled = false
            return true
        }
    }

    private fun initDomain() {
        // 자동완성으로 보여줄 내용들
        var domains = arrayOf("gmail.com", "naver.com", "nate.com", "daum.net")

        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, domains)
        binding.userChangePassDomainTe.setAdapter(adapter)
    }

    private fun validatedEmail(){
        val inputEmail = binding.userChangePassEmailEt.text.toString()
        val inputDomain = binding.userChangePassDomainTe.text.toString()

        val email = "$inputEmail@$inputDomain"

        if(inputEmail.trim().isEmpty()) {
            binding.userChangePassEmailTl.error = "Required Email Field"
        } else if(inputDomain.trim().isEmpty()) {
            binding.userChangePassDomainTl.error = "Required Domain Field"
        }
        else if(!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}\$", email)) {
            binding.userChangePassEmailTl.error = "이메일 형식을 확인해주세요."
        }
        else {
            binding.userChangePassEmailTl.error = null
            binding.userChangePassDomainTl.error = null
            userEmail = email
        }

    }

    fun updatePassword() {
        val id = binding.userChangePassTe.text.toString()
        val password = binding.userChangePassPasswordTe.text.toString()
        UserService().updateUserPassword(UserInfoResponse(id, password), object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                Log.d(TAG, "onSuccess: ")
                Toast.makeText(requireContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
                (requireActivity() as LoginActivity).onBackPressed()
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when(view.id){
                R.id.user_change_pass_te -> {
                    validatedId()
                }
                R.id.user_change_pass_password_te -> {
                    validatedPw()
                }
                R.id.user_change_pass_email_et -> {
                    validatedEmail()
                }
                R.id.user_change_pass_domain_te -> {
                    validatedEmail()
                }
            }
        }
    }


}