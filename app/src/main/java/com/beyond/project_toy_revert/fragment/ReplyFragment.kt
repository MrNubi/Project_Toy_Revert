package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.util.Log
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
import java.time.LocalDateTime


class ReplyFragment : BaseFragment() {
    private lateinit var binding : FragmentReplyBinding

    private var replyList = mutableListOf<replyDataModel>()
    private var rereplyList = mutableListOf<replyDataModel>()
    private lateinit var curruntReplyAdapter: replyAdapter
    private lateinit var curruntReReplyAdapter: replyAdapter

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
        setupEvents()

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
                            binding.edtReplyFragComment.setText("")
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
         val replyId_here = replyList[i].replyId
            serverUtil_okhttp.postReReply(mContext,replyId_here.toString(),"메세지자리",object :serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                    if(RcCode == "201"){
                        activity?.runOnUiThread{
                            Toast.makeText(mContext, "댓글작성완료", Toast.LENGTH_SHORT).show()
                            binding.edtReplyFragComment.setText("")

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



        }

    }

    override fun setValues() {
    }

    fun getReplyData(){
//        replyList = mutableListOf<replyDataModel>()
        val timeNow = LocalDateTime.now().toString()
            .replace("-","")
            .replace("T","")
            .replace(":","")+"999"

        Log.d("현재 시간",timeNow.toString())
        serverUtil_okhttp.getReplyData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode == "200"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    var size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                        var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                        var replyID = json_objdetail.getInt("id")
                        var replyParant:String? =json_objdetail.getString("parent")?:null
                        var reply_createdAt = json_objdetail.getString("created_at")
                                                                    .replace("-","")
                                                                    .replace("T","")
                                                                    .replace(":","")
                                                                    .replace("+0900","")
                       var timeChecker : Double=  timeNow.toDouble() - reply_createdAt.toDouble()

                        Log.d("시간",reply_createdAt)

                        Log.d("시간_t/r",reply_createdAt.toDouble().toString())
                        Log.d("시간_checker",timeChecker.toString())
                        var Data : replyDataModel = replyDataModel(
                            author_reply,
                            json_objdetail.getString("message"),
                            replyID,
                            replyParant
                        )

                        replyList.add(Data)
                    }//for
                    Log.d("데이터리스트", replyList.toString())
                    replyList.reverse()// 정렬 뒤집기
                    activity?.runOnUiThread{
                        curruntReplyAdapter = replyAdapter(mContext, replyList)
                        curruntReplyAdapter.notifyDataSetChanged()
                        binding.LVReplyFrag.adapter = curruntReplyAdapter

                    }
                }
            }
        })
    }
    fun getReReplyData(replyid:String){

        serverUtil_okhttp.getReReplyData(mContext,replyid,object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode == "200"){

                    val resultAnnounce = jsonObject.getJSONArray("results")
                    val resultCount = jsonObject.getInt("count")
                    if (resultCount!=0){
                    var size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                        var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                        var replyID = json_objdetail.getInt("id")
                        var replyParant:String? =json_objdetail.getString("parent")?:null
                        var reply_createdAt = json_objdetail.getString("created_at")
                            .replace("-","")
                            .replace("T","")
                            .replace(":","")
                            .replace(".","")
                            .replace("+0900","")



                        Log.d("시간",reply_createdAt)

                        var Data : replyDataModel = replyDataModel(
                            author_reply,
                            json_objdetail.getString("message"),
                            replyID,
                            replyParant
                        )

                        rereplyList.add(Data)
                    }//for
                    Log.d("데이터리스트", rereplyList.toString())
                    replyList.reverse()// 정렬 뒤집기
                    activity?.runOnUiThread{
                        curruntReReplyAdapter = replyAdapter(mContext, rereplyList)
                        curruntReReplyAdapter.notifyDataSetChanged()
                        binding.LVReplyFrag.adapter = curruntReplyAdapter

                    }
                    }//if count !=0
                    if(resultCount==0){

                    }
                }//if code == 200
            }
        })
    }


}