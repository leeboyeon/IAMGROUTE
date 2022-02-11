package com.ssafy.groute.src.main.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ssafy.groute.R
import com.ssafy.groute.src.dto.Magazine


class MagazineAdapter : RecyclerView.Adapter<MagazineAdapter.MagazineHolder>(){
    var list = mutableListOf<Magazine>()
    inner class MagazineHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindInfo(data : Magazine){
            val option = MultiTransformation(CenterCrop(),RoundedCorners(30))
            Glide.with(itemView)
                .load(data.img)
                .apply(RequestOptions.bitmapTransform(option))
                .into(itemView.findViewById(R.id.magazine_iv_img))

            itemView.findViewById<TextView>(R.id.magazine_tv_title).text = data.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagazineHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_board_magazine_litem,parent,false)
        return MagazineHolder(view)
    }

    override fun onBindViewHolder(holder: MagazineHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, name: String)
    }
    private lateinit var itemClickListener : ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
}