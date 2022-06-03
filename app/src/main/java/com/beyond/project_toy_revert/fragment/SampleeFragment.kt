package com.beyond.project_toy_revert.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.AnnounceListAdapter
import com.beyond.project_toy_revert.adapter.reReplyAdapter
import com.beyond.project_toy_revert.adapter.sampleReplyAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.board.BoardShowActivity
import com.beyond.project_toy_revert.board.BoardWriteActivity
import com.beyond.project_toy_revert.databinding.FragmentAnnounceBinding
import com.beyond.project_toy_revert.databinding.FragmentSampleBinding
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.replyDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import com.beyond.project_toy_revert.util.Context_okhttp
import org.json.JSONObject


class SampleeFragment : BaseFragment() {

    lateinit var binding : FragmentSampleBinding

    private var replyList = mutableListOf<replyDataModel>()
    private lateinit var AFAdapter : sampleReplyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sample, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getListDataAndAdapterSetting()
        setValues()
        setupEvents()

    }//onActivityCreated
    override fun setValues() {


    }//setValues


    override fun setupEvents() {


    }//setupEvents

    fun getListDataAndAdapterSetting(){
        for(i in 0..5){
            var Data : replyDataModel = replyDataModel(

            "clone3",
            "샘플메세지입니다 ${i}",
            i,
            null


            )
            replyList.add(Data)
        }//for
//                    AnnounceList.reverse()// 정렬 뒤집기
        activity?.runOnUiThread{
            AFAdapter = sampleReplyAdapter(replyList)
            binding.LVFragSample.adapter = AFAdapter

        }
    }//fun getData()
}
