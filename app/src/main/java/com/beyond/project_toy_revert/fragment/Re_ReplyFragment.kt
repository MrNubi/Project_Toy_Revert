package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.AnnounceListAdapter
import com.beyond.project_toy_revert.adapter.replyAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.FragmentReplyBinding
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.replyDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import org.json.JSONObject


class Re_ReplyFragment : BaseFragment() {
    private lateinit var binding : FragmentReplyBinding

    private var replyList = mutableListOf<replyDataModel>()
    private lateinit var curruntReplyAdapter: replyAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reply, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getReplyData()
        setValues()
//        setupEvents()

    }

    override fun setupEvents() {
        binding.btnReplyFragCommentPush.setOnClickListener {
            val replyText = binding.edtReplyFragComment.text.toString()
            serverUtil_okhttp.postReply(mContext,replyText,object :serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                    if(RcCode == "201"){
                        activity?.runOnUiThread{
                            Toast.makeText(mContext, "댓글작성완료", Toast.LENGTH_SHORT).show()
                            getReplyData()
                        }

                    }
                    else{
                        activity?.runOnUiThread{
                            Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            })
        }//btnReplyFragCommentPush.sOCL
        binding.LVReplyFrag.setOnItemClickListener { adapterView, view, i, l ->


        }

    }

    override fun setValues() {

    }

    fun getReplyData(){
        replyList = mutableListOf<replyDataModel>()
        serverUtil_okhttp.getReplyData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode == "200"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    var size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                        var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                        var Data : replyDataModel = replyDataModel(
                            author_reply,
                            json_objdetail.getString("message"),

                        )

                        replyList.add(Data)
                    }//for
//                    AnnounceList.reverse()// 정렬 뒤집기
                    activity?.runOnUiThread{
                        curruntReplyAdapter = replyAdapter(replyList)
                        curruntReplyAdapter.notifyDataSetChanged()
                        binding.LVReplyFrag.adapter = curruntReplyAdapter

                    }
                }
            }
        })
    }


}