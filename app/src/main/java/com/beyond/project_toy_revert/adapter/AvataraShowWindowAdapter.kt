package com.beyond.project_toy_revert.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.board.BoardShowActivity
import com.beyond.project_toy_revert.datas.AvataraModel
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class AvataraShowWindowAdapter (val AvataList : MutableList<AvataraModel>) : RecyclerView.Adapter<AvataraShowWindowAdapter.CustomViewHolder>(){


    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    class CustomViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        val Avataimg = itemView.findViewById<ImageView>(R.id.img_item_avataraShow) // 이미지
        val AvataBackground = itemView.findViewById<LinearLayout>(R.id.Lin_avataraShow_background)//배경레이아웃
        val AvataName = itemView.findViewById<TextView>(R.id.txt_name_item_avataraShow)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvataraShowWindowAdapter.CustomViewHolder {
        val view1= LayoutInflater.from(parent.context).inflate(R.layout.item_rv_avatara_show_window, parent, false )
        return AvataraShowWindowAdapter.CustomViewHolder(view1)
    }

    override fun onBindViewHolder(
        holder: AvataraShowWindowAdapter.CustomViewHolder,
        position: Int
    ) {
        holder.AvataName.text = AvataList.get(position).avataraName
        Glide.with(context).load(AvataList.get(position).avataraPicture).into(holder.Avataimg)
        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return AvataList.size
    }
}