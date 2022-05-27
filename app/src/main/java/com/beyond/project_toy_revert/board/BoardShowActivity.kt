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

                Log.d("이미지즘", RcCode)
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
                                Log.d("비디오 비저블", "11${vidioString}")
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


                            Log.d("이미지즘", imgUrl.toString())

                            if(resultLength ==1){
                           runOnUiThread{
                               binding.getImageArea.isVisible = true
                               Glide.with(mContext).load(imgUrl).into(binding.getImageArea)

                           }//runOnUiThread
                        }//if(resultLength ==1)
                            if (resultLength !=1){
                                //0도 아니면서 1도 아니다 -> 다수
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
                        //이미지넣기




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
        Log.d("여기_isLike", is_like.toString() )
        Log.d("여기_IL", IL.toString() )
        //먼저 있던 좋아요 상태 탐색 후 반영 코드
        if(is_like){ //좋아요가 먼저 찍혀있었을 경우
            //애니메이션의 커스텀
            //0f가 0퍼센트, 1F가 100퍼센트
            //ofFloat(시작지점, 종료지점).setDuration(지속시간)
            // Custom animation speed or duration.
            val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(340)
            animator.addUpdateListener {
                binding.lottieBshowHeart.progress = it.animatedValue as Float
            }
            animator.start()

            Log.d("좋아요Setting", "하트 채워짐")
        }else{ //좋아요가 먼저 찍혀있지 않을 경우
            val animator = ValueAnimator.ofFloat(1f, 0f).setDuration(170)
            animator.addUpdateListener {
                binding.lottieBshowHeart.progress = it.animatedValue as Float
            }
            animator.start()

            Log.d("좋아요Setting", "하트 안채워짐")
        }


        binding.btnBshowLike.setOnClickListener {


            if(!is_like){ //기본이 false이므로 false가 아닐때 실행한다.
                //애니메이션의 커스텀
                //0f가 0퍼센트, 1F가 100퍼센트
                //ofFloat(시작지점, 종료지점).setDuration(지속시간)
                // Custom animation speed or duration.
                serverUtil_okhttp.PostLike(mContext, object : serverUtil_okhttp.JsonResponseHandler_Like{
                    override fun onResponse(jsonObject: JSONObject, RcCode: String, Rcname:String) {
                        Log.d("여기_RcCode", RcCode.toString())
                        Log.d("여기_jsonObject", jsonObject.toString())
                        Log.d("여기_Rcname", Rcname.toString())
                        if(RcCode == "201"){
                            val likeCount = jsonObject.getInt("like_count")
                            Log.d("여기", likeCount.toString())


                            runOnUiThread {
                                binding.txtBsowLikeCount.text = likeCount.toString()
                                val animator = ValueAnimator.ofFloat(0f, 0.17f).setDuration(340)
                                animator.addUpdateListener {
                                    binding.lottieBshowHeart.progress = it.animatedValue as Float
                                }
                                animator.start()
                                is_like = true // 그리고 트루로 바꾼다.
                                Log.d("좋아요", "Bhow / 좋아요 버튼이 클릭됨")
                            }


                        }
                        else if(RcCode == "200"){
                            val likeCount = jsonObject.getInt("like_count")
                           runOnUiThread{
                               binding.txtBsowLikeCount.text = likeCount.toString()

                               Toast.makeText(mContext, "이미 좋아요를 눌렀답니다!", Toast.LENGTH_SHORT).show()
                           }
                        }
                        else{
                            val detail = jsonObject.getString("detail")
                            runOnUiThread{ Toast.makeText(mContext, "$detail", Toast.LENGTH_SHORT).show()
                            Log.d("여기_오류", detail)}
                        }


                    }
                })
               }

            else{ //트루일때가 실행된다.
                val myID = Context_okhttp.getID(mContext)
                serverUtil_okhttp.PostUnlike(mContext, object :serverUtil_okhttp.JsonResponseHandler_login{
                    override fun onResponse(
                        jsonObject: JSONObject,
                        RcCode: String,
                    ) {
                        if(RcCode=="200"){
                            Log.d("여기_else_RcCode", RcCode.toString())
                            Log.d("여기_else_jsonObject", jsonObject.toString())
                            val likeCount = jsonObject.getInt("like_count")
                            val myIdResult = jsonObject.getString("${myID}")
                            runOnUiThread {
                                binding.txtBsowLikeCount.text = likeCount.toString()
                                val animator = ValueAnimator.ofFloat(0.17f, 0f).setDuration(170)
                                animator.addUpdateListener {
                                    binding.lottieBshowHeart.progress = it.animatedValue as Float
                                }
                                animator.start()
                                is_like = false // 다시 false로 된다.
                                Log.d("좋아요", "Bhow / 좋아요 버튼이 꺼짐")
                            }//runOnUiThread
                        }
                    }})//onResponse
            }//else
        }
    }

    fun editDialogBtn(){
        // 버튼 가시성 설정
        val id = Context_okhttp.getID(mContext)
        val AnnounceAuthor = intent.getStringExtra("AnnounceAuthor").toString()
        Log.d("btnShow_id", id)
        Log.d("btnShow_AnnounceAuthor", AnnounceAuthor)
        if(id != AnnounceAuthor){
            binding.imgBshowEditOrDel.visibility = GONE
        }
        // 버튼 클릭 이벤트
        binding.imgBshowEditOrDel.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_or_del, null)
            val mBuilder = AlertDialog.Builder(mContext)
                .setView(mDialogView)
                .setTitle("수정 / 삭제")
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
                                Toast.makeText(mContext, "삭제완료", Toast.LENGTH_SHORT).show()
                                val bwIntent = Intent(mContext, MainActivity::class.java)
                                startActivity(bwIntent)
                            }
                        }
                        else{
//                            val detail = jsonObject.getString("detail")
                            runOnUiThread{
//                                Toast.makeText(mContext, "${detail}", Toast.LENGTH_SHORT).show()
                                Toast.makeText(mContext, "에러: ${RcCode}", Toast.LENGTH_SHORT).show()
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
        Log.d("디스트로이","onDestroy")
    }

    override fun onStop() {
        super.onStop()
        if(is_played == true){
        player.pause()
            is_played == false
        }
        Log.d("온스탑","onStop")
    }

}
//*********************************************************************************************
// 옛 코드 저장소
//    fun setText():String{
//        val AnnounceContent = intent.getStringExtra("AnnounceContent").toString()
//        val AnnounceTitle = intent.getStringExtra("AnnounceTitle").toString()
//        val AnnounceNickname = intent.getStringExtra("AnnounceNickname").toString()
//        val AnnounceAuthor = intent.getStringExtra("AnnounceAuthor").toString()
//        val AnnounceId = intent.getStringExtra("AnnounceId").toString()
//        Log.d("쿨톤", AnnounceContent )
//        Log.d("쿨톤", AnnounceTitle )
//        Log.d("쿨톤", AnnounceNickname )
//        Log.d("쿨톤", AnnounceAuthor )
//        Log.d("쿨톤", AnnounceId )
//
//        binding.txtBshowTitle.text=AnnounceTitle
//        binding.txtBshowContent.text=AnnounceContent
//        binding.txtBshowTime.text=AnnounceNickname
//
//        return AnnounceAuthor
//    }//setText()