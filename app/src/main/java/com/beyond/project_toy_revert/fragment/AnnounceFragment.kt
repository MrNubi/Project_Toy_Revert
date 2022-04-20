package com.beyond.project_toy_revert.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.AnnounceListAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.board.BoardShowActivity
import com.beyond.project_toy_revert.board.BoardWriteActivity
import com.beyond.project_toy_revert.databinding.FragmentAnnounceBinding
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import com.beyond.project_toy_revert.util.Context_okhttp
import org.json.JSONObject


class AnnounceFragment : BaseFragment() {

    lateinit var binding : FragmentAnnounceBinding

    private var AnnounceList = mutableListOf<AnnounceDataModel>()
    private lateinit var AFAdapter : AnnounceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_announce, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getListDataAndAdapterSetting()
        setValues()
        setupEvents()

    }//onActivityCreated
    override fun setValues() {


    }//setValues


    override fun setupEvents() {
        binding.LVFragAnnounce.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("몰루", AnnounceList[i].content)
            activity?.let{
                val intent = Intent(context, BoardShowActivity::class.java)
                intent.putExtra("AnnounceContent", AnnounceList[i].content)
                intent.putExtra("AnnounceTitle", AnnounceList[i].title)
                intent.putExtra("AnnounceNickname", AnnounceList[i].nickname)
                intent.putExtra("AnnounceAuthor", AnnounceList[i].author)
                intent.putExtra("AnnounceId", AnnounceList[i].id.toString())
                Context_okhttp.setPostId(mContext, AnnounceList[i].id.toString())
                Log.d("체킹", Context_okhttp.getPostId(mContext))

                startActivity(intent)
                activity?.finish()

            }


        }
        binding.btnAnnounceWrite.setOnClickListener {
            activity?.let{
                val intent = Intent(context, BoardWriteActivity::class.java)

                startActivity(intent)
                activity?.finish()

            }}
            binding.txtRefresh.setOnClickListener {
                getListDataAndAdapterSetting()
            }


    }//setupEvents

    fun getListDataAndAdapterSetting(){
        serverUtil_okhttp.getPostData(object : serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                Log.d("히얼", RcCode)
                if(RcCode == "[\"count\",\"next\",\"previous\",\"results\"]"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    var i : Int = 0
                    var size: Int = resultAnnounce.length()
                    for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                        var Data : AnnounceDataModel = AnnounceDataModel(
                            json_objdetail.getInt("id"),
                            json_objdetail.getString("author"),
                            json_objdetail.getString("nickname"),
                            json_objdetail.getString("title"),
                            json_objdetail.getString("content"),
                        )
                        AnnounceList.add(Data)
                    }//for
//                    AnnounceList.reverse()// 정렬 뒤집기
                    activity?.runOnUiThread{
                        AFAdapter = AnnounceListAdapter(AnnounceList)
                        binding.LVFragAnnounce.adapter = AFAdapter

                    }
                }
            }

        })//serverUtil_okhttp.getPostData
    }//fun getData()
}
