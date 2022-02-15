package com.ssafy.groute.src.main.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewPlaceSearchBinding
import com.ssafy.groute.src.dto.Place

private const val TAG = "SearchAdapter"
class SearchAdapter(var places: List<Place>) : RecyclerView.Adapter <SearchAdapter.SearchHolder>(),
    Filterable {

    private var placeFilterList = places

    inner class SearchHolder(private val binding:RecyclerviewPlaceSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(data: Place){
            binding.place = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_place_search,parent,false)
//        return SearchHolder(view)
        return SearchHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_place_search, parent, false))
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.apply {
//            bindInfo(placeFilterList.value!!.get(position))
            bindInfo(placeFilterList[position])

            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,placeFilterList[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return placeFilterList.size
    }

    override fun getItemId(position: Int): Long {
        return placeFilterList.get(position).id.toLong()
        //        return position.toLong()
    }

    interface ItemClickListener{
        fun onClick(view:View, position: Int, id: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun getFilter(): Filter {
        return object  : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //event
                val charString = constraint.toString()

                placeFilterList = if(charString.isEmpty()){
                    places
                }else{
                    val resultList = ArrayList<Place>()

                    for(item in places){

                        if(item.name.contains(charString)){
                            Log.d(TAG, "performFiltering: ${item.name}")
                            resultList.add(item)
                        }

                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = placeFilterList
                Log.d(TAG, "performFiltering: ${filterResults.values}")
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                placeFilterList = results?.values as List<Place>
                notifyDataSetChanged()
            }

        }
    }
}
