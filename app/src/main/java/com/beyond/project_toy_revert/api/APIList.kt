package com.beyond.project_toy_revert.api

import com.beyond.project_toy_revert.datas.PostData
import com.beyond.project_toy_revert.util.Context_okhttp
import com.google.firebase.database.core.Context
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import om.beyond.project_toy_revert.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    // BASE_URL 에 해당하는 서버에서, 어떤 기능들을 사용할건지 명시


    @Multipart
    @POST("/post/")
    fun postRequestWrite(
        @Part("title") title:String,
        @Part("content") content:String,
        @Part image: MultipartBody.Part,
        @Part("tag_content") tag_content:String,
    ) :Call<PostData>  // 서버가 주는 응답을 (성공시에)  BasicResponse 형태로 자동 파싱




    @FormUrlEncoded// 파라미터중에, Field(formData)에 담아야하는 파라미터가 있다면
    @PUT("/registration/")
    fun putRequestSignUp(
        @Field("username") userName:String,
        @Field("email") email:String,
        @Field("password1") pw1:String,
        @Field("password2") pw2:String,
    ) :Call<BasicResponse>



//
//


//
//    @GET("/user/friend")
//    fun getRequestFriendList(
//        @Query("type") type:String,   // all, my, requested 세 문구외에는 넣지말자
//    ):Call<BasicResponse>
//
//
//    @GET("/search/user")
//    fun getRequestSearchUser(
//        @Query("nickname") nickname:String,
//    ):Call<BasicResponse>
//
//
//    @FormUrlEncoded
//    @POST("/user/friend")
//    fun postRequestAddFriend(
//        @Field("user_id") userId:Int
//    ):Call<BasicResponse>
//
//    @FormUrlEncoded
//    @PUT("/user/friend")
//    fun putFriendYesOrNO(
//        @Field("user_id") userId:Int,
//        @Field("type") type:String,
//    ):Call<BasicResponse>
}