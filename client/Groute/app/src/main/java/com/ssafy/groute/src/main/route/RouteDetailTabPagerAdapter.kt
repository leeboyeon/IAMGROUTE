package com.ssafy.groute.src.main.route

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RouteDetailTabPagerAdapter (fragmnet: Fragment) : FragmentStateAdapter(fragmnet) {
    var fragments : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    fun addFragment(fragmnet: Fragment){
        fragments.add(fragmnet)
        notifyItemInserted(fragments.size-1)
    }
    fun removeFragment(){
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }
}