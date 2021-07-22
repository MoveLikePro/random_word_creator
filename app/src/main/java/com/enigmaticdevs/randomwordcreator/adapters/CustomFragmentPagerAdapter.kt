package com.enigmaticdevs.randomwordcreator.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class CustomFragmentPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mFragment: MutableList<Fragment> = ArrayList()
    private val title: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }
    override fun getCount(): Int {
        return mFragment.size
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
    fun addFragment(fragment: Fragment?, titles: String?) {
        if (fragment != null) {
            mFragment.add(fragment)
        }
        if (titles != null) {
            title.add(titles)
        }
    }
}