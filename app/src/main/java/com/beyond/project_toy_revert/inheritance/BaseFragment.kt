package com.beyond.project_toy_revert.inheritance

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment


// BaseActivity 처럼, 프레그먼트의 기능 + a를 모아두는 클래스
// 모든 프래그먼트의 공통 기능 추가

abstract class BaseFragment :Fragment() {

    lateinit var mContext:Context



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext =requireContext() // 모든 화면정보 -> mContext를 대신 활용



    }

    abstract fun setupEvents()
    abstract fun setValues()
}