package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentMyBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserService

private const val TAG = "MyFragment_groute"
class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding
    private lateinit var mainActivity:MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = MyTravelTabPageAdapter(this)
        val tabList = arrayListOf("My Travel", "Shared Travel", "Save Travel")

        pagerAdapter.addFragment(MyTravelFragment())
        pagerAdapter.addFragment(SharedTravelFragment())
        pagerAdapter.addFragment(SaveTravelFragment())

        binding.myVpLayout.adapter = pagerAdapter
        TabLayoutMediator(binding.mypagetablayout, binding.myVpLayout){tab, position ->
            tab.text = tabList.get(position)
        }.attach()

        initUserInfo()
    }

    // 마이페이지 사용자 정보 갱신
    fun initUserInfo() {
        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.myProfileId.text = "${user.id}"
        val userInfo = UserService().getUserInfo(user.id)
        userInfo.observe(
            viewLifecycleOwner,
            {
                Glide.with(this)
                    .load("${ApplicationClass.IMGS_URL_USER}${it.img}")
                    .circleCrop()
                    .into(binding.myProfileImg)
            }
        )
    }


}