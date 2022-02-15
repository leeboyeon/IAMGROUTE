package com.ssafy.groute.src.main.travel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.RouteRecom

private const val TAG = "RouteRecomDialogAdapter"
class RouteRecomDialogAdapter(val context: Context) : RecyclerView.Adapter<RouteRecomDialogAdapter.RouteRecomHolder>(){
    var list = arrayListOf<RouteRecom>()

    inner class RouteRecomHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindInfo(data: RouteRecom){

            itemView.findViewById<LottieAnimationView>(R.id.routeRom_lottie_fileName).setAnimation(data.lottie)
            itemView.findViewById<TextView>(R.id.routeRom_tv_type).text = data.typeName
            itemView.findViewById<TextView>(R.id.routeRom_tv_descript).text = data.typeDescript

            if(adapterPosition == 0 || adapterPosition == list.size+1){
                itemView.findViewById<ConstraintLayout>(R.id.itemLayout).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val displayMetrics = context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val mLayoutParam:RecyclerView.LayoutParams = itemView.findViewById<ConstraintLayout>(R.id.itemLayout).layoutParams
                        as RecyclerView.LayoutParams
                if(adapterPosition == 0){
                    mLayoutParam.leftMargin = (screenWidth - itemView.findViewById<ConstraintLayout>(R.id.itemLayout).measuredHeightAndState)/2
                }else{
                    mLayoutParam.rightMargin = (screenWidth - itemView.findViewById<ConstraintLayout>(R.id.itemLayout).measuredHeightAndState)/2
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteRecomHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_recomroute_list_item,parent,false)
        return RouteRecomHolder(view)
    }

    override fun onBindViewHolder(holder: RouteRecomHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
            var clickFlag = false
            itemView.setOnClickListener {
                if(clickFlag == false){
                    itemClickListener.onClick(it, position,list[position].typeName)
                    Log.d(TAG, "onBindViewHolder1: $position")
                    holder.itemView.findViewById<FrameLayout>(R.id.check_layout).visibility = View.VISIBLE
                    clickFlag = true
                }else{
                    Log.d(TAG, "onBindViewHolder2: $position")
                    holder.itemView.findViewById<FrameLayout>(R.id.check_layout).visibility = View.INVISIBLE
                    clickFlag = false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ItemClickListener{
        fun onClick(view:View, position: Int,type:String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

}