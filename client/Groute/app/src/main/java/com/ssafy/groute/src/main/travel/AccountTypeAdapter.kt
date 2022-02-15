package com.ssafy.groute.src.main.travel

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewAccountBycategorybyListItemBinding
import com.ssafy.groute.src.dto.Account
import java.io.FilterReader

class AccountTypeAdapter(var list:MutableList<Account>) : RecyclerView.Adapter<AccountTypeAdapter.AccountTypeHolder>(),Filterable{
    var filteredList = list
    var unfilteredList = list
    var size = -1
    inner class AccountTypeHolder(private var binding:RecyclerviewAccountBycategorybyListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data:Account){
            binding.account = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountTypeHolder {
        return AccountTypeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_account_bycategoryby_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: AccountTypeHolder, position: Int) {
        val dto = filteredList[position]
        holder.apply {
            bindInfo(dto)
        }
    }

    override fun getItemCount(): Int {
//        Log.d("AccountTypeAdatper", "getItemCount: ${filteredList.size}")

        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object :Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                filteredList = if(charString.isEmpty()){
                    unfilteredList
                }else{
                    val filteringList = ArrayList<Account>()
                    for(item in unfilteredList){
                        if(item.categoryName == constraint) filteringList.add(item)
                    }
                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                size = filteredList.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Account>
                notifyDataSetChanged()
            }

        }
    }
}