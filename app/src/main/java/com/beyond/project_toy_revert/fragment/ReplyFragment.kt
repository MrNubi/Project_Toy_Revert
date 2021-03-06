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
import org.json.JSONArray
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
        getAllReplyData2()
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
                            Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show()
                            getAllReplyData2()
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
                    .setTitle("???????????????")

                val alertDialog = mBuilder.show()
                alertDialog.findViewById<ImageView>(R.id.btn_dialog_commentPush)?.setOnClickListener {
                    val msg = alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.text.toString()
                    val replyId_here = replyList[i].replyId
                    serverUtil_okhttp.postReReply(mContext,replyId_here.toInt(),msg,object :serverUtil_okhttp.JsonResponseHandler_login{
                        override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                            if(RcCode == "201"){


                                activity?.runOnUiThread{
                                    Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show()
                                    getAllReplyData2()}

                            }
                            else{
                                Log.d("????????? ??????",RcCode)
                            }

                        }
                    })


                    alertDialog.dismiss()
                }//btn_dialog_loginYes




            }//if(replyParent_here=="null")
           else{
                Toast.makeText(mContext, "????????? ???????????? ???????????? ??? ??? ????????????", Toast.LENGTH_SHORT).show()
           }
        }//sOICL
        binding.btnReplyPagination1.setOnClickListener {
            val timeNow = LocalDateTime.now().toString()
                .replace("-","")
                .replace("T","")
                .replace(":","")+"999"
            serverUtil_okhttp.getAllReplyDataToPagination(mContext,"1", object :serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                    if(RcCode == "200"){
                        val resultAnnounce = jsonObject.getJSONArray("results")


                            var size: Int = resultAnnounce.length()
                        replyList = mutableListOf<replyDataModel>()
                            for(i in 0..size-1){
                                var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                                var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                                var replyID = json_objdetail.getInt("id")
                                var replyParant=json_objdetail.get("parent")
                                Log.d("${replyParant}","?????????_parent")
                                var last_repyId:Float = if("${replyParant}"=="null")replyID.toFloat() else "${replyParant}.${replyID}".toFloat()
                                Log.d("${last_repyId}","?????????_lastId")
                                var reply_createdAt = json_objdetail.getString("created_at")
                                    .replace("-","")
                                    .replace("T","")
                                    .replace(":","")
                                    .replace("+0900","")
                                var timeChecker : Double=  timeNow.toDouble() - reply_createdAt.toDouble()

                                Log.d("??????",reply_createdAt)

                                Log.d("??????_t/r",reply_createdAt.toDouble().toString())
                                Log.d("??????_checker",timeChecker.toString())
                                var Data : replyDataModel = replyDataModel(
                                    last_repyId,
                                    json_objdetail.getString("message"),
                                    author_reply,
                                    "${replyParant}"
                                )


                                replyList.add(Data)
                                Log.d("??????",Data.replyId.toString()+" / "+Data.replyParent+" / "+Data.replyMessage)
                            }//for
                            Log.d("??????????????????", replyList.toString())
                            replyList = replyList.sortedBy{it.replyId} as MutableList<replyDataModel>
                            activity?.runOnUiThread{

                                curruntReplyAdapter = replyAdapter(mContext, replyList)
                                curruntReplyAdapter.notifyDataSetChanged()
                                binding.LVReplyFrag.adapter = curruntReplyAdapter
                            }


                    }//if code==200
                }
            })

        }

        binding.btnReplyPagination2.setOnClickListener {
            val timeNow = LocalDateTime.now().toString()
                .replace("-","")
                .replace("T","")
                .replace(":","")+"999"
            serverUtil_okhttp.getAllReplyDataToPagination(mContext,"2", object :serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                    if(RcCode == "200"){
                        val resultAnnounce = jsonObject.getJSONArray("results")


                        var size: Int = resultAnnounce.length()
                        replyList = mutableListOf<replyDataModel>()

                        for(i in 0..size-1){
                            var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                            var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                            var replyID = json_objdetail.getInt("id")
                            var replyParant=json_objdetail.get("parent")
                            Log.d("${replyParant}","?????????_parent")
                            var last_repyId:Float = if("${replyParant}"=="null")replyID.toFloat() else "${replyParant}.${replyID}".toFloat()
                            Log.d("${last_repyId}","?????????_lastId")
                            var reply_createdAt = json_objdetail.getString("created_at")
                                .replace("-","")
                                .replace("T","")
                                .replace(":","")
                                .replace("+0900","")
                            var timeChecker : Double=  timeNow.toDouble() - reply_createdAt.toDouble()

                            Log.d("??????",reply_createdAt)

                            Log.d("??????_t/r",reply_createdAt.toDouble().toString())
                            Log.d("??????_checker",timeChecker.toString())
                            var Data : replyDataModel = replyDataModel(
                                last_repyId,
                                json_objdetail.getString("message"),
                                author_reply,
                                "${replyParant}"
                            )
                            replyList.add(Data)
                            Log.d("??????",Data.replyId.toString()+" / "+Data.replyParent+" / "+Data.replyMessage)
                        }//for
                        Log.d("??????????????????", replyList.toString())
                        replyList = replyList.sortedBy{it.replyId} as MutableList<replyDataModel>
                        activity?.runOnUiThread{

                            curruntReplyAdapter = replyAdapter(mContext, replyList)
                            curruntReplyAdapter.notifyDataSetChanged()
                            binding.LVReplyFrag.adapter = curruntReplyAdapter
                        }


                    }//if code==200
                }
            })

        }



    }//setEvent

    override fun setValues() {
    }

    fun getReplyData(){
      replyList = mutableListOf<replyDataModel>()
        val timeNow = LocalDateTime.now().toString()
            .replace("-","")
            .replace("T","")
            .replace(":","")+"999"

        Log.d("?????? ??????",timeNow.toString())
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

                        Log.d("??????",reply_createdAt)

                        Log.d("??????_t/r",reply_createdAt.toDouble().toString())
                        Log.d("??????_checker",timeChecker.toString())
                        var Data : replyDataModel = replyDataModel(
                            replyID.toFloat(),
                            json_objdetail.getString("message"),
                            author_reply,
                            replyParant
                        )

                        replyList.add(Data)
                    }//for
                    Log.d("??????????????????", replyList.toString())
                    replyList.reverse()// ?????? ?????????
                    activity?.runOnUiThread{
                        curruntReplyAdapter = replyAdapter(mContext, replyList)
                        curruntReplyAdapter.notifyDataSetChanged()
                        binding.LVReplyFrag.adapter = curruntReplyAdapter

                    }
                }
            }
        })
    }

//    fun getAllReplyData(){
////        replyList = mutableListOf<replyDataModel>()
//        val timeNow = LocalDateTime.now().toString()
//            .replace("-","")
//            .replace("T","")
//            .replace(":","")+"999"
//
//        Log.d("?????? ??????",timeNow.toString())
//        serverUtil_okhttp.getAllReplyData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login{
//            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
//                if(RcCode == "200"){
//                    val resultAnnounce = jsonObject.getJSONArray("results")
//                    val countReply = jsonObject.getInt("count")
//                    val countReply_Head = countReply/10
//                    if(countReply==0){
//                        binding.LVReplyFrag.visibility= View.GONE
//                    }
//                    if(countReply!=0){
//                    var size: Int = resultAnnounce.length()
//                    for(i in 0..size-1){
//                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
//                        var author_reply = json_objdetail.getJSONObject("author").getString("nickname")
//                        var replyID = json_objdetail.getInt("id")
//                        var replyParant=json_objdetail.get("parent")
//                        Log.d("${replyParant}","?????????_parent")
//                        var last_repyId:Float = if("${replyParant}"=="null")replyID.toFloat() else "${replyParant}.${replyID}".toFloat()
//                        Log.d("${last_repyId}","?????????_lastId")
//                        var reply_createdAt = json_objdetail.getString("created_at")
//                            .replace("-","")
//                            .replace("T","")
//                            .replace(":","")
//                            .replace("+0900","")
//                        var timeChecker : Double=  timeNow.toDouble() - reply_createdAt.toDouble()
//
//                        Log.d("??????",reply_createdAt)
//
//                        Log.d("??????_t/r",reply_createdAt.toDouble().toString())
//                        Log.d("??????_checker",timeChecker.toString())
//                        var Data : replyDataModel = replyDataModel(
//                            last_repyId,
//                            json_objdetail.getString("message"),
//                            author_reply,
//                            "${replyParant}"
//                        )
//
//                        replyList.add(Data)
//                        Log.d("??????",Data.replyId.toString()+" / "+Data.replyParent+" / "+Data.replyMessage)
//                    }//for
//                    Log.d("??????????????????", replyList.toString())
//                    replyList = replyList.sortedBy{it.replyId} as MutableList<replyDataModel>
//                    activity?.runOnUiThread{
//
//                        if(countReply>10){
//                            binding.LinReplyPaginationBar.visibility = View.VISIBLE
//                            binding.btnReplyPagination1.visibility = View.VISIBLE
//                            binding.btnReplyPagination2.visibility = View.VISIBLE
//                            if (countReply>20){
//                                binding.btnReplyPagination3.visibility = View.VISIBLE
//                                if (countReply>30){
//                                    binding.btnReplyPaginationElse.visibility = View.VISIBLE
//                                }
//                            }
//                       }//if(countReply>10)
//                        curruntReplyAdapter = replyAdapter(mContext, replyList)
//                        curruntReplyAdapter.notifyDataSetChanged()
//                        binding.LVReplyFrag.adapter = curruntReplyAdapter
//                    }
//
//                }//if count!=0
//               }//if code==200
//            }
//        })
//    }
    //****************************************************************************************************************
    fun getAllReplyData2(){
        replyList = mutableListOf<replyDataModel>()
        val timeNow = LocalDateTime.now().toString()
            .replace("-","")
            .replace("T","")
            .replace(":","")+"999"

        Log.d("?????? ??????",timeNow.toString())
        serverUtil_okhttp.getAllReplyData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login2{
            override fun onResponse(jsonObject: JSONArray, RcCode: String) {
                if(RcCode == "200"){
                    var size = jsonObject.length()

                    for(i in 0..size-1){
                        var responseObj= jsonObject[i]
                        if(responseObj is JSONObject){
                            val yp = responseObj.getString("message")
                            Log.d("?????? if", yp)
                            Log.d("?????? if2", size.toString())

                                var author_reply = responseObj.getJSONObject("author").getString("nickname")
                                var replyID = responseObj.getInt("id")
                                var replyParant=responseObj.get("parent")
                                Log.d("${replyParant}","?????????_parent")
                                var last_repyId:Float = if("${replyParant}"=="null")replyID.toFloat() else "${replyParant}.${replyID}".toFloat()
                                Log.d("${last_repyId}","?????????_lastId")
                                var reply_createdAt = responseObj.getString("created_at")
                                    .replace("-","")
                                    .replace("T","")
                                    .replace(":","")
                                    .replace("+0900","")
                                    .replace(" ","")
                                var timeChecker : Double=  timeNow.toDouble() - reply_createdAt.toDouble()

                                Log.d("??????",reply_createdAt)

                                Log.d("??????_t/r",reply_createdAt.toDouble().toString())
                                Log.d("??????_checker",timeChecker.toString())
                                var Data : replyDataModel = replyDataModel(
                                    last_repyId,
                                    responseObj.getString("message"),
                                    author_reply,
                                    "${replyParant}"
                                )
                            Log.d("??????",Data.replyId.toString()+" / "+Data.replyParent+" / "+Data.replyMessage)

                                replyList.add(Data)
                                Log.d("??????",Data.replyId.toString()+" / "+Data.replyParent+" / "+Data.replyMessage)
                        }//if

                    }//for
                            Log.d("??????????????????", replyList.toString())
                            replyList = replyList.sortedBy{it.replyId} as MutableList<replyDataModel>
                            activity?.runOnUiThread{

//                                if(size>10){
//                                    binding.LinReplyPaginationBar.visibility = View.VISIBLE
//                                    binding.btnReplyPagination1.visibility = View.VISIBLE
//                                    binding.btnReplyPagination2.visibility = View.VISIBLE
//                                    if (size>20){
//                                        binding.btnReplyPagination3.visibility = View.VISIBLE
//                                        if (size>30){
//                                            binding.btnReplyPaginationElse.visibility = View.VISIBLE
//                                        }
//                                    }
//                                }//if(countReply>10)
                                curruntReplyAdapter = replyAdapter(mContext, replyList)
                                curruntReplyAdapter.notifyDataSetChanged()
                                binding.LVReplyFrag.adapter = curruntReplyAdapter
                            }
















                }//if code==200
            }
        })
    }
    //****************************************************************************************************************


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



                        Log.d("??????",reply_createdAt)

                        var Data : replyDataModel = replyDataModel(
                            replyID.toFloat(),
                            json_objdetail.getString("message"),
                            author_reply,
                            replyParant
                        )

                        rereplyList.add(Data)
                    }//for
                    Log.d("??????????????????", rereplyList.toString())
                    replyList.reverse()// ?????? ?????????
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