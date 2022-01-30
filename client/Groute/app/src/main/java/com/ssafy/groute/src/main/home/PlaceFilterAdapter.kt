package com.ssafy.groute.src.main.home

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewAreaPlaceItemBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.util.CommonUtils

private const val TAG = "AreaFilterAdapter"
//class AreaFilterAdapter() : RecyclerView.Adapter<AreaFilterAdapter.AreaViewHolder>(),
//class AreaFilterAdapter(var placeList:MutableList<Place>) : RecyclerView.Adapter<AreaFilterAdapter.AreaViewHolder>(),
class PlaceFilterAdapter(var placeList : MutableList<Place>) : ListAdapter<Place, PlaceFilterAdapter.PlaceViewHolder>(DiffCallback),
    Filterable {
    private var unFilteredList = placeList
    private var filteredList = placeList
//    private var context:Context?=null
//    var list = listOf<Place>()
//    var isHeart = false

    override fun getFilter(): Filter {
        return object  : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //event
                val charString = constraint.toString()
//                Log.d(TAG, "performFiltering: ${charString}")
                filteredList = if(charString.isEmpty()){
                    unFilteredList
                }else{
                    val filteringList = ArrayList<Place>()
                    for(item in unFilteredList){
//                        Log.d(TAG, "performFiltering: ${item}")
                        if(item.type.contains(charString)) filteringList.add(item)
                    }
                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
//                Log.d(TAG, "performFiltering: ${filterResults.values}")
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Place>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_area_place_item,parent,false)
//        return AreaViewHolder(view)
        return PlaceViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_area_place_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.apply {
//            bindInfo(filteredList[position])
            bindInfo(filteredList[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position, filteredList[position].id)
            }
//            bindInfo(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    /**
     * bindingAdapter에서
     * adapter.setHasStableIds(true)
     * 사용할 때 필요
     */
    override fun getItemId(position: Int): Long {
        return filteredList.get(position).id.toLong()
    //        return position.toLong()
    }

    inner class PlaceViewHolder(private var binding:RecyclerviewAreaPlaceItemBinding) : RecyclerView.ViewHolder(binding.root){
//        val heartLottie = itemView.findViewById<LottieAnimationView>(R.id.area_abtn_heart)

        fun bindInfo(place : Place){
            binding.place = place
            binding.executePendingBindings()
//            Glide.with(itemView)
//                .load("${ApplicationClass.IMGS_URL_PLACE}${data.img}")
//                .into(itemView.findViewById(R.id.areaPlace_iv_img))
//
            itemView.findViewById<TextView>(R.id.areaPlace_tv_name).text =CommonUtils.getFormattedTitle(place.name)
            itemView.findViewById<TextView>(R.id.areaPlace_tv_content).text = CommonUtils.getFormattedDescription(place.description)
////            itemView.findViewById<TextView>(R.id.areaPlace_rb_rating) = data.review
//            itemView.findViewById<TextView>(R.id.areaPlace_tv_info).text = data.type

            // place 좋아요 버튼 클릭 이벤트(Lottie)
            binding.areaAbtnHeart.setOnClickListener {
                val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
                animator.addUpdateListener { animation ->
                    binding.areaAbtnHeart.progress = animation.animatedValue as Float
                }
                animator.start()
            }
        }
    }



    interface ItemClickListener{
        fun onClick(view:View, position: Int, placeId: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }


    object DiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }
    }

}