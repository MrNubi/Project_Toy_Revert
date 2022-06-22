package com.beyond.project_toy_revert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.replyDataModel
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class sampleReplyAdapter(val sampleReplyList : MutableList<replyDataModel>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return sampleReplyList.size
    }

    override fun getItem(p0: Int): Any {
        return sampleReplyList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, c1: View?, p2: ViewGroup?): View {
        var view = c1
        if (c1 == null) {
            view =
                LayoutInflater.from(p2?.context).inflate(R.layout.item_sample_reply, p2, false)
        }


        val RName = view?.findViewById<TextView>(R.id.txt_sample_name)
        val RMessage = view?.findViewById<TextView>(R.id.txt_sample_message)
        val RImg =  view?.findViewById<ImageView>(R.id.img_sample_imgview)


        RName!!.text = sampleReplyList[p0].replyName
        RMessage!!.text = sampleReplyList[p0].replyMessage
        if(RImg!=null){
            Glide.with(context).load(R.raw.spinning_golden_rectangle_cat1).into(RImg!!)
        }

        

        return view!!
    }
}