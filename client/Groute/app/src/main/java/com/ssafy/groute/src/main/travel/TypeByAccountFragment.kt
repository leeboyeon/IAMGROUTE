package com.ssafy.groute.src.main.travel

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentTypeByAccountBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel

private const val TAG = "TypeByAccountFragment"
class TypeByAccountFragment : BaseFragment<FragmentTypeByAccountBinding>(FragmentTypeByAccountBinding::bind,R.layout.fragment_type_by_account) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
        Log.d(TAG, "onAttach: ${planId}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChart()
    }
    fun initChart(){
        var pieChart = binding.accountChart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5F,10F,5F,5F)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = false
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 61f
    }
    companion object {

        @JvmStatic
        fun newInstance(key: String, value:Int) =
            TypeByAccountFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}