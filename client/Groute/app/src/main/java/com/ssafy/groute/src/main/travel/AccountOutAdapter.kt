package com.ssafy.groute.src.main.travel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewAccountOutBinding
import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.src.dto.AccountOut
import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.src.dto.UserPlan
import java.text.SimpleDateFormat

private const val TAG = "AccountOutAdapter"
class AccountOutAdapter(val context:Context) : RecyclerView.Adapter<AccountOutAdapter.AccountOutHolder>(){
    var list = mutableListOf<AccountOut>()
    inner class AccountOutHolder(private var binding:RecyclerviewAccountOutBinding):RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: AccountOut){
            Log.d(TAG, "bindInfo: ${data}")
            binding.dto = data
            if(data.account.isEmpty()){
                var nulllist = mutableListOf<Account>()
                binding.accountInRv.adapter = AccountInAdapter(context, nulllist)

            }else{
                binding.accountInRv.adapter = AccountInAdapter(context,
                    data.account as MutableList<Account>
                )
                Log.d(TAG, "bindInfo: ${data.account}")
            }
            binding.accountInRv.layoutManager = LinearLayoutManager(context)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountOutHolder {
        return AccountOutHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.recyclerview_account_out,parent,false))
    }

    override fun onBindViewHolder(holder: AccountOutHolder, position: Int) {
        val dto = list[position]
        holder.apply{
            bindInfo(dto)
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${list.size}")
        return list.size
    }
}