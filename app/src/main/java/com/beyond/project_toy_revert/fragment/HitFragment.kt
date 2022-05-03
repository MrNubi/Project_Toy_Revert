package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.AnnounceListAdapter
import com.beyond.project_toy_revert.adapter.HitRecyclerAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.FragmentHitBinding
import com.beyond.project_toy_revert.datas.AnnounceDataModel
import com.beyond.project_toy_revert.datas.HitRecyclerDataModel
import com.beyond.project_toy_revert.inheritance.BaseFragment
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject


class HitFragment : BaseFragment() {
private lateinit var HitAdapter : HitRecyclerAdapter
private  var HITlist = mutableListOf<HitRecyclerDataModel>()
private lateinit var binding: FragmentHitBinding
private  var best3list = mutableListOf<HitRecyclerDataModel>()
private  var bestNumlist = mutableListOf<HitRecyclerDataModel>()
private lateinit var best3Adapter : HitRecyclerAdapter





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hit, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHitPost()
        setBest3Post()
        setValues()
        setupEvents()

    }

    override fun setupEvents() {

    }

    override fun setValues() {
    }

    fun setHitPost(){
        serverUtil_okhttp.getPostData(mContext, object : serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                Log.d("히얼", RcCode)
                if(RcCode == "[\"count\",\"next\",\"previous\",\"results\"]"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    var i : Int = 0
                    var size: Int = resultAnnounce.length()


                        for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                            val resultPostType = json_objdetail.get("images")
                            if(resultPostType is JSONArray) {
                                val resultPostId = json_objdetail.getJSONArray("images")
                                var responseName = resultPostId.getJSONObject(0).names()
                                Log.d("nami", responseName.toString())
                                var imgUrl =
                                    if (resultPostId.length() != 0) resultPostId.getJSONObject(0)
                                        .getString("image") else ""
                                var Data: HitRecyclerDataModel = HitRecyclerDataModel(


                                    json_objdetail.getString("title"),
                                    json_objdetail.getInt("like_count"),
                                    imgUrl,
                                    json_objdetail.getInt("id"),


                                    )

                                HITlist.add(Data)
                            }//if(jsonarray)
                    }//for

                        HITlist.sortBy {it.hitLike}
                        HITlist.reverse()
//                    AnnounceList.reverse()// 정렬 뒤집기
                    activity?.runOnUiThread{
                        HitAdapter = HitRecyclerAdapter(HITlist)
                        binding.recyclerLikeHitRecycler.adapter = HitAdapter
                        binding.recyclerLikeHitRecycler.layoutManager = LinearLayoutManager(mContext).also{it.orientation = LinearLayoutManager.HORIZONTAL}

                    }
                }
            }

        })//serverUtil_okhttp.getPostData
    }

    fun setBest3Post(){
        serverUtil_okhttp.getPostData(mContext, object : serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                Log.d("히얼", RcCode)
                if(RcCode == "[\"count\",\"next\",\"previous\",\"results\"]"){
                    val resultAnnounce = jsonObject.getJSONArray("results")
                    var i : Int = 0
                    var size: Int = resultAnnounce.length()


                    for(i in 0..size-1){
                        var json_objdetail: JSONObject = resultAnnounce.getJSONObject(i)
                        val resultPostId = json_objdetail.get("images")
                        if(resultPostId is JsonArray){
                            val resultLikeArray = json_objdetail.getJSONArray("images")
                            var imgUrl = if(resultLikeArray.length() != 0)resultLikeArray.getJSONObject(0).getString("image")else ""



                        var Data : HitRecyclerDataModel = HitRecyclerDataModel(


                            json_objdetail.getString("title"),
                            json_objdetail.getInt("like_count"),
                            imgUrl ,
                            json_objdetail.getInt("id"),



                            )

                        bestNumlist.add(Data)
                        }   //if(jsonARRAY)
                        else{
                            var Data : HitRecyclerDataModel = HitRecyclerDataModel(


                                json_objdetail.getString("title"),
                                json_objdetail.getInt("like_count"),
                                "",
                                json_objdetail.getInt("id"),



                                )
                            bestNumlist.add(Data)
                        }//else
                    }//for
                    bestNumlist.sortBy {it.hitLike}

                    bestNumlist.reverse()
                    for(i in 0..2){
                        var DataBest : HitRecyclerDataModel = HitRecyclerDataModel(
                            bestNumlist[i].hitTitle,
                            bestNumlist[i].hitLike,
                            bestNumlist[i].hitimgurl,
                            bestNumlist[i].hitID

                        )
                        best3list.add(DataBest)
                    }
//                    AnnounceList.reverse()// 정렬 뒤집기
                    activity?.runOnUiThread{
                        best3Adapter = HitRecyclerAdapter(best3list)
                        binding.recyclerBest3HitRecycler.adapter = best3Adapter
                        binding.recyclerBest3HitRecycler.layoutManager = LinearLayoutManager(mContext).also{it.orientation = LinearLayoutManager.HORIZONTAL}

                    }
                }
            }

        })//serverUtil_okhttp.getPostData
    }


}