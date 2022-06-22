package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.replyAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.FragmentReplyBinding
import com.beyond.project_toy_revert.datas.replyDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import com.google.android.material.internal.ContextUtils
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
        getAllReplyData()
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
                            getAllReplyData()
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
         val replyParent_here = replyList[i].replyParent
            if(replyParent_here=="null"){
                val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edt, null)
                val mBuilder = AlertDialog.Builder(mContext)
                    .setView(mDialogView)
                    .setTitle("대댓글입력")

                val alertDialog = mBuilder.show()
                alertDialog.findViewById<ImageView>(R.id.btn_dialog_commentPush)?.setOnClickListener {
                    val msg = alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.text.toString()
                    val replyId_here = replyList[i].replyId
                    serverUtil_okhttp.postReReply(mContext,replyId_here.toInt(),msg,object :serverUtil_okhttp.JsonResponseHandler_login{
                        override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                            if(RcCode == "201"){


                                activity?.runOnUiThread{
                                    Toast.makeText(context, "대댓글작성완료", Toast.LENGTH_SHORT).show()
                                    getAllReplyData()}

                            }
                            else{
                                Log.d("대댓글 애러",RcCode)
                            }

                        }
                    })


                    alertDialog.dismiss()
                }//btn_dialog_loginYes




            }//if(replyParent_here=="null")
           else{
                Toast.makeText(mContext, "지금은 댓글에만 대댓글을 달 수 있습니다", Toast.LENGTH_SHORT).show()
           }
        }//sOICL


    }

    override fun setValues() {
    }

    fun getReplyData(){
      replyList = mutableListOf<replyDataModel>()
        val timeNow = LocalDateTime.now().toString()
            .replace("-","")
            .replace("T","")
            .replace(":","")+"999"

        Log.d("현재 시간",timeNow.toString())
        serverUtil_okhttp.getReplyData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode == "200"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    val size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        val json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
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
                            replyID.toFloat(),
                            json_objdetail.getString("message"),
                            author_reply,
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

    fun getAllReplyData(){
//        replyList = mutableListOf<replyDataModel>()
        val timeNow = LocalDateTime.now().toString()
            .replace("-","")
            .replace("T","")
            .replace(":","")+"999"

        Log.d("현재 시간",timeNow.toString())
        serverUtil_okhttp.getAllReplyData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode == "200"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    var size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                        var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                        var replyID = json_objdetail.getInt("id")
                        var replyParant=json_objdetail.get("parent")
                        Log.d("${replyParant}","리플리_parent")
                        var last_repyId:Float = if("${replyParant}"=="null")replyID.toFloat() else "${replyParant}.${replyID}".toFloat()
                        Log.d("${last_repyId}","리플리_lastId")
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
                            last_repyId,
                            json_objdetail.getString("message"),
                            author_reply,
                            "${replyParant}"
                        )

                        replyList.add(Data)
                        Log.d("시발",Data.replyId.toString()+" / "+Data.replyParent+" / "+Data.replyMessage)
                    }//for
                    Log.d("데이터리스트", replyList.toString())
                    replyList = replyList.sortedBy{it.replyId} as MutableList<replyDataModel>
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
                    val size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        val json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
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
                            replyID.toFloat(),
                            json_objdetail.getString("message"),
                            author_reply,
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