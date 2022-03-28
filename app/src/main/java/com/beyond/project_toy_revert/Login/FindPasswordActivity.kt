package com.beyond.project_toy_revert.Login

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.ActivityFindPasswordBinding

class FindPasswordActivity : BasicActivity() {
    private lateinit var binding : ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_find_password)

        binding.btnFindingPassword1.setOnClickListener {
            Toast.makeText(this, "비밀번호찾기1 기능실행", Toast.LENGTH_SHORT).show()
        }
        binding.btnFindingPassword2.setOnClickListener {
            Toast.makeText(this, "비밀번호찾기2 기능 실행", Toast.LENGTH_SHORT).show()
        }


    }
}