package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.FragmentAnnounceBinding
import com.beyond.project_toy_revert.inheritance.BaseFragment


class AnnounceFragment : BaseFragment() {

    lateinit var binding : FragmentAnnounceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_announce, container, false)
        return binding.root
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
