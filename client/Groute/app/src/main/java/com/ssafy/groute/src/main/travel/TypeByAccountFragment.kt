package com.ssafy.groute.src.main.travel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.ssafy.groute.R
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentTypeByAccountBinding
import com.ssafy.groute.src.dto.AccountChart
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.CommonUtils
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "TypeByAccountFragment"
class TypeByAccountFragment : BaseFragment<FragmentTypeByAccountBinding>(FragmentTypeByAccountBinding::bind,R.layout.fragment_type_by_account) {
    private lateinit var mainActivity: MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    var sum = 0
    var cnt = 0
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
        runBlocking {
            planViewModel.getCategoryChart(planId)
        }

        initChart()
    }
    fun sumPrice(){
        var price = planViewModel.accountPriceList.value
        sum = 0
        for(i in 0..price!!.size-1){
            sum += price[i]
            if(price[i] == 0){
                cnt++
            }
        }
    }
    fun initChart(){
        sumPrice()
        planViewModel.accountPriceList.observe(viewLifecycleOwner, Observer {
            with(binding.accountChart) {
                this.renderer = CustomPieChartRenderer(this, 10f)
                setExtraOffsets(20f, 30f, 20f, 20f)

                //dataSet
                val yValues:ArrayList<PieEntry> = ArrayList()
                with(yValues){
                    add(PieEntry(it[0].toFloat(),"카페"))
                    add(PieEntry(it[1].toFloat(),"식당"))
                    add(PieEntry(it[2].toFloat(),"쇼핑"))
                    add(PieEntry(it[3].toFloat(),"항공"))
                    add(PieEntry(it[4].toFloat(),"교통"))
                    add(PieEntry(it[5].toFloat(),"관광"))
                    add(PieEntry(it[6].toFloat(),"숙소"))
                    add(PieEntry(it[7].toFloat(),"기타"))
                }
                Log.d(TAG, "initChart: ${cnt}")
                while(cnt-- > 0){
                    var i = 0
                    if(yValues[i].value == 0.0f){
                        yValues.removeAt(i)
                    }
                    i++
                    if(cnt == 0){
                        break;
                    }
                }

                animateY(1000, Easing.EaseInOutCubic)
                var colors = arrayListOf<Int>(
                    Color.parseColor("#FFE162"),
                    Color.parseColor("#FF6464"),
                    Color.parseColor("#91C483"),
                    Color.parseColor("#A2D2FF"),
                    Color.parseColor("#FEF9EF"),
                    Color.parseColor("#FF865E"),
                    Color.parseColor("#FEE440"),
                    Color.parseColor("#EEEEEE")
                )

                val dataSet:PieDataSet = PieDataSet(yValues, "")
                dataSet.valueFormatter = object : ValueFormatter() {
                    private val formatter = DecimalFormat("###,###,##")
                    override fun getFormattedValue(value: Float) : String{
                        return formatter.format(value) + "%"
                    }
                    override fun getPieLabel(value: Float, pieEntry: PieEntry?) : String{
                        if(this@with != null && this@with.isUsePercentValuesEnabled()){
                            return getFormattedValue(value)
                        }else{
                            return formatter.format(value)
                        }
                    }
                }

                with(dataSet) {
                    setColors(colors)
                    setValueTextColors(colors)
                    valueLinePart1Length = 0.6f
                    valueLinePart2Length = 0.3f
                    valueLineWidth = 2f
                    valueLinePart1OffsetPercentage = 115f
                    isUsingSliceColorAsValueLineColor = true
                    yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    valueTextSize = 16f
                    valueTypeface = Typeface.DEFAULT_BOLD
                    selectionShift = 3f
                }


                setUsePercentValues(true)
                isDrawHoleEnabled = true
                holeRadius = 50f
                setDrawCenterText(true)
                setCenterTextSize(20f)
                setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                setCenterTextColor(Color.parseColor("#222222"))
                centerText = "총금액\n ${CommonUtils.makeComma(sum)}"
                legend.isEnabled = true
                description = null
                var l = legend
                l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                l.orientation = Legend.LegendOrientation.HORIZONTAL
                l.setDrawInside(false)

                val pieData = PieData(dataSet)
//                with(pieData){
//                    setValueTextSize(10f)
//                    setValueTextColor(Color.BLACK)
//                    invalidate()
//                    setDrawEntryLabels(false)
//                    contentDescription = ""
//                    setEntryLabelTextSize(12f)
//                }

                data = pieData

            }
        })

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
    inner class CustomPieChartRenderer(pieChart: PieChart, val circleRadius: Float)
        : PieChartRenderer(pieChart, pieChart.animator, pieChart.viewPortHandler) {

        override fun drawValues(c: Canvas) {
            super.drawValues(c)

            val center = mChart.centerCircleBox

            val radius = mChart.radius
            var rotationAngle = mChart.rotationAngle
            val drawAngles = mChart.drawAngles
            val absoluteAngles = mChart.absoluteAngles

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            val roundedRadius = (radius - radius * mChart.holeRadius / 100f) / 2f
            val holeRadiusPercent = mChart.holeRadius / 100f
            var labelRadiusOffset = radius / 10f * 3.6f

            if (mChart.isDrawHoleEnabled) {
                labelRadiusOffset = (radius - radius * holeRadiusPercent) / 2f
                if (!mChart.isDrawSlicesUnderHoleEnabled && mChart.isDrawRoundedSlicesEnabled) {
                    rotationAngle += roundedRadius * 360 / (Math.PI * 2 * radius).toFloat()
                }
            }

            val labelRadius = radius - labelRadiusOffset

            val dataSets = mChart.data.dataSets

            var angle: Float
            var xIndex = 0

            c.save()
            for (i in dataSets.indices) {
                val dataSet = dataSets[i]
                val sliceSpace = getSliceSpace(dataSet)
                for (j in 0 until dataSet.entryCount) {
                    angle = if (xIndex == 0) 0f else absoluteAngles[xIndex - 1] * phaseX
                    val sliceAngle = drawAngles[xIndex]
                    val sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius)
                    angle += (sliceAngle - sliceSpaceMiddleAngle / 2f) / 2f

                    if (dataSet.valueLineColor != ColorTemplate.COLOR_NONE) {
                        val transformedAngle = rotationAngle + angle * phaseY
                        val sliceXBase = cos(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
                        val sliceYBase = sin(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
                        val valueLinePart1OffsetPercentage = dataSet.valueLinePart1OffsetPercentage / 100f
                        val line1Radius = if (mChart.isDrawHoleEnabled) {
                            (radius - radius * holeRadiusPercent) * valueLinePart1OffsetPercentage + radius * holeRadiusPercent
                        } else {
                            radius * valueLinePart1OffsetPercentage
                        }
                        val px = line1Radius * sliceXBase + center.x
                        val py = line1Radius * sliceYBase + center.y

                        if (dataSet.isUsingSliceColorAsValueLineColor) {
                            mRenderPaint.color = dataSet.getColor(j)
                        }
                        c.drawCircle(px, py, circleRadius, mRenderPaint)
                    }

                    xIndex++
                }
            }
            MPPointF.recycleInstance(center)
            c.restore()
        }
    }
}