package com.beyond.project_toy_revert

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.databinding.ActivitySplashBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class SplashActivity : BasicActivity() {
    private lateinit var auth: FirebaseAuth



    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

//        if(auth.currentUser?.uid == null){newUser()}
//        else{ currentUser() }
        val pl = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d("권한 허용","향")

                val myHandler = Handler(Looper.getMainLooper())

                myHandler.postDelayed({
                    // 자동로그인을 해도 되는가?
                    // 1) 사용자가 자동로그인 의사를 ok했는지?
                    val userAutoLogin =  Context_okhttp.getAutoLogin(mContext)
                    val userToken = Context_okhttp.getToken(mContext)
                    val userID = Context_okhttp.getID(mContext)

                    // 2) 로그인 시에 받아낸 토큰값이 지금도 유효한지?
                    // 2-1) 저장된 토큰이 있는지? (임시방편)
                    // 2-2) 그 토큰이 사용자 정보를 불러내는지? (실제)
                    //  => 2.5초 전에 미리 물어본 상태. isTokenOk에 결과가 들어있다

                    // Intent로 화면 이동 => 상황에 따라 다른 Intent를 만든다
                    val myIntent : Intent

                    if(userAutoLogin && userToken != "TOKEN"){
                        // 둘다 ok라면, 바로 메인화면으로
                        Toast.makeText(mContext, "$userID"+"님 환영합니다", Toast.LENGTH_SHORT).show()
                        Log.d("캬옹", "$userToken")
                        Log.d("캬옹", "$userID")
                        myIntent = Intent(mContext, MainActivity::class.java)
                    }
                    else{
                        // 아니라면, 로그인 화면으로
                        Log.d("캬옹", "$userToken")
                        Log.d("캬옹옹", "$userID")
                        myIntent = Intent(mContext, ConvertActivity::class.java)
                    }

                    startActivity(myIntent)
                    finish()

                }, 2500)

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(mContext, "권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()


            }
        }

        TedPermission.create()
            .setPermissionListener(pl)
            .setDeniedMessage("권한을 거부하면 통화가 불가능합니다. 설정 > 권한에서 허용해주세요.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정으로 가기")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
            .check()


        
//        Handler().postDelayed({
//            startActivity(Intent(this, ConvertActivity::class.java))
//            finish()
//        }, 3000)

    }//onCreate

//    fun newUser(){
//        auth = Firebase.auth
//
//            Log.d("Newb", "SplashActivity")
//



    }//fun NewId()

//    fun currentUser(){
//        Log.d("currentUser", "SplashActivity")
//
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login_intro, null)
//        val mBuilder = AlertDialog.Builder(this)
//            .setView(mDialogView)
//            .setTitle(R.string.check_AutoLogin)
//
//        val alertDialog = mBuilder.show()
//        alertDialog.findViewById<Button>(R.id.btn_dialog_loginYes)?.setOnClickListener {
//            Toast.makeText(this, R.string.Success, Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, MainActivity::class.java)
//
//            startActivity(intent)
//
//            alertDialog.dismiss()
//        }//btn_dialog_loginYes
//
//        alertDialog.findViewById<Button>(R.id.btn_dialog_loginNo)?.setOnClickListener {
//
//
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//
//
//            alertDialog.dismiss()

//
//        }//btn_dialog_loginNo
//    }//fun currentId()
//
//
//}//SplashActivity

