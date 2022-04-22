package com.beyond.project_toy_revert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class HitRecyclerAdapter(val HitList : MutableList<HitRecyclerDataModel>) : RecyclerView.Adapter<HitRecyclerAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HitRecyclerAdapter.CustomViewHolder {
        val view1= LayoutInflater.from(parent.context).inflate(R.layout.item_hit_recycler, parent, false )
        return CustomViewHolder(view1)
    }

    override fun onBindViewHolder(holder: HitRecyclerAdapter.CustomViewHolder, position: Int) {
        holder.HITimg.setImageResource(R.drawable.cloud)
        holder.HITtitle.text = HitList.get(position).hitTitle
        holder.HITlike.text = HitList.get(position).hitLike.toString()
        Glide.with(context).load(HitList.get(position).hitimgurl).into(holder.HITimg)


    }

    override fun getItemCount(): Int {
        // 리스트 갯수
        return HitList.size
    }
    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val HITimg = itemView.findViewById<ImageView>(R.id.img_item_hitRecycler) // 이미지
        val HITtitle = itemView.findViewById<TextView>(R.id.txt_title_item_hitRecycler)
        val HITlike = itemView.findViewById<TextView>(R.id.txt_like_item_hitRecycler)

    }
}