package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.inheritance.BaseFragment


class ReplyFragment : BaseFragment() {




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_reply, container, false)
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

    fun getReplyData(){

    }


}