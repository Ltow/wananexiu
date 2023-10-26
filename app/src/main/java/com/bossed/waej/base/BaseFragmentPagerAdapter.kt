package com.bossed.waej.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT 只加载当前fragment
 */
class BaseFragmentPagerAdapter(fm: FragmentManager, fragmentList: MutableList<Fragment>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList: MutableList<Fragment> = ArrayList()

    init {
        this.fragmentList = fragmentList
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
}