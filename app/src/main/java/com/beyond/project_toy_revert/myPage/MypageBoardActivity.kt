package com.beyond.project_toy_revert.myPage

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.MainFragAdapter
import com.beyond.project_toy_revert.adapter.MyPageFragmentSwipAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.avatara.AvataraActivity
import com.beyond.project_toy_revert.databinding.ActivityMypageBoardBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import org.json.JSONObject

class MypageBoardActivity : BasicActivity() {
    private lateinit var binding : ActivityMypageBoardBinding

    lateinit var myPageAdapter : MyPageFragmentSwipAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mypage_board)
        myPageAdapter = MyPageFragmentSwipAdapter(supportFragmentManager)
        binding.viewPager2MyPageFragmentSwiperZone.adapter =myPageAdapter
        binding.tabMyPageFragmentSwiperTab.setupWithViewPager(binding.viewPager2MyPageFragmentSwiperZone)
        binding.viewPager2MyPageFragmentSwiperZone.setCurrentItem(1)
        settingIntroducePage()
        setAbatara()
        settingIntroducePage()
        clickEvent()
    }
    fun clickEvent(){
        binding.imgMypageAvataraHead.setOnClickListener {
            val aIntent = Intent(mContext,AvataraActivity::class.java)
            startActivity(aIntent)
        }
    }

    fun settingIntroducePage(){
        //Splash에서 about_me 받아서 메모장에 저장 후 받아서 여기 입력하자
        // 입력은 뭐 생기고 나서 고민
        serverUtil_okhttp.getMyData(mContext,"","","profile/",object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                val myFallowingCount = jsonObject.getInt("following_count")
                val myFallowerCount = jsonObject.getInt("follower_count")
                val myPostCount = jsonObject.getInt("post_count")
                val aboutMe:String?= jsonObject.getString("about_me")
                val aboutMeNullchecked = if(aboutMe=="null")"자기소개 미작성" else aboutMe
                Log.d("내소개",aboutMeNullchecked.toString())

                runOnUiThread {
                    binding.btnMyPageFallowerCount.text = "팔로워\n${myFallowerCount}"
                    binding.btnMyPageFallowingCount.text = "팔로잉\n${myFallowingCount}"
                    binding.btnMyPageMyWriteCount.text = "내가 쓴 글\n${myPostCount}"
                    binding.txtMyPageIntroduceMySelf.text = "${aboutMeNullchecked}"
                }//runOnUiThread
            }})
    }//fun settingIntroducePage()

    fun setAbatara(){
        serverUtil_okhttp.getMyData(mContext,"","/avatar","",object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                val abataraCode = jsonObject.getJSONArray("results").getJSONObject(0)
               var head = abataraCode.getString("head")
               var body = abataraCode.getString("upper_body")
               var showes = abataraCode.getString("lower_body")
               runOnUiThread{

                   when(head){
                       "1" -> Glide.with(mContext).load(R.drawable.samp_hat1).into(binding.imgMypageAvataraHat)
                       "2" ->  Glide.with(mContext).load(R.drawable.samp_hat2).into(binding.imgMypageAvataraHat)
                       else -> Log.d("에러 - head is not 1 or 2"," ${head}", Throwable())
                   }
                   when(body){
                       "0" -> Glide.with(mContext).load(R.drawable.samp_head_cut).into(binding.imgMypageAvataraHead)
                       "1" -> Glide.with(mContext).load(R.drawable.samp_head_cut).into(binding.imgMypageAvataraHead)
                       "2" -> Glide.with(mContext).load(R.drawable.samp_head_cut2).into(binding.imgMypageAvataraHead)
                       else -> Log.d("아바타 오류", "모자:${head}, 머리:${body}")
                   }//when(body)

               }//runOnUiThread

            }
        })
    }//fun setAbatara()


}