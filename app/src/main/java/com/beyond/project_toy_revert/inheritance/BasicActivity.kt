package com.beyond.project_toy_revert.inheritance

import android.app.TaskStackBuilder.create
import android.content.Context
import android.content.IntentFilter.create
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ScrollerCompat.create
import com.beyond.project_toy_revert.api.APIList
import com.beyond.project_toy_revert.api.ServerAPI
import com.google.android.gms.common.images.ImageManager.create
import com.gun0912.tedpermission.normal.TedPermission.create
import retrofit2.Retrofit

abstract class BasicActivity : AppCompatActivity(){
    lateinit var apiList : APIList

    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        mContext = this  // 화면이 만들어질때 this를 대입

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)


     }
}