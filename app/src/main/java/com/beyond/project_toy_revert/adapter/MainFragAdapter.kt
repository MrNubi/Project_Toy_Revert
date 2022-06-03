package com.beyond.project_toy_revert.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.beyond.project_toy_revert.fragment.AnnounceFragment
import com.beyond.project_toy_revert.fragment.GalleryFragment
import com.beyond.project_toy_revert.fragment.HitFragment
import com.beyond.project_toy_revert.fragment.SampleeFragment


class MainFragAdapter(fm:FragmentManager) :FragmentPagerAdapter(fm) {
    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> GalleryFragment()
            1 -> AnnounceFragment()
            2 -> SampleeFragment()
            else -> AnnounceFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {

            0-> "Gallery"
            1 -> "Announce"
            2 -> "sample"
            else -> "Announce"
        }
    }

}