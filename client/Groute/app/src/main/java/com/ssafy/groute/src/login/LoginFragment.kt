package com.ssafy.groute.src.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentLoginBinding
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "LoginFragment_groute"
class LoginFragment : Fragment() {
    private lateinit var loginActivity: LoginActivity
    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater);
        return binding.root    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = view.findViewById<EditText>(R.id.editTextTextPersonName)
        var pass = view.findViewById<EditText>(R.id.editTextTextPersonName2)

        binding.loginTvJoin.setOnClickListener{
            loginActivity.openFragment(2)
        }

        binding.loginBtnLogin.setOnClickListener {
            login(id.text.toString(), pass.text.toString())
        }
    }

    private fun login(loginId: String, loginPass: String) {
        val user = User(loginId, loginPass)
        UserService().login(user, LoginCallback())
    }

    inner class LoginCallback: RetrofitCallback<User> {
        override fun onSuccess( code: Int, user: User) {
            if (user.id != null) {
                Log.d(TAG, "onSuccess: ${user.id}")
                Toast.makeText(context,"로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                ApplicationClass.sharedPreferencesUtil.addUser(user)
                loginActivity.openFragment(1)
            }else{
                Toast.makeText(context,"ID 또는 패스워드를 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")

        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
            Toast.makeText(context, "ID 또는 패스워드를 확인해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}