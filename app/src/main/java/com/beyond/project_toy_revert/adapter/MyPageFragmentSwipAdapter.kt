package com.beyond.project_toy_revert.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.beyond.project_toy_revert.fragment.AnnounceFragment
import com.beyond.project_toy_revert.fragment.GalleryFragment
import com.beyond.project_toy_revert.fragment.SamplyWriteDownFragment
import com.beyond.project_toy_revert.myPage.MypageFrag.MypageMyLikeFragment
import com.beyond.project_toy_revert.myPage.MypageFrag.MypageMyPostFragment
import com.beyond.project_toy_revert.myPage.MypageFrag.MypageMyReplyFragment
import com.beyond.project_toy_revert.myPage.MypageFrag.MypageMyScrapFragment

class MyPageFragmentSwipAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount() = 4

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> MypageMyPostFragment()
            1 -> MypageMyReplyFragment()
            2 -> MypageMyLikeFragment()
            3 -> MypageMyScrapFragment()
            else -> AnnounceFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {

            0-> "내가 쓴 글"
            1 -> "내가 쓴 댓글"
            2 -> "좋아요 한 글"
            3 -> "스크랩"
            else -> "내가 쓴 글"
        }
    }

}