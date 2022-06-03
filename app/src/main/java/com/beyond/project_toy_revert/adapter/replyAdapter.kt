package com.beyond.project_toy_revert.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.board.BoardShowActivity
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.replyDataModel
import com.google.android.material.internal.ContextUtils.getActivity
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import org.json.JSONObject

class replyAdapter(val context : Context, val replyList : MutableList<replyDataModel>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return replyList.size
    }

    override fun getItem(p0: Int): Any {
        return replyList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("RestrictedApi")
    override fun getView(p0: Int, c1: View?, p2: ViewGroup?): View {
        var view = c1
        if (c1 == null) {
            view =
                LayoutInflater.from(p2?.context).inflate(R.layout.item_reply, p2, false)
        }


        val RName = view?.findViewById<TextView>(R.id.txt_replyItem_name)
        val RMessage = view?.findViewById<TextView>(R.id.txt_replyItem_message)
        val RRBtn = view?.findViewById<Button>(R.id.btn_rereplySender)


        val RRLayout = view?.findViewById<LinearLayout>(R.id.reply_reReplyArea)
        val RRListView = view?.findViewById<ListView>(R.id.rereplyListView)

        RName!!.text = replyList[p0].replyName
        RMessage!!.text = replyList[p0].replyMessage



        val rereDataList = mutableListOf<replyDataModel>()
        var rereAdapter = reReplyAdapter(rereDataList)
        RRListView?.adapter = rereAdapter
        serverUtil_okhttp.getReReplyData(TedPermissionProvider.context,replyList[p0].replyId.toString(),object :
            serverUtil_okhttp.JsonResponseHandler_login{
            @SuppressLint("RestrictedApi")
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode == "200"){



                    val resultCount = jsonObject.getInt("count")
                    if (resultCount!=0){
                        getActivity(context)?.runOnUiThread {
                            RRLayout?.visibility=View.VISIBLE
                        }

                        val resultAnnounce = jsonObject.getJSONArray("results")
                        val size: Int = resultAnnounce.length()
                        for(i in 0..size-1){
                            val json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                            val author_reply = json_objdetail.getJSONObject("author").getString("nickname")
                            val replyID = json_objdetail.getInt("id")
                            val replyParant:String? =json_objdetail.getString("parent")?:null
                            val reply_createdAt = json_objdetail.getString("created_at")
                                .replace("-","")
                                .replace("T","")
                                .replace(":","")
                                .replace(".","")
                                .replace("+0900","")



                            Log.d("시간",reply_createdAt)

                            val Data : replyDataModel = replyDataModel(
                                author_reply,
                                json_objdetail.getString("message"),
                                replyID,
                                replyParant
                            )

                            rereDataList.add(Data)
                        }//for
                        Log.d("데이터리스트", rereDataList.toString())
                        rereDataList.reverse()// 정렬 뒤집기

                        rereAdapter = reReplyAdapter(rereDataList)
                        getActivity(context)?.runOnUiThread {
                            rereAdapter.notifyDataSetChanged()
                            RRListView!!.adapter=rereAdapter
                        }



                    }//if count !=0
                    if(resultCount==0){
                        getActivity(context)?.runOnUiThread {
                            RRLayout?.visibility = View.GONE
                        }
                    }
                }//if code == 200
            }
        })
        RRBtn?.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edt, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("대댓글입력")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<ImageView>(R.id.btn_dialog_commentPush)?.setOnClickListener {
            val msg = alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.text.toString()
            val replyId_here = replyList[p0].replyId
            serverUtil_okhttp.postReReply(context,replyId_here.toString(),"메세지자리",object :serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                    if(RcCode == "201"){


                        getActivity(context)?.runOnUiThread {
                            Toast.makeText(context, "대댓글작성완료", Toast.LENGTH_SHORT).show()
                            rereAdapter.notifyDataSetChanged()}

                    }
                    else{
                        Log.d("대댓글 애러",RcCode)
                    }

                }
            })


            alertDialog.dismiss()
        }//btn_dialog_loginYes


        }





        return view!!
    }
}