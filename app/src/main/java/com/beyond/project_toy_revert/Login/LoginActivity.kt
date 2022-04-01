package com.beyond.project_toy_revert.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.ActivityLoginBinding
import com.beyond.project_toy_revert.util.Context_okhttp
import org.json.JSONObject

class LoginActivity : BasicActivity() {


    private lateinit var binding : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val Oldid = Context_okhttp.getID(mContext)
        if(Oldid != "ID"){
             binding.editLoginActivityId.setText(Context_okhttp.getID(mContext))
        }

        binding.autoLoginCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->

            Log.d("체크값변경", "${isChecked}로 변경됨")

            // <연습문제>
            // ContextUtil를 이용해서, true로 변경되면 -> 자동로그인값도 true로 저장
            // false로 되면, 자동로그인 값도 false로 저장
            Context_okhttp.setAutoLogin(mContext, isChecked)

        }



        binding.btnLoginActivityLogIn.setOnClickListener {
            val inputId = binding.editLoginActivityId.text.toString()
            val inputPw = binding.editLoginActivityPw.text.toString()

            serverUtil_okhttp.postReqestLogin(inputId, "" ,inputPw, object : serverUtil_okhttp.JsonResponseHandler_login{
                override fun onResponse(jsonObject: JSONObject, RcCode:String) {

                    // 화면의 입장에서, 로그인 결과를 받아서 처리할 코드
                    // 서버에 다녀오고 실행 : 라이브러리가 자동으로 백그라운데어서 돌도록 만들어둔 코드
                    if(RcCode == "[\"token\",\"user\"]"){
                        val key = jsonObject.getString("token")




                        runOnUiThread {
                            Toast.makeText(mContext, "${inputId}님, 환영합니다!", Toast.LENGTH_SHORT).show()
                            Context_okhttp.setToken(mContext, key)
                            Context_okhttp.setID(mContext, inputId)


                            Log.d("캬옹", inputId)
                            val myIntent =  Intent(mContext, MainActivity::class.java)
                            myIntent.putExtra("id", inputId)
                            startActivity(myIntent)
                        }//runOnUiThread
                    }else{
                        Context_okhttp.setID(mContext, inputId)
                        runOnUiThread { Toast.makeText(mContext,"$RcCode", Toast.LENGTH_SHORT).show()}
                    }




//                    if(code==200){
//                        runOnUiThread {
//
////                                // 토스트를 띄우는 코드만, UI 전담 쓰레드에서 실행하도록
////                                Toast.makeText(mContext, "로그인성공", Toast.LENGTH_SHORT).show()
//
//                            //[도전과제] 로그인 한 사람의 닉네임을 추출, "000님, 환영합니다.!" 로 토스트 띄우기
//                            val dataObj = jsonObject.getJSONObject("data")
//                            val userObj = dataObj.getJSONObject("user")
//                            val nickname =  userObj.getString("nick_name")
//
//                            runOnUiThread {
//                                Toast.makeText(this@LoginActivity, "${nickname}님, 환영합니다!", Toast.LENGTH_SHORT).show()
//                            }
//
//                            // 서버가 내려준 토큰값을 변수에 담아보자
//                            val token = dataObj.getString("token")
//                            Context_okhttp.setToken(this@LoginActivity, token)
//
//                            // 변수에 담긴 토큰값(String) 을 SharedPreferences에 담아두자
//                            // 로그인 성공시에는 담기만,  필요한 화면/클래스에서 꺼내서 사용
//
//
//
//                            // 메인 화면으로 진입 => 클래스의 객체화 (UI동작 X)
//                            val myIntent =  Intent(this@LoginActivity, MainActivity::class.java)
//                            startActivity(myIntent)
//
//                        }
//                    }
//                    else{
//                        val message = jsonObject.getString("message")
//
//                        // 토스트: UI 조작 => 백그라운드에서 UI를 건드리면, 위험한 동작으로 간주하고  앱을 강제 종료
//                        runOnUiThread {
//
//                            // 토스트를 띄우는 코드만, UI 전담 쓰레드에서 실행하도록
//                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
//                        }
//                    }

                }//override fun onResponse
            })//serverUtil_okhttp.postReqestLogin

//            auth.signInWithEmailAndPassword(id, password)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//
//                        val intent = Intent(this, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//
//                        Toast.makeText(this, R.string.Success, Toast.LENGTH_LONG).show()
//
//                    } else {
//
//                        Toast.makeText(this, R.string.Fail, Toast.LENGTH_LONG).show()
//
//                    }
//                }
        }//btnLoginActivityLogIn

        binding.btnLoginActivitySignIn.setOnClickListener {
            val intent = Intent(this, JoinUsActivity::class.java)
            startActivity(intent)
        }//btnLoginActivitySignIn

        binding.btnFindPassword.setOnClickListener {
            val intent = Intent(this, FindPasswordActivity::class.java)
            startActivity(intent)
        }//btnFindPassword

    }
}