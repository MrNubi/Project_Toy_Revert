package com.beyond.project_toy_revert.util

import android.content.Context

class ContextUtil {

    companion object{

        private  val  prefName = "KeepTheTimePref"
        private  val  codename = "gg"
        private  val  codename2 = "gg"


        private val LOGIN_USER_TOKEN ="LOGIN_USER_TOKEN"

        fun setLoginUserToken(context:Context, token:String){

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(LOGIN_USER_TOKEN, token).apply()
        }

        fun getLoginUserToken(context: Context) : String{

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return  pref.getString(LOGIN_USER_TOKEN, "")!!
        }
        fun GT(context:Context, token:String){

            val pref = context.getSharedPreferences(codename, Context.MODE_PRIVATE)
            pref.edit().putString(codename, token).apply()
        }

        fun TYT(context: Context) : String{

            val pref = context.getSharedPreferences(codename, Context.MODE_PRIVATE)
            return  pref.getString(codename, "")!!
        }
    }
}