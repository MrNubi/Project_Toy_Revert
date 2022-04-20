package com.beyond.project_toy_revert

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        Log.d("옹", Context_okhttp.getToken(mContext))


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
    var mBackWait: Long = 0
    override fun onBackPressed() {
        // 뒤로가기버튼클릭

        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(baseContext, "한번더누르시면종료됩니다", Toast.LENGTH_SHORT).show()
        } else {moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());}}
}