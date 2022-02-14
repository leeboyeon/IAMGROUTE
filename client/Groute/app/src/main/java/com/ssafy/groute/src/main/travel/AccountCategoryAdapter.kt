package com.ssafy.groute.src.main.travel


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ssafy.groute.R
import com.ssafy.groute.databinding.RecyclerviewAccountCategoryListItemBinding
import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.src.dto.AccountCategory

class AccountCategoryAdapter(var selectList: ArrayList<Int>): RecyclerView.Adapter<AccountCategoryAdapter.ACategoryHolder>(){
    var list = mutableListOf<AccountCategory>()
    inner class ACategoryHolder(private var binding:RecyclerviewAccountCategoryListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: AccountCategory, position: Int){
            binding.category= item
            binding.executePendingBindings()

            if(selectList[position] == 1) {
                itemView.findViewById<LottieAnimationView>(R.id.checklottie_account).visibility = View.VISIBLE
            } else {
                itemView.findViewById<LottieAnimationView>(R.id.checklottie_account).visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ACategoryHolder {
        return ACategoryHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_account_category_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ACategoryHolder, position: Int) {
        val item = list[position]
        holder.apply {
            itemView.setOnClickListener {
                if(selectList[position] == 0){
                    for(i in 0 until list.size) {
                        if(selectList[i] == 1) {
                            selectList[i] = 0
                        }
                    }
                    selectList[position] = 1
                    itemClickListener.onClick(it,position,list[position].id)
                }else{
                    for(i in 0 until list.size) {
                            selectList[i] = 0
                    }
                }
            }
            bind(item, position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ItemClickListener{
        fun onClick(view:View,position: Int,id:Int)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}