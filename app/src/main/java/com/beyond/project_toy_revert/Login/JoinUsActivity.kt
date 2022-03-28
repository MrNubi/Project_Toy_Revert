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
import com.beyond.project_toy_revert.databinding.ActivityJoinUsBinding
import com.beyond.project_toy_revert.util.Context_okhttp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class JoinUsActivity : BasicActivity() {

    private lateinit var binding : ActivityJoinUsBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_us)


        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_us)

        binding.joinBtn.setOnClickListener {

            SignUpCheck()
            val SignUpCheckListResult : Boolean = SignUpCheck()?: false
            //요구사항 지켰으면 true, 아니면 false
            Signup(SignUpCheckListResult)
        }//setOnClickListener



    }//onCreate

    private fun SignUpCheck() : Boolean {
        var isGoToJoin = true

        val edt_userName = binding.userNameArea.text.toString()
        val edt_email = binding.emailArea.text.toString()
        val edt_password1 = binding.passwordArea1.text.toString()
        val edt_password2 = binding.passwordArea2.text.toString()

        // 각각의 값이 비어있는지 확인1
        if (edt_userName.isEmpty()) {
            Toast.makeText(mContext, R.string.plaese_nick, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        if (edt_email.isEmpty()) {
            Toast.makeText(this, R.string.plaese_email, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        // 각각의 값이 비어있는지 확인2
        if (edt_password1.isEmpty()) {
            Toast.makeText(this, R.string.plaese_password1, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        // 각각의 값이 비어있는지 확인3
        if (edt_password2.isEmpty()) {
            Toast.makeText(this, R.string.plaese_password2, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        // 비밀번호 2개가 같은지 확인
        if (!edt_password1.equals(edt_password2)) {
            Toast.makeText(this, R.string.mismatch_password, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        // 비밀번호가 6자 이상인지
        if (edt_password1.length < 6) {
            Toast.makeText(this, R.string.check_password, Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        return isGoToJoin

    }//fun SignUp()

    private fun Signup(isGoToJoin : Boolean){
        if (isGoToJoin == true) {
//            val retrfit = ServerAPI.getRetrofit(mContext)
//          var  apiList : APIList = retrfit.create(APIList::class.java)


            val edt_userName = binding.userNameArea.text.toString()
            val edt_nickname12 = binding.userNameArea.text.toString()
            val edt_email = binding.emailArea.text.toString()
            val edt_password1 = binding.passwordArea1.text.toString()
            val edt_password2 = binding.passwordArea2.text.toString()
            serverUtil_okhttp.putRequestSignUp(
                edt_userName,
                edt_email,
                edt_password1,
                edt_password2,
                object:serverUtil_okhttp.JsonResponseHandler_login{
                    override fun onResponse(jsonObj: JSONObject, rqCode:String) {
                        // 회원가입 성공 / 실패 분기
                        if(rqCode in "[\"token\",\"user\"]"){
                            val key = jsonObj.getString("token")
//                            val user = jsonObj.getString("user") // 이 안에 유저프로필 있음// pref에 받을거 다 받아놔라
                            Log.d("클락", key)


                                // 가입한 사람의 닉네임 추출 -> 000님 가입을 축하합니다.  토스트
                                // 회원가입화면 종료 -> 로그인화면 복귀


                                runOnUiThread {
                                    Toast.makeText(mContext, "${edt_userName}님, 가입을 축하합니다!", Toast.LENGTH_SHORT).show()
                                    Context_okhttp.setToken(mContext, key)
                                    Context_okhttp.setID(mContext, edt_userName)
                                    val myIntent =  Intent(mContext, MainActivity::class.java)
                                    startActivity(myIntent)
                                }}
                            else{
                                runOnUiThread {
                                    Log.d("칼", rqCode)
                                    Toast.makeText(mContext, "$rqCode" , Toast.LENGTH_SHORT).show()
                                }
                            }


                            // 화면 종료 : 객체 소멸(UI 동작 X)
                           // finish()


                        }


                    })
                }


//            apiList.putRequestSignUp(edt_userName, edt_email, edt_password1, edt_password2).enqueue(object :
//                Callback<BasicResponse> {
//                override fun onResponse(
//                    call: Call<BasicResponse>,
//                    response: Response<BasicResponse>
//                ) {
//                    if(response.isSuccessful){
//
//                        val br = response.body()
//                        Log.d("샹","${br?.username}")
//                        Toast.makeText(mContext, "${br?.username}님! 가입을 축하합니다!", Toast.LENGTH_SHORT).show()
//                        finish()
//                    }
//
//                }
//
//                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//                    Log.d("샹","ak")
//
//                }
//
//            })

//            auth.createUserWithEmailAndPassword(edt_email, edt_password1)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, R.string.Success, Toast.LENGTH_LONG).show()
//
//                        val intent = Intent(this, MainActivity::class.java)
//                        intent.flags =
//                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//
//
//                    }
//                    else {
//                        Toast.makeText(this, R.string.Fail, Toast.LENGTH_LONG).show()
//                    }
//                }
        }

    }// fun Signup()

//JoinAuActivity


