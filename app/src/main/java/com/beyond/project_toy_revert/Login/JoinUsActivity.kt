package com.beyond.project_toy_revert.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.ActivityJoinUsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinUsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinUsBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_us)


        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_us)

        binding.joinBtn.setOnClickListener {

            SignUpCheck()
            val SignUpCheckListResult : Boolean = SignUpCheck()as Boolean
            //요구사항 지켰으면 true, 아니면 false
            Signup(SignUpCheckListResult)
        }//setOnClickListener



    }//onCreate

    private fun SignUpCheck() : Boolean {
        var isGoToJoin = true

        val email = binding.emailArea.text.toString()
        val password1 = binding.passwordArea1.text.toString()
        val password2 = binding.passwordArea2.text.toString()

        // 각각의 값이 비어있는지 확인1
        if (email.isEmpty()) {
            Toast.makeText(this, R.string.plaese_email, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        // 각각의 값이 비어있는지 확인2
        if (password1.isEmpty()) {
            Toast.makeText(this, R.string.plaese_password1, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        // 각각의 값이 비어있는지 확인3
        if (password2.isEmpty()) {
            Toast.makeText(this, R.string.plaese_password2, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        // 비밀번호 2개가 같은지 확인
        if (!password1.equals(password2)) {
            Toast.makeText(this, R.string.mismatch_password, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        // 비밀번호가 6자 이상인지
        if (password1.length < 6) {
            Toast.makeText(this, R.string.check_password, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        return isGoToJoin

    }//fun SignUp()

    private fun Signup(isGoToJoin : Boolean){
        if (isGoToJoin == true) {
            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            auth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, R.string.Success, Toast.LENGTH_LONG).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)


                    }
                    else {
                        Toast.makeText(this, R.string.Fail, Toast.LENGTH_LONG).show()
                    }
                }
        }

    }// fun Signup()

}//JoinAuActivity


