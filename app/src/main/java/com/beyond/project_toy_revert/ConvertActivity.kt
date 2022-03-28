package com.beyond.project_toy_revert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.Login.JoinUsActivity
import com.beyond.project_toy_revert.Login.LoginActivity
import com.beyond.project_toy_revert.databinding.ActivityConvertBinding
import com.google.android.gms.common.SignInButton

class ConvertActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConvertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_convert)

        binding.btnGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.btnGoToSignIn.setOnClickListener {
            startActivity(Intent(this, JoinUsActivity::class.java))
            finish()
        }

    }
}