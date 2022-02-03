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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.databinding.RecyclerviewAreaPlaceItemBinding
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.response.PlaceLikeResponse
import com.ssafy.groute.src.service.PlaceService
import com.ssafy.groute.src.viewmodel.PlaceViewModel
import com.ssafy.groute.util.CommonUtils
import kotlinx.coroutines.runBlocking

private const val TAG = "AreaFilterAdapter"
class PlaceFilterAdapter(var placeList : MutableList<Place>, var likeList: LiveData<MutableList<Place>>, var owner: LifecycleOwner) : ListAdapter<Place, PlaceFilterAdapter.PlaceViewHolder>(DiffCallback),
    Filterable {
    private var unFilteredList = placeList
    private var filteredList = placeList
    private lateinit var placeViewModel:PlaceViewModel
    private var heartHashMap:HashMap<Int,Boolean> = HashMap()
    override fun getFilter(): Filter {
        return object  : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //event
                val charString = constraint.toString()
                filteredList = if(charString.isEmpty()){
                    unFilteredList
                }else{
                    val filteringList = ArrayList<Place>()
                    for(item in unFilteredList){
                        if(item.type.contains(charString)) filteringList.add(item)
                    }
                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Place>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_area_place_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.apply {
            bindInfo(filteredList[position], position)
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position, filteredList[position].id)
            }

            val heart = itemView.findViewById<LottieAnimationView>(R.id.area_abtn_heart)

            heart.setOnClickListener {
                heartClickListener.onClick(it,position,filteredList[position].id)
                if(heart.progress > 0F){
                    Log.d(TAG, "onBindViewHolder: 이미 클릭됨")
                    heart.pauseAnimation()
                    heart.progress = 0F
                }else{
                    Log.d(TAG, "onBindViewHolder: 클릭할거얌")
                    val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(500)
                    animator.addUpdateListener { animation ->
                        heart.progress = animation.animatedValue as Float
                    }
                    animator.start()
                }
            }
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
        fun bindInfo(place : Place, position: Int){
            binding.place = place
            binding.executePendingBindings()
//            Glide.with(itemView)
//                .load("${ApplicationClass.IMGS_URL_PLACE}${data.img}")
//                .into(itemView.findViewById(R.id.areaPlace_iv_img))
//
            itemView.findViewById<TextView>(R.id.areaPlace_tv_name).text =CommonUtils.getFormattedTitle(place.name)
            itemView.findViewById<TextView>(R.id.areaPlace_tv_content).text = CommonUtils.getFormattedDescription(place.description)
            itemView.findViewById<LottieAnimationView>(R.id.area_abtn_heart).progress = 0F
////            itemView.findViewById<TextView>(R.id.areaPlace_rb_rating) = data.review
//            itemView.findViewById<TextView>(R.id.areaPlace_tv_info).text = data.type

            val itemAtPosition1 = filteredList[position]
            val actualPosition1 = placeList.indexOf(itemAtPosition1)

            likeList.observe(owner, Observer {
                for(i in 0 until placeList.size-1){
                    for(j in 0 until it.size){
                        if(placeList[i].id == likeList.value!![j].id){
                            if(actualPosition1 == i ){
                                itemView.findViewById<LottieAnimationView>(R.id.area_abtn_heart).progress = 0.5F
                            }
                        }
                    }
                }
            })

        }
    }

    interface ItemClickListener{
        fun onClick(view:View, position: Int, placeId: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    interface HeartClickListener{
        fun onClick(view:View, position: Int, placeId: Int)
    }
    private lateinit var heartClickListener : HeartClickListener
    fun setHeartClickListener(heartClickListener: HeartClickListener){
        this.heartClickListener = heartClickListener
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