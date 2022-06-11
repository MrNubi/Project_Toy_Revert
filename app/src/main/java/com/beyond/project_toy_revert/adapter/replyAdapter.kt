package com.beyond.project_toy_revert.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp

import com.beyond.project_toy_revert.datas.replyDataModel
import com.google.android.material.internal.ContextUtils.getActivity
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
        val RRback= view?.findViewById<LinearLayout>(R.id.reply_background)




        RName!!.text = replyList[p0].replyName
        RMessage!!.text = replyList[p0].replyMessage
        if(replyList[p0].replyParent!="null"){
        RRback!!.setBackgroundColor(Color.YELLOW)
        }










        return view!!
    }
}