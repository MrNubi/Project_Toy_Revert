package com.beyond.project_toy_revert.inheritance

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

abstract class BasicActivity : AppCompatActivity(){

    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        mContext = this  // 화면이 만들어질때 this를 대입


     }
}