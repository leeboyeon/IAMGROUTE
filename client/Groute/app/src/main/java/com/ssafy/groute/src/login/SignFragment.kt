package com.ssafy.groute.src.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentSignBinding
import java.util.regex.Pattern


class SignFragment : Fragment() {
    private lateinit var binding: FragmentSignBinding


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
    }


    /**
     * TextInputEditText listener 등록
     */
    private fun initListeners() {

        binding.joinEditId.addTextChangedListener(TextFieldValidation(binding.joinEditId))
        binding.joinEditPw.addTextChangedListener(TextFieldValidation(binding.joinEditPw))
        binding.joinEditEmail.addTextChangedListener(TextFieldValidation(binding.joinEditEmail))
        binding.joinEditPhone.addTextChangedListener(TextFieldValidation(binding.joinEditPhone))
//        binding.joinEditNickname.addTextChangedListener(TextFieldValidation(binding.joinEditNickname))
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
        }
        return true
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
//                    validatedId()
                }
                R.id.join_edit_pw -> {
//                    validatedPw()
                }
                R.id.join_edit_phone -> {
                    validatedPhone()
                }
//                R.id.join_edit_nickname -> {
//                    validatedNickname()
//                }
            }
        }
    }
}