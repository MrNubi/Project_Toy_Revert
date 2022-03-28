package com.beyond.project_toy_revert

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.adapter.MainFragAdapter
import com.beyond.project_toy_revert.databinding.ActivityMainBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp

class MainActivity : BasicActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mAdpater : MainFragAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        mAdpater = MainFragAdapter(supportFragmentManager)
        binding.viewMainv2.adapter =mAdpater
        binding.tabMainTop.setupWithViewPager(binding.viewMainv2)
        binding.viewMainv2.setCurrentItem(1)



        binding.imgMainProf.setOnClickListener {
            Context_okhttp.setToken(mContext,"TOKEN")
            Context_okhttp.setID(mContext, "")
            // + 서버 로그아웃 활성화
            val myIntent =  Intent(mContext, SplashActivity::class.java)
            startActivity(myIntent)
        }



//        val id = Context_okhttp.getID(mContext)
//        binding.textTest.text = "${id}님을 환영합니다"
//
//
//
//        binding.btnEscape.setOnClickListener {
//
//
//            Context_okhttp.setToken(mContext,"TOKEN")
//            Context_okhttp.setID(mContext, "")
//            // + 서버 로그아웃 활성화
//            val myIntent =  Intent(mContext, SplashActivity::class.java)
//            startActivity(myIntent)
//        }
    }
}