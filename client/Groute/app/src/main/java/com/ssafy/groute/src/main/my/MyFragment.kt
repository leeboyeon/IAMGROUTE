package com.ssafy.groute.src.main.my

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentMyBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.viewmodel.MyViewModel
import com.ssafy.groute.util.RetrofitCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

private const val TAG = "MyFragment_groute"
class MyFragment : BaseFragment<FragmentMyBinding>(FragmentMyBinding::bind, R.layout.fragment_my) {

    private lateinit var mainActivity:MainActivity
    private lateinit var userInfoResponse: UserInfoResponse
    private lateinit var intent: Intent
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        val userInfo = UserService().getUserInfo(user.id)
        userInfo.observe(viewLifecycleOwner, {
                userInfoResponse = it
            }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = MyTravelTabPageAdapter(this)
        val tabList = arrayListOf("My Travel", "Shared Travel", "Save Travel")
        binding.vm = viewModel
        binding.lifecycleOwner = this
        pagerAdapter.addFragment(MyTravelFragment())
        pagerAdapter.addFragment(SharedTravelFragment())
        pagerAdapter.addFragment(SaveTravelFragment())

        intent = Intent(mainActivity, ProfileEditActivity::class.java)

        binding.myVpLayout.adapter = pagerAdapter
        TabLayoutMediator(binding.mypagetablayout, binding.myVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        initUserInfo()
        initPopup()

        binding.myEditProfileTv.setOnClickListener {
            intent.putExtra("userData", userInfoResponse)
            startActivity(intent)
        }

    }
    fun initPopup(){
        binding.myIbtnMore.setOnClickListener {
            val popup = PopupMenu(context, binding.myIbtnMore)
            MenuInflater(context).inflate(R.menu.my_menu_user_item, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_logout -> {
                        mainActivity.moveFragment(10)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.menu_userDelete ->{
                        showDeleteuserDialog()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }
    }

    // 마이페이지 사용자 정보 갱신
    fun initUserInfo() {
        viewModel.initData(this)
    }

    fun showDeleteuserDialog(){
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("회원 탈퇴")
            .setMessage("정말로 탈퇴하시겠습니까?")
            .setPositiveButton("YES",DialogInterface.OnClickListener{dialogInterface, id ->
                // 탈퇴기능구현
                UserService().deleteUser(ApplicationClass.sharedPreferencesUtil.getUser().id, DeleteCallback())

                val disposables = CompositeDisposable()

                // 연결 끊기
                UserApiClient.rx.unlink()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                    }, { error ->
                        Log.e(TAG, "연결 끊기 실패", error)
                    }).addTo(disposables)
            })
            .setNeutralButton("NO", null)
            .create()

        builder.show()
    }

    inner class DeleteCallback() : RetrofitCallback<Boolean>{
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Boolean) {
            if(responseData){
                Toast.makeText(requireContext(),"탈퇴되었습니다.",Toast.LENGTH_SHORT).show()
                mainActivity.moveFragment(10)
            }
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: ")
        }
    }
}