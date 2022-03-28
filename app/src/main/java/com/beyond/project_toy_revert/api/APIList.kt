package com.beyond.project_toy_revert.api

import om.beyond.project_toy_revert.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    // BASE_URL 에 해당하는 서버에서, 어떤 기능들을 사용할건지 명시

    @FormUrlEncoded // 파라미터중에, Field(formData)에 담아야하는 파라미터가 있다면
    @POST("/rest-auth/registration/")
    fun postRequestLogin(
        @Field("username") userName:String,
        @Field("email") email:String,
        @Field("password") pw:String
    ) :Call<BasicResponse>  // 서버가 주는 응답을 (성공시에)  BasicResponse 형태로 자동 파싱


    @FormUrlEncoded
    @PUT("/registration/")
    fun putRequestSignUp(
        @Field("username") userName:String,
        @Field("email") email:String,
        @Field("password1") pw1:String,
        @Field("password2") pw2:String,
    ) :Call<BasicResponse>


//    @GET("/user")
//    fun getRequestMyInfo() :  Call<BasicResponse>
//
//
//    @GET("/user/check")
//    fun getRequestDuplicatedCheck(
//        @Query("type") type:String,
//        @Query("value") value:String
//    ) : Call<BasicResponse>
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