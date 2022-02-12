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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentUserIdFindBinding
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import java.util.regex.Pattern


private const val TAG = "UserIdFindFragment"
class UserIdFindFragment : BaseFragment<FragmentUserIdFindBinding>(FragmentUserIdFindBinding::bind, R.layout.fragment_user_id_find) {
    private lateinit var loginActivity: LoginActivity
    private var userEmail : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.useridFindSelectIdBtn.setOnClickListener {
            if(userEmail != "") {
                UserService().getUserIdByEmail(userEmail, object: RetrofitCallback<UserInfoResponse> {
                    override fun onError(t: Throwable) {
                        Log.d(TAG, "onError: ")
                    }

                    override fun onSuccess(code: Int, responseData: UserInfoResponse) {
                        Log.d(TAG, "onSuccess: $responseData")
                        binding.useridFindTxt2.isVisible = true
                        binding.useridFindTxt3.isVisible = true
                        binding.useridFindSelectIdBtn.isVisible = false
                        binding.useridFindGoLoginBtn.isVisible = true
                        binding.useridFindTxt3.text = "아이디: ${responseData.id}"

                        binding.useridFindGoLoginBtn.setOnClickListener {
                            (requireActivity() as LoginActivity).onBackPressed()
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
        binding.useridFindEmailTe.addTextChangedListener(TextFieldValidation(binding.useridFindEmailTe))
        binding.useridFindDomainTe.addTextChangedListener(TextFieldValidation(binding.useridFindDomainTe))

        initDomain() // email domain list adapter
    }

    private fun initDomain() {
        // 자동완성으로 보여줄 내용들
        var domains = arrayOf("gmail.com", "naver.com", "nate.com", "daum.net")

        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, domains)
        binding.useridFindDomainTe.setAdapter(adapter)
    }

    private fun validatedEmail(){
        val inputEmail = binding.useridFindEmailTe.text.toString()
        val inputDomain = binding.useridFindDomainTe.text.toString()

        val email = "$inputEmail@$inputDomain"

        if(inputEmail.trim().isEmpty()) {
            binding.useridFindEmailTl.error = "Required Email Field"
        } else if(inputDomain.trim().isEmpty()) {
            binding.useridFindDomainTl.error = "Required Domain Field"
        }
        else if(!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}\$", email)) {
            binding.useridFindEmailTl.error = "이메일 형식을 확인해주세요."
        }
        else {
            binding.useridFindEmailTl.error = null
            binding.useridFindDomainTl.error = null
            userEmail = email
        }

    }

    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when(view.id){
                R.id.userid_find_email_te -> {
                    validatedEmail()
                }
                R.id.userid_find_domain_te -> {
                    validatedEmail()
                }
            }
        }
    }

}