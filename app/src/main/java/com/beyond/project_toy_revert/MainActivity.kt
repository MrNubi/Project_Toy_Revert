package com.beyond.project_toy_revert

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.adapter.MainFragAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.avatara.AvataraActivity
import com.beyond.project_toy_revert.databinding.ActivityMainBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.myPage.MypageBoardActivity
import com.beyond.project_toy_revert.testActivity.testWriteDownActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : BasicActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mAdpater : MainFragAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        mAdpater = MainFragAdapter(supportFragmentManager)
        binding.viewMainv2.adapter =mAdpater
        binding.tabMainTop.setupWithViewPager(binding.viewMainv2)
        binding.viewMainv2.setCurrentItem(1)
        Log.d("옹", Context_okhttp.getToken(mContext))
        val avataraUri = Context_okhttp.getAvatara(mContext)
        if(avataraUri != "123")
        Glide.with(mContext).load(avataraUri.toUri()).into(binding.imgMainAvatarZone)
        checkAvatara()
        binding.imgMainAvatarZone.setOnClickListener {
            val I = Intent(mContext, AvataraActivity::class.java)
            startActivity(I)
        }
        binding.imgMainProf.setOnClickListener {
            Context_okhttp.setToken(mContext,"TOKEN")
            Context_okhttp.setID(mContext, "")
            // + 서버 로그아웃 활성화
            val myIntent =  Intent(mContext, SplashActivity::class.java)
            startActivity(myIntent)
        }
        binding.imgMainCloud.setOnClickListener {
            val myIntent =  Intent(mContext, MypageBoardActivity::class.java)
            startActivity(myIntent)
        }


//        val id = Context_okhttp.getID(mContext)
//        binding.textTest.text = "${id}님을 환영합니다"
//
//
//
//        binding.btnEscape.setOnClickListener {
//
//
//            Context_okhttp.setToken(mContext,"TOKEN")
//            Context_okhttp.setID(mContext, "")
//            // + 서버 로그아웃 활성화
//            val myIntent =  Intent(mContext, SplashActivity::class.java)
//            startActivity(myIntent)
//        }
    }

    fun checkAvatara(){
        serverUtil_okhttp.getProfileData(mContext,object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {


                if(RcCode=="200"){
                    val AvataraOK = jsonObject.getInt("count")
                    if (AvataraOK != 0) {
                        val Aresult: JSONArray? = jsonObject.getJSONArray("results")
                        if (Aresult != null) {
                            val t = Aresult.getJSONObject(0).getString("id")
                            Context_okhttp.setAid(mContext, t)
                            Log.d("아바타아이디", t)
                        } else {
                            Context_okhttp.setAid(mContext, "0")
                            Log.d("아바타없음", "AID=0")
                        }
                    } else {
                        Context_okhttp.setAid(mContext, "0")
                        Log.d("아바타없음", "AID=0")
                    }

                    Context_okhttp.setAvataOk(mContext, AvataraOK.toString())
                }

                else{
                    Log.d("아바타에러","${RcCode}")
                }
            }
        })

    }


    var mBackWait: Long = 0


    override fun onBackPressed() {
        // 뒤로가기버튼클릭

        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(baseContext, "한번더누르시면종료됩니다", Toast.LENGTH_SHORT).show()
        } else {moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());}}
}
/*
摩訶般若波羅蜜多心經
마하반야바라밀다심경

觀自在菩薩 行深般若波羅蜜多時
관자재보살 행심반야바라밀다 시
관자재보살이 깊은 반야바라밀다를 행할 때

照見五蘊皆空 度一切苦厄
조견오온개공 도일체고액
다섯가지 쌓임이 모두 공한 것을 비추어보고
온갖 괴로움과 재앙을 건지느리라

舍利子 色不異空 空不異色
사리자 색불이공 공불이색
사리불자여 물질이 공과 다르지 않고 공이 물질과 다르지 않으며

色卽是空 空卽是色 受想行識 亦復如是
색즉시공 공즉시색 수상행식 역부여시
물질이 곧 공이요 공이 곧 물질이니
느낌과 생각과 지어감과 의식 또한 그러하니라

舍利子 是諸法空相 不生不滅
사리자 시제법공상 불생불멸
사리불자여 법은 본래 공한 모양으로 나지도 않고 없어지지도 않으며

不垢不淨 不增不減
불구부정 부증불감
더럽지도 않고 깨끗하지도 않으며
늘지도 않고 줄어들지도 않느니라

是故空中無色 無受想行識
시고공중무색 무수상행식
그러므로 공 가운데에는 물질도 없고 느낌과 생각과 지어감과 의식도 없으며

 無眼耳鼻舌身意 無色聲香味觸法 無眼界 乃至
무안이비설신의 무색성향미촉법 무안계 내지
눈과 귀와 코와 혀와 몸과 뜻도 없으며 빛과 소리와 냄새와 맛과 느낌과 법도 없으며 눈의 경계도 없으며

 無意識界 無無明 亦無無明盡 乃至
무의식계 무무명 역무무명진 내지
의식의 경계까지도 없으며 무명도 없고 ㅗ또 무명이 다함도 없으며

 無老死 亦無老死盡
무노사 역무노사진
늙고 죽음이 없고 늙고 죽음이 다함도 없느니라

無苦集滅道 無智亦無得 以無所得故
무고집멸도 무지역무득 이무소득고
 괴로움과 괴로움의 원인과 괴로움의 없어짐과 괴로움을 없애는 길도 없으며 지혜도 없으며 얻음도 없느니라 얻을 것이 없는 까닭에

 菩提薩陀依般若波羅蜜多
보리살타 의반야바라밀다
보살은 반야바라밀다를 의지하므로

故心無罣碍 無罣碍故 無有恐怖
고심무가애 무가애고 무유공포
마음에 걸림이 없고 걸림이 없으므로 두려움이 없어서

遠離顚倒夢想 究竟涅槃
원리전도몽상 구경열반
뒤바뀐 헛된 생각을 아주 떠나 완전한 열반에 들어가며

三世諸佛 依般若波羅蜜多故
삼세제불 의반야바라밀다고
과거 현재 미래의 모든 부처님도 이 반야바라밀다를 의지하므로

得阿縟多羅三邈三菩提
 득아뇩다라삼먁삼보리
아뇩다라 삼먁삼보리를 얻었느니라

故知般若波羅蜜多 是大神呪 是大明呪
고지반야바라밀다 시대신주 시대명주
그러므로 반야바라밀다는 가장 신비한 주문이며 가장 밝은 주문이며

是無上呪 是無等等呪
시무상주 시무등등주
 가장 높은 주문이며 아무것도 견줄 수 없는 주문이니

能祭一切苦 真実不虚
능제일체고 진실불허
온갖 괴로움을 없애고 진실하여 허망하지 않음을 알아라

故說般若波羅蜜多呪 卽說呪曰
고설반야바라밀다주 즉설주왈
그러므로 반야바라밀다의 주문을 말하느니
주문은 이러하느니라

揭諦揭諦 波羅揭諦 波羅僧揭諦 菩提薩婆訶
아제아제 바라아제 바라승아제 모지사바하
揭諦揭諦 波羅揭諦 波羅僧揭諦 菩提薩婆訶
아제아제 바라아제 바라승아제 모지사바하
揭諦揭諦 波羅揭諦 波羅僧揭諦 菩提薩婆訶
아제아제 바라아제 바라승아제 모지사바하
*/