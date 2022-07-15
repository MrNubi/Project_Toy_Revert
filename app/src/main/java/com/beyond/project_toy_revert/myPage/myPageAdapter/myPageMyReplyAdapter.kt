package com.beyond.project_toy_revert.myPage.myPageAdapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.board.BoardShowActivity
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.util.Context_okhttp
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

class myPageMyReplyAdapter(val myPostList : MutableList<HitRecyclerDataModel>) :
    RecyclerView.Adapter<myPageMyReplyAdapter.CustomViewHolder>(),Filterable {

    var filteredmPList= mutableListOf<HitRecyclerDataModel>()
    var itemFilter = ItemFilter()

    init {
        filteredmPList.addAll(myPostList)
    }
    inner class ItemFilter:Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            Log.d("myPageMyReplyAdapter", "charSequence : $charSequence")

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList: MutableList<HitRecyclerDataModel> = mutableListOf<HitRecyclerDataModel>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = myPostList
                results.count = myPostList.size

                return results
                //공백제외 2글자 이하인 경우 -> 이름으로만 검색
            }
            else if (filterString.trim { it <= ' ' }.length <= 2) {
                for (mPl in myPostList) {
//                    if (mPl.hitTitle.contains(filterString)) {
//                        filteredList.add(mPl)
//                    }
                    if (mPl.hitTitle.contains(filterString) || mPl.hitimgurl.contains(filterString)) {
                        filteredList.add(mPl)
                    }
                }
                //그 외의 경우(공백제외 2글자 초과) -> 이름/전화번호로 검색
            }
            else {
                for (person in myPostList) {
                    if (person.hitTitle.contains(filterString) || person.hitimgurl.contains(filterString)) {
                        filteredList.add(person)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredmPList.clear()
            filteredmPList.addAll(p1?.values as MutableList<HitRecyclerDataModel>)
            notifyDataSetChanged()
        }
    }

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


        holder.mpCreatedAt.text = filteredmPList.get(position).hitTitle
        holder.mpMessage.text = filteredmPList.get(position).hitimgurl
        holder.mpId.text = filteredmPList.get(position).hitID.toString()



    }

    override fun getItemCount(): Int {
        // 리스트 갯수
        return filteredmPList.size
    }
    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val mpImg = itemView.findViewById<ImageView>(R.id.img_myPage_item_leaf) // 이미지
        val mpCreatedAt = itemView.findViewById<TextView>(R.id.txt_myPage_item_createdAt)
        val mpId = itemView.findViewById<TextView>(R.id.txt_myPage_item_postId)
        val mpMessage = itemView.findViewById<TextView>(R.id.txt_myPage_item_message)

    }

    override fun getFilter(): Filter {
        return itemFilter
    }


}


