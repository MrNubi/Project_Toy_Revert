package com.beyond.project_toy_revert.api

import android.content.Context
import android.net.rtp.RtpStream
import android.util.Log
import com.beyond.project_toy_revert.util.Context_okhttp
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class serverUtil_okhttp {
    // 서버유틸로 돌아온 응답을  => 액티비티에서 처리하도록, 일처리 넘기기
    // 나에게 생긴일을 -> 다른 클래스에게 처리요청: interface 활용
    interface JsonResponseHandler_del{
        fun onResponse(RcCode:String)
    }
    interface JsonResponseHandler_login{
        fun onResponse(jsonObject: JSONObject, RcCode:String)
    }
    interface JsonResponseHandler_Like{
        fun onResponse(jsonObject: JSONObject, RcCode:String, Rcname:String)
    }
    interface JsonResponseHandler{
        fun onResponse(jsonObject: JSONObject)
    }


    // 서버에 Request를 날리는 역할
    // 함수를 만들려고 하는데, 어떤 객체가 실행해도 결과만 잘 나오면 그만인 함수
    // 코틀린에서 자바의 static에 해당하는 개념? companion object {  } 에 만들자

    companion object{

        // 서버 컴퓨터 주소만 변수로 저장(관리 일원화) => 외부노출X
        private val BASE_URL = "http://54.180.52.26"

        // 로그인 기능 호출 함수
        // handler :  이 함수를 쓰는 화면에서, JSON 분석을 어떻게 / UI에서 어떻게 활용할지 방안(인터페이스)
        //  -처리방안을 임시로 비워두려면, null 대입 허용해야함
        fun postReqestLogin(username:String, id:String, pw:String, handler:JsonResponseHandler_login?){

            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/rest-auth/login/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("username", username)
                .add("email", id)
                .add("password", pw)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    Log.d("캬옹", response.toString())
                    val bodyString =  response.body?.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용
                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)

                    val GT = jsonObj.names().toString()

                    Log.d("서버테스트",jsonObj.toString())
                    Log.d("서버테스트",GT)


                    handler?.onResponse(jsonObj,GT)

                }
            })


        }

        fun postReply(context:Context, message:String, handler:JsonResponseHandler_login?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)
            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/post/${postedID}/comments/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("message", message)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .post(formData)
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    Log.d("캬옹", response.toString())
                    val bodyString =  response.body?.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용
                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)

                    val Rccode = response.code.toString()

                    Log.d("댓글쓰기_응답내용",jsonObj.toString())
                    Log.d("댓글쓰기_응답코드",Rccode)


                    handler?.onResponse(jsonObj,Rccode)

                }
            })


        }

        fun getProfileData(context:Context, handler:JsonResponseHandler_login?){
            val postedPK = Context_okhttp.getPk(context)
            // 1) 어느 주소로 가야하는가? + 어떤 파라미터를 첨부하는가? 도 주소에 같이 포함
            // => 라이브러리의 도움을 받자  HttpUrl 클래스 (OkHttp 소속)
            val urlBuilder = "http://luckyfriends.kro.kr/users/${postedPK}/avatar/".toHttpUrlOrNull()!!.newBuilder()

                .build()

            val urlString = urlBuilder.toString()
//            Log.d("완성된url: ", urlString)

            // 2) 요청 정보 정리 -> Request 생성
            val reauest = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .get()
                .build()

            // 3) Request 완성-> 서버에 호출, 응답을 화면에 넘기자
            val client = OkHttpClient()
            client.newCall(reauest).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    val KT = jsonObj.names().toString()
                    val code = response.code
                    Log.d("getProfile_이름", KT)
                    Log.d("getProfile_코드", code.toString())
                    Log.d("getProfile_서버응답: ", jsonObj.toString())

                    handler?.onResponse(jsonObj,code.toString())
                }
            })

        }

        fun patchProfile(context:Context, Head:String,Body:String,Shoes:String, handler:JsonResponseHandler_login?){
            val postedPK = Context_okhttp.getPk(context)
            val postedAID = Context_okhttp.getAid(context)
            Log.d("유알엘pk", postedPK)
            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/users/${postedPK}/avatar/${postedAID}/"


            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("head", Head)
                .add("upper_body", Body)
                .add("lower_body", Shoes)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .patch(formData)
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    Log.d("캬옹", response.toString())
                    val bodyString =  response.body?.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용
                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)

                    val Rccode = response.code.toString()

                    Log.d("댓글쓰기_응답내용",jsonObj.toString())
                    Log.d("댓글쓰기_응답코드",Rccode)


                    handler?.onResponse(jsonObj,Rccode)

                }
            })


        }

        fun postProfile(context:Context, Head:String,Body:String,Shoes:String, handler:JsonResponseHandler_login?){
            val postedPK = Context_okhttp.getPk(context)
            Log.d("유알엘pk", postedPK)
            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/users/${postedPK}/avatar/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("head", Head)
                .add("upper_body", Body)
                .add("lower_body", Shoes)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .post(formData)
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    Log.d("캬옹", response.toString())
                    val bodyString =  response.body?.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용
                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)

                    val Rccode = response.code.toString()

                    Log.d("댓글쓰기_응답내용",jsonObj.toString())
                    Log.d("댓글쓰기_응답코드",Rccode)


                    handler?.onResponse(jsonObj,Rccode)

                }
            })


        }


        // 회원가입 기능함수
        fun putRequestSignUp(username:String,email:String, pw:String, nickname:String, handler: JsonResponseHandler_login?){

            val urlString = "http://luckyfriends.kro.kr/rest-auth/registration/"

            val formData = FormBody.Builder()
                .add("username", username)
                .add("nickname", username)
                .add("email", email)
                .add("password1", pw)
                .add("password2", nickname)
                .build()

            val request =  Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 물리적인 연결실패 - 따로코딩 x 예정
                    Log.d("캬오옹", "실패")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("캬오옹", response.toString())
                    val GG = response.code
                    if(GG==201){



                        val bodyString = response.body!!.string()
                        val jsonObj =  JSONObject(bodyString)
                        val rqName =   jsonObj.names().toString()
                        Log.d("캬오옹 서버응답: ", rqName)


                        Log.d("캬오옹 서버응답: ", jsonObj.toString())
                        handler?.onResponse(jsonObj,rqName)

                    }
                    else{

                        Log.d("캬오옹 else" , GG.toString())


                    }




                }

            })

        }

        // 이메일  or 닉네임 중복 검사 함수
        fun getPostData(context:Context, handler:JsonResponseHandler_login?){

            // 1) 어느 주소로 가야하는가? + 어떤 파라미터를 첨부하는가? 도 주소에 같이 포함
            // => 라이브러리의 도움을 받자  HttpUrl 클래스 (OkHttp 소속)
            val urlBuilder = "http://luckyfriends.kro.kr/post".toHttpUrlOrNull()!!.newBuilder()

                .build()

            val urlString = urlBuilder.toString()
//            Log.d("완성된url: ", urlString)

            // 2) 요청 정보 정리 -> Request 생성
            val reauest = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .get()
                .build()

            // 3) Request 완성-> 서버에 호출, 응답을 화면에 넘기자
            val client = OkHttpClient()
            client.newCall(reauest).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    val KT = jsonObj.names().toString()
                    val code = response.code
                    Log.d("이름", KT)
                    Log.d("코드", code.toString())
                    Log.d("서버응답: ", jsonObj.toString())

                    handler?.onResponse(jsonObj,KT)
                }
            })

        }

        // 연습 : 내정보 불러오기 (/user_info - GET)
        // 토큰은, ContextUtil 클래스에서  getToken함수로 꺼내올 수 있다
        // 토큰값 자체는 파라미터로 받아올 필요 없다 => ContextUtil를 불러서 사용하자
        // 메모장에 접근할 수 있게, Context변수 하나를 미리 받아두자
        fun getPostDataID(context: Context, handler: JsonResponseHandler_login?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)

            val urlBuilder = "http://luckyfriends.kro.kr/post/${postedID}".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()
            Log.d("유알엘", urlString)

            val request =  Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .get()
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    val KT = response.code.toString()
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj,KT)
                }
            })
        }
        //*********************************************************************************
        fun getAllReplyData(context: Context, handler: JsonResponseHandler_login?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)

            val urlBuilder = "http://luckyfriends.kro.kr/post/${postedID}/comment-all".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()
            Log.d("유알엘", urlString)

            val request =  Request.Builder()
                .url(urlString)
//                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .get()
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    val KT = response.code.toString()
                    Log.d("서버응답_getReplyData", jsonObj.toString())
                    handler?.onResponse(jsonObj,KT)
                }
            })
        }
        //*********************************************************************************
        fun getReplyData(context: Context, handler: JsonResponseHandler_login?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)

            val urlBuilder = "http://luckyfriends.kro.kr/post/${postedID}/comment-noreply".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()
            Log.d("유알엘", urlString)

            val request =  Request.Builder()
                .url(urlString)
//                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .get()
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    val KT = response.code.toString()
                    Log.d("서버응답_getReplyData", jsonObj.toString())
                    handler?.onResponse(jsonObj,KT)
                }
            })
        }
        //*********************************************************************

        fun postReReply(context:Context,parent:Int, message:String, handler:JsonResponseHandler_login?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)
            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/post/${postedID}/comments/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("message", message)
                .add("parent", parent.toString())
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .post(formData)
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    Log.d("캬옹", response.toString())
                    val bodyString =  response.body?.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용
                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)

                    val Rccode = response.code.toString()

                    Log.d("대댓글쓰기_응답내용",jsonObj.toString())
                    Log.d("대댓글쓰기_응답코드",Rccode)


                    handler?.onResponse(jsonObj,Rccode)

                }
            })


        }

        //*********************************************************************
        fun getReReplyData(context: Context,replyId:String, handler: JsonResponseHandler_login?){


            val urlBuilder = "http://luckyfriends.kro.kr/post/${replyId}/replys".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()
            Log.d("유알엘", urlString)

            val request =  Request.Builder()
                .url(urlString)
//                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .get()
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    val KT = response.code.toString()
                    Log.d("서버응답_ReReplyData", jsonObj.toString())
                    handler?.onResponse(jsonObj,KT)
                }
            })
        }

        //*********************************************************************
        fun PostLike(context: Context, handler: JsonResponseHandler_Like?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)

            val urlString = "http://luckyfriends.kro.kr/post/${postedID}/like/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("token", "token")
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .post(formData)
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    val bodyString =  response.body!!.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용

                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)
                    val RcName = jsonObj.names().toString()
                    val rc = response.code


                    Log.d("토큰 이름",RcName.toString())
                    Log.d("토큰 바디",jsonObj.toString())
                    Log.d("토큰 코드",rc.toString())

                    // 실제 handler변수에,  jsonObj를 가지고 화면에서 어떻게 처리할지 계획이 들어와있다
                    // (계획이  되어있을때만) 해당 계획을 실행하자
                    handler?.onResponse(jsonObj,rc.toString(),RcName)

                }
            })
        }

        fun PostUnlike(context: Context, handler: JsonResponseHandler_login?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)



            val urlBuilder = "http://luckyfriends.kro.kr/post/${postedID}/unlike/".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()

            val request =  Request.Builder()
                .url(urlString)
                .delete()
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())

                    val RcCode = response.code.toString()
                    Log.d("삭제_서버응답", jsonObj.toString())
                    Log.d("삭제_RcCode", RcCode.toString())

                    handler?.onResponse(jsonObj,RcCode)
                }
            })
        }



        fun deleteAnnounce(context: Context, handler: JsonResponseHandler_del?){
            val postedID = Context_okhttp.getPostId(context)
            Log.d("유알엘", postedID)
            val urlBuilder = "http://luckyfriends.kro.kr/post/${postedID}/".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()

            val request =  Request.Builder()
                .url(urlString)
                .delete()
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {


                    val RcCode = response.code
                    handler?.onResponse(RcCode.toString())
                }
            })
        }


        fun getUserDetail(context:Context, handler: JsonResponseHandler_login?){

            val urlBuilder = "http://luckyfriends.kro.kr/rest-auth/user/".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리파라미터를 담을게 없다. 바로 build()로 마무리

            var urlString = urlBuilder.toString()

            val request =  Request.Builder()
                .url(urlString)
                .get()
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")  // ContextUtil를 통해, 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {


                    val jsonObj = JSONObject(response.body!!.string())
                    val rcCode = response.code.toString()



                    Log.d("유저가져와", jsonObj.toString())
                    Log.d("유저가져와", rcCode)
                    handler?.onResponse(jsonObj,rcCode)
                }
            })
        }


        fun postTokenRefresh(token:String, handler:JsonResponseHandler_login?){

            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/rest-auth/refresh/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("token", token)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .post(formData)

                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    val bodyString =  response.body!!.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용

                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)
                    val RTcode = jsonObj.names().toString()
                    val rc = response.code

                    Log.d("토큰 리프레시",jsonObj.toString())
                    Log.d("토큰",RTcode.toString())
                    Log.d("토큰",rc.toString())

                    // 실제 handler변수에,  jsonObj를 가지고 화면에서 어떻게 처리할지 계획이 들어와있다
                    // (계획이  되어있을때만) 해당 계획을 실행하자
                    handler?.onResponse(jsonObj,RTcode)

                }
            })


        }

        fun postRequestTopicReply(context: Context, topicId: Int, content:String, handler:JsonResponseHandler?){

            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "${BASE_URL}/topic_reply"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("topic_id", topicId.toString())
                .add("content", content)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("X-Http-Token", Context_okhttp.getToken(context))
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    val bodyString =  response.body!!.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용

                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)

                    Log.d("서버테스트",jsonObj.toString())

                    // 실제 handler변수에,  jsonObj를 가지고 화면에서 어떻게 처리할지 계획이 들어와있다
                    // (계획이  되어있을때만) 해당 계획을 실행하자
                    handler?.onResponse(jsonObj)

                }
            })


        }


        fun postAnnounceBoard(context: Context, title:String, content:String, handler:JsonResponseHandler_login?){

            // Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

            // 제작 1) 어느 주소(url) 로 접근할지? => 서버주소 + 기능주소
            val urlString = "http://luckyfriends.kro.kr/post/"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("title", title)
                .add("content", content)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성 (어느주소 + 어느 메소드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("Authorization", "JWT ${Context_okhttp.getToken(context)}")
                .build()

            // 종합한 Request도 실제 호출을 해줘야 API 호출이 실행됨 (startActivity 같은 동작 필요함)
            // 실제 호출: 클라이언트로써 동작 -> OkHttpClient 클래스
            val client = OkHttpClient()

            // OkHttpClient 객체를 이용-> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결과를 받아서 처리
            // => 서버에 다녀와서 할일을 등록: enqueue(  Callback  )
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 실패: 서버에 연결 자체를 실패. 응답이 오지 않았다.
                    // ex) 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex) 비번 틑려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 -> 그 내용만 실패(물리적 실패X)
                }

                override fun onResponse(call: Call, response: Response) {
                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우 (그 내용은 성공/실패 일 수 있다)
                    // 응답: response 변수 -> 응답의 본문(body)만 보자
                    val code = response.code
                    Log.d("응답", code.toString())
                    val bodyString =  response.body!!.string() // toString()고르면 안됨!, string()기능은 1회용. 변수에 담아두고 이용

                    // 응답의 본문을 String으로 변환하면, JSON Encoding 적용된 상태(한글 깨짐)
                    // JSONObject 객체로 응답본문String을 변환해주면, 한글이 복구됨
                    // => UI에서도 JSONObject 이용해서, 데이터 추출 / 실제 활용
                    val jsonObj = JSONObject(bodyString)
                    val RtCode = jsonObj.names().toString()

                    Log.d("응답.내용",jsonObj.toString())
                    Log.d("응답.RTCode",RtCode.toString())

                    // 실제 handler변수에,  jsonObj를 가지고 화면에서 어떻게 처리할지 계획이 들어와있다
                    // (계획이  되어있을때만) 해당 계획을 실행하자
                    handler?.onResponse(jsonObj,RtCode)

                }
            })


        }
    }
}