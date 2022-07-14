package com.beyond.project_toy_revert.util

import android.content.Context

class Context_okhttp {
    companion object{

        // 메모장 파일 이름처럼, SharedPreferences의 이름 설정
        private val prefName = "OkHttpPracticePref"// 위치
        private val prefID = "OkHttpPracticePrefID"

        // 저장할 데이터의 항목명도 변수로 만들어두자
        private val TOKEN = "TOKEN"
        private val POSTID = "12"
        private val ReplyID = "12"
        private val avataraResult = "123"
        private val Nick ="NICK"
        private val UID = "ID"
        private val AID = "ID"
        private val VIDEOUID = "VIDEOUID"
        private val AVATARAOK = "OK?"
        private val AUTO_LOGIN = "AUTO_LOGIN"
        private val Auto_Boolean = false
        private val mp_Pagination = "1"


        // 데이터 저장 함수(setter) / 조회 함수 (getter)   별개로 작성
        // TOKEN항목에 저장 => token항목 조회? 데이터 인식x.  대소문자까지 동일해야함
        // 오타를 줄이고, 코딩 편하게 하는 조치

        fun setAvatara(context: Context, avataraCode:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(avataraResult, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(avataraResult, avataraCode).apply()

        }

        fun getAvatara(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(avataraResult, Context.MODE_PRIVATE)
            //
            return  pref.getString(avataraResult, "")!!
        }
        //*********************************************************************************************
        fun setPg(context: Context, mpPgCode:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(mp_Pagination, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(mp_Pagination, mpPgCode).apply()

        }

        fun getPg(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(mp_Pagination, Context.MODE_PRIVATE)
            //
            return  pref.getString(mp_Pagination, "")!!
        }

        //*********************************************************************************************

        fun setAid(context: Context, Aid:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(AID, Aid).apply()

        }

        fun getAid(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //
            return  pref.getString(AID, "")!!
        }



        //************************************************************************************************8
        fun setUri(context: Context, nick:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(Nick, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(Nick, nick).apply()

        }

        fun getUri(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(Nick, Context.MODE_PRIVATE)
            //
            return  pref.getString(Nick, "")!!
        }

        fun setVideoUri(context: Context, Video:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(VIDEOUID, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(VIDEOUID, Video).apply()

        }

        fun getVedeoUri(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(VIDEOUID, Context.MODE_PRIVATE)
            //
            return  pref.getString(VIDEOUID, "")!!
        }
//*****************************************************************************************
        fun setPostId(context: Context, postid:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(POSTID, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(POSTID, postid).apply()

        }

        fun getPostId(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(POSTID, Context.MODE_PRIVATE)
            //
            return  pref.getString(POSTID, "")!!
        }
        //***********************************************************************
        fun setPk(context: Context, replyid:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(ReplyID, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(ReplyID, replyid).apply()

        }

        fun getPk(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(ReplyID, Context.MODE_PRIVATE)
            //
            return  pref.getString(ReplyID, "")!!
        }
//********************************************************************************
fun setAvataOk(context: Context, avataOK:String){

    // 메모장 파일을 열자
    val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    // 입력들어온 token 내용 (TOKEN 항목에) 저장
    pref.edit().putString(AVATARAOK, avataOK).apply()

}

        fun getAvataOk(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //
            return  pref.getString(AVATARAOK, "")!!
        }
//********************************************************************************
        fun setToken(context: Context, token:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(TOKEN, token).apply()

        }

        fun getToken(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //
            return  pref.getString(TOKEN,"")!!
        }

        fun setID(context: Context, ID:String){

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            // 입력들어온 token 내용 (TOKEN 항목에) 저장
            pref.edit().putString(UID, ID).apply()

        }

        fun getID(context: Context):String{

            // 메모장 파일을 열자
            val pref  =  context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //
            return  pref.getString(UID, "")!!
        }


        fun setAutoLogin(context: Context, isAuto:Boolean){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(AUTO_LOGIN, isAuto).apply()
            //
        }

        fun getAutoLogin(context: Context):Boolean{
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN, false)!!
        }
    }
}