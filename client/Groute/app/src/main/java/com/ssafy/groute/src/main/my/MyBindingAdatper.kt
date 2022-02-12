//package com.ssafy.groute.src.main.my
//
//import android.widget.ImageView
//import androidx.databinding.BindingAdapter
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//
//class MyBindingAdatper {
//    companion object {
//        @JvmStatic
//        @BindingAdapter("imageRes")
//        fun imgLoad(view: ImageView, imageUrl: String?) {
//            Glide.with(view.context)
//                .load(imageUrl)
//                .circleCrop()
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(view)
//        }
//    }
//}