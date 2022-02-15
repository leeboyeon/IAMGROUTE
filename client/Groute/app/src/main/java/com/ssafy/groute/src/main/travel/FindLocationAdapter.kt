package com.ssafy.groute.src.main.travel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewFindlocationListItemBinding
import com.ssafy.groute.src.dto.Place

class FindLocationAdapter : RecyclerView.Adapter<FindLocationAdapter.LocationHolder>() {

    var list = listOf<Place>()

    inner class LocationHolder(private var binding:RecyclerviewFindlocationListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: Place){
            binding.place = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        return LocationHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recyclerview_findlocation_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val dto = list[position]
        holder.apply {
            bindInfo(dto)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}