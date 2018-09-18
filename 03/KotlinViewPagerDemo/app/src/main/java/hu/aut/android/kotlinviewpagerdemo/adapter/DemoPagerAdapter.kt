package hu.aut.android.kotlinviewpagerdemo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import hu.aut.android.kotlinviewpagerdemo.fragment.FragmentMain
import hu.aut.android.kotlinviewpagerdemo.fragment.FragmentDetails
import android.support.v4.app.FragmentPagerAdapter


class DemoPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentMain()
            1 -> FragmentDetails()
            else -> FragmentMain()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "Main page" else "Details page"
    }

    override fun getCount(): Int {
        return 2
    }
}
