package com.beyond.project_toy_revert.myPage.MypageFrag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.FragmentMypageMyReplyBinding
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import com.beyond.project_toy_revert.myPage.myPageAdapter.myPageMyReplyAdapter
import org.json.JSONObject

class MypageMyReplyFragment : BaseFragment() {
    private lateinit var binding: FragmentMypageMyReplyBinding
    lateinit var mrAdapter:myPageMyReplyAdapter
    var mrList = mutableListOf<HitRecyclerDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mypage_my_reply, container, false )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getMyReply("1")
        setupEvents()
        setValues()

    }

    override fun setupEvents() {
        binding.btnMyReplyPagination1.setOnClickListener {
            getMyReply("1")
        }
        binding.btnMyReplyPagination2.setOnClickListener {
            getMyReply("2")
        }
        binding.btnMyReplyPagination3.setOnClickListener {
            getMyReply("3")
        }
        binding.btnMyReplyPagination4.setOnClickListener {
            getMyReply("4")
        }
        binding.btnMyReplyPaginationElse.setOnClickListener {
            getMyReply("5")
        }

    }

    override fun setValues() {

    }

    fun getMyReply(page:String){
        mrList = mutableListOf<HitRecyclerDataModel>()
        serverUtil_okhttp.getMyData(mContext,page,"/comment?page=","",object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {

            if(RcCode=="200"){
                Log.d("getMyReplyData_코드","200")
                val myReplyData = jsonObject.getJSONArray("results")
                val count = jsonObject.getInt("count")
                activity?.runOnUiThread{
                    if (count > 10) {
                        binding.btnMyReplyPagination1.visibility = View.VISIBLE
                        binding.btnMyReplyPagination2.visibility = View.VISIBLE
                        if (count > 20) {
                            binding.btnMyReplyPagination3.visibility = View.VISIBLE
                            if (count > 30) {
                                binding.btnMyReplyPagination4.visibility = View.VISIBLE
                                if (count > 50) {
                                    binding.btnMyReplyPaginationElse.visibility = View.VISIBLE
                                }
                            }
                        }
                    }//if(count>10)
                     }


                val myReplyDataSize = myReplyData.length()
                    for(i in 0..myReplyDataSize-1){
                        var RPdata = myReplyData[i]
                        if(RPdata is JSONObject){
                            val rpMessage = RPdata.getString("message")
                            val rpCreated_at =RPdata.getString("created_at")
                            val rpId =RPdata.getInt("id")

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
                          mrList.add(rpData)
                        }//iff(RPdata is JSONObject)
                        else{
                            Log.d("오류_getMyReply", "RPdata가 JSONObject가 아님")
                        }
                    }//for
                activity?.runOnUiThread{
                    mrAdapter = myPageMyReplyAdapter(mrList)
                    binding.rvMyReplyMyReplyShowRv.adapter = mrAdapter
                    binding.rvMyReplyMyReplyShowRv.layoutManager = LinearLayoutManager(mContext)
                    mrAdapter.notifyDataSetChanged()
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