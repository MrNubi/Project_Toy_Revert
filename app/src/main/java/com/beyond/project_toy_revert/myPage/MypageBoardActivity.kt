package com.beyond.project_toy_revert.myPage

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.MainFragAdapter
import com.beyond.project_toy_revert.adapter.MyPageFragmentSwipAdapter
import com.beyond.project_toy_revert.databinding.ActivityMypageBoardBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp

class MypageBoardActivity : BasicActivity() {
    private lateinit var binding : ActivityMypageBoardBinding

    lateinit var myPageAdapter : MyPageFragmentSwipAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mypage_board)
        myPageAdapter = MyPageFragmentSwipAdapter(supportFragmentManager)
        binding.viewPager2MyPageFragmentSwiperZone.adapter =myPageAdapter
        binding.tabMyPageFragmentSwiperTab.setupWithViewPager(binding.viewPager2MyPageFragmentSwiperZone)
        binding.viewPager2MyPageFragmentSwiperZone.setCurrentItem(1)

    }
}