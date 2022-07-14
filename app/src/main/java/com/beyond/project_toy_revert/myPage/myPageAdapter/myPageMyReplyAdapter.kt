package com.beyond.project_toy_revert.myPage.myPageAdapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.board.BoardShowActivity
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.util.Context_okhttp
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class myPageMyReplyAdapter(val myPostList : MutableList<HitRecyclerDataModel>) : RecyclerView.Adapter<myPageMyReplyAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): myPageMyReplyAdapter.CustomViewHolder {
        val view1= LayoutInflater.from(parent.context).inflate(R.layout.item_my_post_rv, parent, false )
        return CustomViewHolder(view1).apply {
//            itemView.setOnClickListener {
//                val curPos : Int = adapterPosition
//                val HITDATA: HitRecyclerDataModel = myPostList.get(curPos)
//                Context_okhttp.setPostId(TedPermissionProvider.context, HITDATA.hitID.toString())
//                val k = Context_okhttp.getPostId(TedPermissionProvider.context)
//                Log.d("히트", k)
//                val HITintent = Intent(context, BoardShowActivity::class.java)
//                HITintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//
//
//                TedPermissionProvider.context.startActivity(HITintent)
//            }

        }
    }

    override fun onBindViewHolder(holder: myPageMyReplyAdapter.CustomViewHolder, position: Int) {


        holder.mpCreatedAt.text = myPostList.get(position).hitTitle
        holder.mpMessage.text = myPostList.get(position).hitimgurl
        holder.mpId.text = myPostList.get(position).hitID.toString()



    }

    override fun getItemCount(): Int {
        // 리스트 갯수
        return myPostList.size
    }
    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val mpImg = itemView.findViewById<ImageView>(R.id.img_myPage_item_leaf) // 이미지
        val mpCreatedAt = itemView.findViewById<TextView>(R.id.txt_myPage_item_createdAt)
        val mpId = itemView.findViewById<TextView>(R.id.txt_myPage_item_postId)
        val mpMessage = itemView.findViewById<TextView>(R.id.txt_myPage_item_message)

    }



}