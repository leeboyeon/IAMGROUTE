package com.ssafy.groute.src.main.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentMyBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.response.UserInfoResponse
import com.ssafy.groute.src.service.UserService

private const val TAG = "MyFragment_groute"
class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding
    private lateinit var mainActivity:MainActivity
    private lateinit var userInfoResponse: UserInfoResponse
    private lateinit var intent: Intent
    private lateinit var viewModel: MyViewModel


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
        userInfo.observe(
            viewLifecycleOwner,
            {
                userInfoResponse = it
            }
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentMyBinding.inflate(inflater,container,false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = MyTravelTabPageAdapter(this)
        val tabList = arrayListOf("My Travel", "Shared Travel", "Save Travel")

        pagerAdapter.addFragment(MyTravelFragment())
        pagerAdapter.addFragment(SharedTravelFragment())
        pagerAdapter.addFragment(SaveTravelFragment())

        intent = Intent(mainActivity, ProfileEditActivity::class.java)

        binding.myVpLayout.adapter = pagerAdapter
        TabLayoutMediator(binding.mypagetablayout, binding.myVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        initUserInfo()

        binding.myEditProfileTv.setOnClickListener {
            intent.putExtra("userData", userInfoResponse)
            startActivity(intent)
        }
    }

    // 마이페이지 사용자 정보 갱신
    fun initUserInfo() {
//        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        viewModel.initData(this)
        //binding.myProfileId.text = "${user.id}"
//        val userInfo = UserService().getUserInfo(user.id)
//        userInfo.observe(
//            viewLifecycleOwner, {
//                if(it.type.equals("sns")){
//                    Glide.with(this)
//                        .load(it.img)
//                        .circleCrop()
//                        .into(binding.myProfileImg)
//                } else{
//                    Log.d(TAG, "initProfileBar: ${it.img}")
//                    Glide.with(this)
//                        .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
//                        .circleCrop()
//                        .into(binding.myProfileImg)
//                }
//                userInfoResponse = it
//            }
//        )
//        userInfo.observe(
//            viewLifecycleOwner,
//            {
//                Glide.with(this)
//                    .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
//                    .circleCrop()
//                    .into(binding.myProfileImg)
//
//                userInfoResponse = it
//            }
//        )
    }


}