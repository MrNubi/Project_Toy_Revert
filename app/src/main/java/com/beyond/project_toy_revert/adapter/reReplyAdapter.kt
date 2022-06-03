package com.beyond.project_toy_revert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.replyDataModel

class reReplyAdapter(val replyList : MutableList<replyDataModel>) :
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

    override fun getView(p0: Int, c1: View?, p2: ViewGroup?): View {
        var view = c1
        if (c1 == null) {
            view =
                LayoutInflater.from(p2?.context).inflate(R.layout.item_reply, p2, false)
        }


        val RName = view?.findViewById<TextView>(R.id.txt_replyItem_name)
        val RMessage = view?.findViewById<TextView>(R.id.txt_replyItem_message)

        RName!!.text = replyList[p0].replyName
        RMessage!!.text = replyList[p0].replyMessage

        

        return view!!
    }
}