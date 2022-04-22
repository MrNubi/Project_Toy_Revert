package com.beyond.project_toy_revert.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.beyond.project_toy_revert.fragment.AnnounceFragment
import com.beyond.project_toy_revert.fragment.GalleryFragment
import com.beyond.project_toy_revert.fragment.HitFragment
import com.beyond.project_toy_revert.fragment.ReplyFragment


class replyFragViewpagerAdapter(fm:FragmentManager) :FragmentPagerAdapter(fm) {
    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> ReplyFragment()
            1 -> AnnounceFragment()

            else -> AnnounceFragment()
        }
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return when (position) {
//
//            0-> "Gallery"
//            1 -> "Announce"
//
//            else -> "Announce"
//        }
//    }

}