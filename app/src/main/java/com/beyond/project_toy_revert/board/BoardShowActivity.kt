package com.beyond.project_toy_revert.board

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.MainFragAdapter
import com.beyond.project_toy_revert.adapter.MultifleImgRcAdapter
import com.beyond.project_toy_revert.adapter.replyFragViewpagerAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.ActivityBoardShowBinding
import com.beyond.project_toy_revert.datas.MultifleImgDataModel
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class BoardShowActivity : BasicActivity() {
    private lateinit var binding: ActivityBoardShowBinding
    private var is_like = false
    private var is_played = false

    private  var MImglist = mutableListOf<MultifleImgDataModel>()
    private lateinit var MImgAdapter : MultifleImgRcAdapter
    private lateinit var player: SimpleExoPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_board_show)
//        val likeState = intent.getStringExtra("AnnounceLike").toBoolean()
        binding.getImageArea.visibility = View.GONE
        binding.rcBshowMulti.visibility = View.GONE
        binding.exoPlaerBshowPlayerview.visibility = View.GONE
        setPage()
//        setText()
        editDialogBtn()

    }
    fun setPage() {
        serverUtil_okhttp.getPostDataID(mContext,
            object : serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode: String) {

                Log.d("????????????", RcCode)
                    if(RcCode == "200"){
                        val imgORtxt = jsonObject.get("images")
                        val vidioNullCheck = jsonObject.get("video")
                        val vidioString :String = if(vidioNullCheck != null) jsonObject.getString("video") else ""
                        val resultPostId =if(imgORtxt is JSONArray) jsonObject.getJSONArray("images") else ""
                        val resultLength = if(resultPostId is JSONArray)resultPostId.length() else 0
                        val bShowTitle = jsonObject.getString("title")
                        val bShowContent = jsonObject.getString("content")
                        val bShowAuthor = jsonObject.getString("author")
                        val isLike = jsonObject.getBoolean("is_like")
                        val likeCount = jsonObject.getInt("like_count")
                        runOnUiThread{LikeSetter(isLike)
                        binding.txtBshowTitle.text= bShowTitle
                        binding.txtBshowContent.text= bShowContent
                        binding.txtBshowTime.text= bShowAuthor
                        binding.txtBsowLikeCount.text= likeCount.toString()}
                        if (vidioString!=""&&vidioString!="null"){

                            runOnUiThread{
                                is_played = true
                                player = SimpleExoPlayer.Builder(this@BoardShowActivity).build()
                                binding.exoPlaerBshowPlayerview.player =  player
                                binding.exoPlaerBshowPlayerview.isVisible = true
                                Log.d("????????? ?????????", "11${vidioString}")
                                val dataSourceFactory = DefaultDataSourceFactory(this@BoardShowActivity)
                                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                                    .createMediaSource(MediaItem.fromUri(Uri.parse(vidioString)))
                                player.setMediaSource(mediaSource)
                                player.prepare()
                                binding.txtBshowTitle.setOnClickListener {
                                    player.play()
                                }




                            }
                        }
                        if (resultLength != 0){
                            val resultPostIdDetail = if(resultPostId is JSONArray)resultPostId.getJSONObject(0) else ""
                            val imgUrl = if(resultPostIdDetail is JSONObject)resultPostIdDetail.getString("image") else R.drawable.cloud


                            Log.d("????????????", imgUrl.toString())

                            if(resultLength ==1){
                           runOnUiThread{
                               binding.getImageArea.isVisible = true
                               Glide.with(mContext).load(imgUrl).into(binding.getImageArea)

                           }//runOnUiThread
                        }//if(resultLength ==1)
                            if (resultLength !=1){
                                //0??? ???????????? 1??? ????????? -> ??????
                                    for(i in 0 .. resultLength-1){
                                        val resultPostIdDetail = if(resultPostId is JSONArray)resultPostId.getJSONObject(i) else ""

                                        val imgUrl = if(resultPostIdDetail is JSONObject)resultPostIdDetail.getString("image") else R.drawable.cloud.toString()
                                        var Data: MultifleImgDataModel = MultifleImgDataModel(
                                            imgUrl
                                        )


                                        MImglist.add(Data)
                                    }
                                runOnUiThread{
                                    MImgAdapter = MultifleImgRcAdapter(MImglist)
                                    binding.rcBshowMulti.adapter = MImgAdapter
                                    binding.rcBshowMulti.layoutManager =
                                        LinearLayoutManager(mContext).also {
                                            it.orientation = LinearLayoutManager.HORIZONTAL
                                        }
                                    binding.rcBshowMulti.isVisible = true
                                }
                            }

                    }
                    else{

                        }
                        //???????????????




                    }
                      else{
                          val detail = jsonObject.getString("detail")
                        Toast.makeText(mContext, "${detail}", Toast.LENGTH_SHORT).show()
                      }

                }
            })
    }


    fun LikeSetter(likeOk : Boolean){
        val IL = likeOk
        is_like = IL
        Log.d("??????_isLike", is_like.toString() )
        Log.d("??????_IL", IL.toString() )
        //?????? ?????? ????????? ?????? ?????? ??? ?????? ??????
        if(is_like){ //???????????? ?????? ??????????????? ??????
            //?????????????????? ?????????
            //0f??? 0?????????, 1F??? 100?????????
            //ofFloat(????????????, ????????????).setDuration(????????????)
            // Custom animation speed or duration.
            val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(340)
            animator.addUpdateListener {
                binding.lottieBshowHeart.progress = it.animatedValue as Float
            }
            animator.start()

            Log.d("?????????Setting", "?????? ?????????")
        }else{ //???????????? ?????? ???????????? ?????? ??????
            val animator = ValueAnimator.ofFloat(1f, 0f).setDuration(170)
            animator.addUpdateListener {
                binding.lottieBshowHeart.progress = it.animatedValue as Float
            }
            animator.start()

            Log.d("?????????Setting", "?????? ????????????")
        }


        binding.btnBshowLike.setOnClickListener {


            if(!is_like){ //????????? false????????? false??? ????????? ????????????.
                //?????????????????? ?????????
                //0f??? 0?????????, 1F??? 100?????????
                //ofFloat(????????????, ????????????).setDuration(????????????)
                // Custom animation speed or duration.
                serverUtil_okhttp.PostLike(mContext, object : serverUtil_okhttp.JsonResponseHandler_Like{
                    override fun onResponse(jsonObject: JSONObject, RcCode: String, Rcname:String) {
                        Log.d("??????_RcCode", RcCode.toString())
                        Log.d("??????_jsonObject", jsonObject.toString())
                        Log.d("??????_Rcname", Rcname.toString())
                        if(RcCode == "201"){
                            val likeCount = jsonObject.getInt("like_count")
                            Log.d("??????", likeCount.toString())


                            runOnUiThread {
                                binding.txtBsowLikeCount.text = likeCount.toString()
                                val animator = ValueAnimator.ofFloat(0f, 0.17f).setDuration(340)
                                animator.addUpdateListener {
                                    binding.lottieBshowHeart.progress = it.animatedValue as Float
                                }
                                animator.start()
                                is_like = true // ????????? ????????? ?????????.
                                Log.d("?????????", "Bhow / ????????? ????????? ?????????")
                            }


                        }
                        else if(RcCode == "200"){
                            val likeCount = jsonObject.getInt("like_count")
                           runOnUiThread{
                               binding.txtBsowLikeCount.text = likeCount.toString()

                               Toast.makeText(mContext, "?????? ???????????? ???????????????!", Toast.LENGTH_SHORT).show()
                           }
                        }
                        else{
                            val detail = jsonObject.getString("detail")
                            runOnUiThread{ Toast.makeText(mContext, "$detail", Toast.LENGTH_SHORT).show()
                            Log.d("??????_??????", detail)}
                        }


                    }
                })
               }

            else{ //??????????????? ????????????.
                val myID = Context_okhttp.getID(mContext)
                serverUtil_okhttp.PostUnlike(mContext, object :serverUtil_okhttp.JsonResponseHandler_login{
                    override fun onResponse(
                        jsonObject: JSONObject,
                        RcCode: String,
                    ) {
                        if(RcCode=="200"){
                            Log.d("??????_else_RcCode", RcCode.toString())
                            Log.d("??????_else_jsonObject", jsonObject.toString())
                            val likeCount = jsonObject.getInt("like_count")
                            runOnUiThread {
                                binding.txtBsowLikeCount.text = likeCount.toString()
                                val animator = ValueAnimator.ofFloat(0.17f, 0f).setDuration(170)
                                animator.addUpdateListener {
                                    binding.lottieBshowHeart.progress = it.animatedValue as Float
                                }
                                animator.start()
                                is_like = false // ?????? false??? ??????.
                                Log.d("?????????", "Bhow / ????????? ????????? ??????")
                            }//runOnUiThread
                        }
                    }})//onResponse
            }//else
        }
    }

    fun editDialogBtn(){
        // ?????? ????????? ??????
        val id = Context_okhttp.getID(mContext)
        val AnnounceAuthor = intent.getStringExtra("AnnounceAuthor").toString()
        Log.d("btnShow_id", id)
        Log.d("btnShow_AnnounceAuthor", AnnounceAuthor)
        if(id != AnnounceAuthor){
            binding.imgBshowEditOrDel.visibility = GONE
        }
        // ?????? ?????? ?????????
        binding.imgBshowEditOrDel.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_or_del, null)
            val mBuilder = AlertDialog.Builder(mContext)
                .setView(mDialogView)
                .setTitle("?????? / ??????")
            val alertDialog = mBuilder.show()
            alertDialog.findViewById<Button>(R.id.btn_dialog_edit)?.setOnClickListener {
                startActivity(Intent(mContext, BoardEditActivity::class.java))
                finish()
            }
            alertDialog.findViewById<Button>(R.id.btn_dialog_del)?.setOnClickListener{
                serverUtil_okhttp.deleteAnnounce(mContext, object : serverUtil_okhttp.JsonResponseHandler_del{
                    override fun onResponse(RcCode: String) {
                        val Rc_Int = RcCode.toInt()
                        if(Rc_Int==204){
                            runOnUiThread{
                                Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show()
                                val bwIntent = Intent(mContext, MainActivity::class.java)
                                startActivity(bwIntent)
                            }
                        }
                        else{
//                            val detail = jsonObject.getString("detail")
                            runOnUiThread{
//                                Toast.makeText(mContext, "${detail}", Toast.LENGTH_SHORT).show()
                                Toast.makeText(mContext, "??????: ${RcCode}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }
    }
    override fun onBackPressed() {
        val bwIntent = Intent(mContext, MainActivity::class.java)
        startActivity(bwIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(is_played == true){
        player.release()
            is_played == false

        }
        Log.d("???????????????","onDestroy")
    }

    override fun onStop() {
        super.onStop()
        if(is_played == true){
        player.pause()
            is_played == false
        }
        Log.d("?????????","onStop")
    }

}
//*********************************************************************************************
// ??? ?????? ?????????
//    fun setText():String{
//        val AnnounceContent = intent.getStringExtra("AnnounceContent").toString()
//        val AnnounceTitle = intent.getStringExtra("AnnounceTitle").toString()
//        val AnnounceNickname = intent.getStringExtra("AnnounceNickname").toString()
//        val AnnounceAuthor = intent.getStringExtra("AnnounceAuthor").toString()
//        val AnnounceId = intent.getStringExtra("AnnounceId").toString()
//        Log.d("??????", AnnounceContent )
//        Log.d("??????", AnnounceTitle )
//        Log.d("??????", AnnounceNickname )
//        Log.d("??????", AnnounceAuthor )
//        Log.d("??????", AnnounceId )
//
//        binding.txtBshowTitle.text=AnnounceTitle
//        binding.txtBshowContent.text=AnnounceContent
//        binding.txtBshowTime.text=AnnounceNickname
//
//        return AnnounceAuthor
//    }//setText()