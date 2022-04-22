package com.beyond.project_toy_revert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.datas.AnnounceDataModel

 class AnnounceListAdapter(val boardList : MutableList<AnnounceDataModel>) :
     BaseAdapter()
//     Filterable
 {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(p0: Int): Any {
        return boardList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, c1: View?, p2: ViewGroup?): View {

        var view = c1
        if (c1 == null) {
            view =
                LayoutInflater.from(p2?.context).inflate(R.layout.item_announce_lv, p2, false)
        }


        val title = view?.findViewById<TextView>(R.id.txt_annouceItem_titile)
        val nick = view?.findViewById<TextView>(R.id.txt_item_nick)

        title!!.text = boardList[p0].title
        nick!!.text = boardList[p0].nickname

        return view!!
    }

//     override fun getFilter(): Filter {
//
//     }

 }