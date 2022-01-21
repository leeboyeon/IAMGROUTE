package com.ssafy.groute.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.FragmentInfoBinding
import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.util.RetrofitCallback

private const val TAG = "InfoFragment"
class InfoFragment : Fragment() {
    private var placeId = -1
    private lateinit var mainActivity : MainActivity
    private lateinit var binding: FragmentInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            placeId = it.getInt("placeId", -1)
            Log.d(TAG, "onAttach: $placeId")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }
    fun initData(){
        Log.d(TAG, "initData: $placeId")
        val placesDetail = PlaceService().getPlace(placeId, placesCallback())
    }
    companion object {

        @JvmStatic
        fun newInstance(key:String, value:Int) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(key,value)
                }
            }
    }
    inner class placesCallback : RetrofitCallback<Places> {
        override fun onError(t: Throwable) {
            Log.d(TAG, "onError: ")
        }

        override fun onSuccess(code: Int, responseData: Places) {
            Log.d(TAG, "onSuccess: ${responseData}")
            binding.placeDetailTvDescript.text = responseData.description
            binding.infoTvAddr.text = responseData.address
            if(responseData.contact == "" || responseData.contact ==null){
                binding.infoTvPhone.text = "없음"
            }else{
                binding.infoTvPhone.text = responseData.contact
            }

            binding.placeDetailTvBigContent.text = responseData.name+"은"
            Glide.with(this@InfoFragment)
                .load("${ApplicationClass.IMGS_URL_PLACE}${responseData.img}")
                .into(binding.placeDetailIvSomenail)
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onFailure: $code")

        }

    }
}