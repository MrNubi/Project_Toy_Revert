package com.beyond.project_toy_revert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.Login.LoginActivity
import com.beyond.project_toy_revert.databinding.ActivitySplashBinding
import com.beyond.project_toy_revert.util.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash)
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        if(auth.currentUser?.uid == null){newUser()}
        else{ currentUser() }

    }//onCreate

    fun newUser(){
        auth = Firebase.auth

            Log.d("Newb", "SplashActivity")

            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 3000)


    }//fun NewId()

    fun currentUser(){
        Log.d("currentUser", "SplashActivity")

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login_intro, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(R.string.check_AutoLogin)

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.btn_dialog_loginYes)?.setOnClickListener {
            Toast.makeText(this, R.string.Success, Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)

            alertDialog.dismiss()
        }//btn_dialog_loginYes

        alertDialog.findViewById<Button>(R.id.btn_dialog_loginNo)?.setOnClickListener {


            startActivity(Intent(this, LoginActivity::class.java))
            finish()


            alertDialog.dismiss()


        }//btn_dialog_loginNo
    }//fun currentId()


}//SplashActivity

