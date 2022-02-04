package com.ssafy.groute.src.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentLoginBinding
import com.ssafy.groute.databinding.FragmentTravelPlanBinding
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.util.RetrofitCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

private const val TAG = "LoginFragment_Groute"
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {
    private lateinit var loginActivity: LoginActivity
//    private lateinit var binding: FragmentLoginBinding

    // google 로그인
    private lateinit var mAuth: FirebaseAuth
    var mGoogleSignInClient: GoogleSignInClient? = null

    // naver 로그인
    lateinit var mOAuthLoginInstance : OAuthLogin

    // kakao 로그인
//    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val pass = view.findViewById<EditText>(R.id.editTextTextPersonName2)


        binding.loginTvJoin.setOnClickListener{
            loginActivity.openFragment(2)
        }

        binding.loginBtnLogin.setOnClickListener {
            login(id.text.toString(), pass.text.toString())
        }


        // 구글 계정으로 로그인 버튼 클릭
        binding.loginIbtnGoogle.setOnClickListener {
            initAuth()
        }

        // 네이버 계정으로 로그인 버튼 클릭
        mOAuthLoginInstance = OAuthLogin.getInstance()
        mOAuthLoginInstance.init(
            requireContext(),
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            getString(R.string.naver_client_name))
        binding.loginIbtnNaver.setOnClickListener {
            mOAuthLoginInstance.startOauthLoginActivity(requireActivity(), mOAuthLoginHandler);
        }

        // 카카오 계정으로 로그인 버튼 클릭
        var disposables = CompositeDisposable()
        binding.loginIbtnKakao.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.rx.loginWithKakaoTalk(requireContext())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext { error ->
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Single.error(error)
                        } else {
                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.rx.loginWithKakaoAccount(requireContext())
                        }
                    }.observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ token ->
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        successKakao()
                    }, { error ->
                        Log.e(TAG, "로그인 실패", error)
                    }).addTo(disposables)
            } else {
                UserApiClient.rx.loginWithKakaoAccount(requireContext())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ token ->
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        successKakao()
                    }, { error ->
                        Log.e(TAG, "로그인 실패", error)
                    }).addTo(disposables)
            }
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


    // ---------------------------------------------------------------------------------------------
    /**
     * #S06P12D109-13
     * sns Login - Google 로그인
     */
    // 인증 초기화
    private fun initAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_login_key))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        mAuth = FirebaseAuth.getInstance()
        signIn()
    }

    // 구글 로그인 창을 띄우는 작업
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        requestActivity.launch(signInIntent)
    }

    // 구글 인증 결과 획득 후 동작 처리
    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    // 구글 인증 결과 성공 여부에 따른 처리
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    if(user != null) {
                        // id pw nickname phone email, birth, gender, type img
                        Log.d(TAG, ": ${user.phoneNumber} ${user.email} \t ${user.metadata} \t ${user.photoUrl} ${user.uid}")
                        val id = user.email.toString()
                        val pw = user.uid
                        val nickname = user.displayName.toString()
                        var phone = user.phoneNumber.toString()
                        val image = user.photoUrl.toString()
                        if(phone.equals("null")) {
                            phone = ""
                        }
                        Log.d(TAG, "firebaseAuthWithGoogle: $image")
                        val newUser = User(id, pw, nickname, phone, id, "", "", "sns", image)
                        UserService().isUsedId(user.email!!, isUsedIdCallback(newUser))
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }


    /**
     * #S06P12D109-14
     * sns Login - Naver
     */
    // ---------------------------------------------------------------------------------------------
    @SuppressLint("HandlerLeak")
    val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken: String = mOAuthLoginInstance.getAccessToken(requireContext())
                Log.d(TAG, "run: $accessToken")
                RequestApiTask(requireContext(), mOAuthLoginInstance).execute()
            } else {
                val errorCode: String = mOAuthLoginInstance.getLastErrorCode(requireContext()).code
                val errorDesc = mOAuthLoginInstance.getLastErrorDesc(requireContext())
                Log.d(TAG, "run: errorCode:" + errorCode + ", errorDesc:" + errorDesc)
            }
        }
    }


    inner class RequestApiTask(private val mContext: Context, private val mOAuthLoginModule: OAuthLogin) :
        AsyncTask<Void?, Void?, String>() {
        override fun onPreExecute() {}

        override fun onPostExecute(content: String) {
            try {
                val loginResult = JSONObject(content)
                if (loginResult.getString("resultcode") == "00") {
                    val response = loginResult.getJSONObject("response")
                    val id = response.getString("email")
                    val pw = response.getString("id")   // 사용자 식별 정보
                    val nickname = response.getString("nickname")
                    val mobile = response.getString("mobile")
                    val gender = response.getString("gender")
                    val birthYear = response.getString("birthyear")
                    val birthDay = response.getString("birthday")
                    var image = response.getString("profile_image")
                    image = image.replace("\\", "")
                    val newUser = User(id, pw, nickname, mobile, id, "$birthYear-$birthDay", gender, "sns", image)
                    UserService().isUsedId(id, isUsedIdCallback(newUser))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun doInBackground(vararg params: Void?): String {
            val url = "https://openapi.naver.com/v1/nid/me"
            val at = mOAuthLoginModule.getAccessToken(mContext)
            return mOAuthLoginModule.requestApi(mContext, at, url)
        }
    }


    /**
     * #S06P12D109-6
     * sns Login - Kakao
     */
    // ---------------------------------------------------------------------------------------------
    private fun successKakao() {
        var disposables = CompositeDisposable()
        // 사용자 정보 요청 (기본)
        UserApiClient.rx.me()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user ->
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +  // pw
                        "\n이메일: ${user.kakaoAccount?.email}" +  // id, email
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +  // nickname
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +   // image
                        "\n성별 : ${user.kakaoAccount?.gender}" +
                        "\n생일 : ${user.kakaoAccount?.birthyear} - ${user.kakaoAccount?.birthday}")

                val id = user.kakaoAccount?.email.toString()
                val pw = user.id.toString()
                val nickname = user.kakaoAccount?.profile?.nickname.toString()
                var gender = user.kakaoAccount?.gender.toString()
                val image = user.kakaoAccount?.profile?.thumbnailImageUrl.toString()
                if(gender.equals("null")) {
                    gender = ""
                }
                val newUser = User(id, pw, nickname, "", id, "", gender, "sns", image)
                UserService().isUsedId(id, isUsedIdCallback(newUser))
            }, { error ->
                Log.e(TAG, "사용자 정보 요청 실패", error)
            })
            .addTo(disposables)
    }

    /**
     * sns 로그인 시 id(or email)로 id 사용여부(회원인지 아닌지)를 확인
     * 사용된 id이면 사용자 인증 정보로 로그인
     * 사용되지 않은 id이면 사용자 인증 정보로 회원 가입 진행, 회원 가입 성공 시 로그인 수행
     */
    // 사용자가 입력한 userId를 인자아서 id 중복 체크
    inner class isUsedIdCallback(val user: User) : RetrofitCallback<Boolean> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            Log.d(TAG, "onSuccess IsUsedId: $responseData")  // 0 : 중복 X, 사용가능 <-> 1 : 중복되는 ID, 사용불가능
            if(responseData){   // 중복되는 id가 있으면 이미 가입된 회원 -> 로그인 수행
                login(user.id, user.password)
            } else {
                signUp(user)
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }


    // 서버로 사용자 정보 보낸 후 성공하면 login 수행
    private fun signUp(newUser: User){
        UserService().signUp(newUser, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, t.message?:"회원가입 통신오류")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                login(newUser.id, newUser.password)
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: resCode $code")
            }
        })
    }
}