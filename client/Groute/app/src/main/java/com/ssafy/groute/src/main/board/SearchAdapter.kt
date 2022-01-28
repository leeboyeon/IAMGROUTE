package com.ssafy.groute.src.main.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Places

private const val TAG = "SearchAdapter"
class SearchAdapter(var places:List<Places>, var context:Context) : RecyclerView.Adapter <SearchAdapter.SearchHolder>(),
    Filterable {

    var placeFilterList:List<Places> = arrayListOf()
    init{
        placeFilterList = places
    }
    inner class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindInfo(data: Places){
            itemView.findViewById<TextView>(R.id.search_tv_type).text = data.type
            itemView.findViewById<TextView>(R.id.search_tv_Name).text = data.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_place_search,parent,false)
        return SearchHolder(view)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.apply {
            bindInfo(placeFilterList[position])
            itemView.setOnClickListener {
                itemClickListener.onClick(it,position,placeFilterList[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return placeFilterList.size
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
                Log.d(TAG, "performFiltering: ${charString}")
                if(charString.isEmpty()){
                    placeFilterList = places
                }else{
                    val resultList = ArrayList<Places>()
                    for(item in places){
                        Log.d(TAG, "performFiltering: ${item}")
                        if(item!!.name.contains(charString)) resultList.add(item)
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = placeFilterList
                Log.d(TAG, "performFiltering: ${filterResults.values}")
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                placeFilterList = results?.values as ArrayList<Places>
                notifyDataSetChanged()
            }

        }
    }
}