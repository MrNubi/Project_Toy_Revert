package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.HitRecyclerAdapter
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment


class HitFragment : BaseFragment() {
private lateinit var HitAdapter : HitRecyclerAdapter
private lateinit var HITlist : MutableList<HitRecyclerDataModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setValues()
        setupEvents()

    }

    override fun setupEvents() {

    }

    override fun setValues() {
    }


}