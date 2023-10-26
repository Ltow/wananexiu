package com.bossed.waej.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(private val date: MutableList<View>) : PagerAdapter() {
    override fun getCount(): Int {
        return if (date.isEmpty()) 0 else date.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(date[position])
        return date[position]
    }
}