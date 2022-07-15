package com.beyond.project_toy_revert.myPage.MypageFrag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.FragmentMypageMyPostBinding
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import com.beyond.project_toy_revert.myPage.myPageAdapter.myPageMyReplyAdapter
import org.json.JSONArray
import org.json.JSONObject


class MypageMyScrapFragment : BaseFragment() {
    private lateinit var binding: FragmentMypageMyPostBinding
    lateinit var mpAdapter: myPageMyReplyAdapter
    var mpList = mutableListOf<HitRecyclerDataModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mypage_my_post, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMyFallow("1")
        setupEvents()
        binding.searchViewMyPostMyPostSearch.setOnQueryTextListener(searchViewTextListener())


    }
    fun searchViewTextListener(): androidx.appcompat.widget.SearchView.OnQueryTextListener? =
        object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            //텍스트 입력/수정시에 호출
            override fun onQueryTextChange(s: String): Boolean {
                mpAdapter.getFilter().filter(s)
                Log.d("서치뷰", "SearchVies Text is changed : $s")
                return false
            }
        }
    override fun setupEvents() {

    }

    override fun setValues() {
        binding.btnMyPostPagination1.setOnClickListener {
            getMyFallow("1")
        }
        binding.btnMyPostPagination2.setOnClickListener {
            getMyFallow("2")
        }

        binding.btnMyPostPagination3.setOnClickListener {
            getMyFallow("3")
        }

        binding.btnMyPostPagination4.setOnClickListener {
            getMyFallow("4")
        }

        binding.btnMyPostPaginationElse.setOnClickListener {
            getMyFallow("5")
        }


    }
    fun getMyFallow(page:String){
        mpList = mutableListOf<HitRecyclerDataModel>()
        serverUtil_okhttp.getMyData(mContext,page,"/following?page=","",object : serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {

                if(RcCode=="200"){
                    Log.d("getMyReplyData_코드","200")
                    val myReplyData = jsonObject.getJSONArray("results")
                    val count = jsonObject.getInt("count")



                    val myReplyDataSize = myReplyData.length()
                    for(i in 0..myReplyDataSize-1){
                        var RPdata = myReplyData[i]
                        if(RPdata is JSONObject){
                            val rpMessage = RPdata.getString("nickname")
                            val rpCreated_at =RPdata.getString("about_me")
                            val rpId =RPdata.getInt("pk")
//                            val imgCheck = RPdata.get("images")
//                            if(imgCheck is JSONArray){
//                                val imgUrl = imgCheck.getJSONObject(0).getString("image")
//                                Log.d("getMyPost_이미지있음", imgUrl)
//                            }


                            var rpData : HitRecyclerDataModel = HitRecyclerDataModel(
//                                val hitTitle : String,
//                            val hitLike : Int,
//                            val hitimgurl : String,
//                            val hitID : Int,

//                                holder.mpCreatedAt.text = myPostList.get(position).hitTitle
//                                        holder.mpMessage.text = myPostList.get(position).hitimgurl
//                                        holder.mpId.text = myPostList.get(position).hitID.toString()
                                rpCreated_at,
                                0,
                                rpMessage,
                                rpId
                            )
                            mpList.add(rpData)
                        }//iff(RPdata is JSONObject)
                        else{
                            Log.d("오류_getMyReply", "RPdata가 JSONObject가 아님")
                        }
                    }//for
                    activity?.runOnUiThread{
                        if(count>10){
                            binding.btnMyPostPagination1.visibility = View.VISIBLE
                            binding.btnMyPostPagination2.visibility = View.VISIBLE
                            if(count>20){
                                binding.btnMyPostPagination3.visibility = View.VISIBLE
                                if(count>30){
                                    binding.btnMyPostPagination4.visibility = View.VISIBLE
                                    if(count>50){
                                        binding.btnMyPostPaginationElse.visibility = View.VISIBLE
                                    }}}}//if(count>10)
                        mpAdapter = myPageMyReplyAdapter(mpList)
                        binding.rvMyPostMyPostShowRv.adapter = mpAdapter
                        binding.rvMyPostMyPostShowRv.layoutManager = LinearLayoutManager(mContext)
                        mpAdapter.notifyDataSetChanged()
                    }

                }//if(RcCode=="200")
                else{
                    activity?.runOnUiThread{
                        Log.wtf("응답불량:getMyReply","응답코드: ${RcCode}")
                    }}//else
            }//override fun onResponse

        })//serverUtil_okhttp.getMyReplyData
    }//fun getMyReply


}