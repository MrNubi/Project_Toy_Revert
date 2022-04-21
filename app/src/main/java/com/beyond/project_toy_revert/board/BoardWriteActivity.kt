package com.beyond.project_toy_revert.board

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.APIList
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.ActivityBoardWriteBinding
import com.beyond.project_toy_revert.datas.PostData
import com.beyond.project_toy_revert.fragment.AnnounceFragment
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.beyond.project_toy_revert.util.URIPathHelper
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BoardWriteActivity : BasicActivity() {


    private var imgClicked = false
    private lateinit var binding : ActivityBoardWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)


        binding.imgBwriteCam.setOnClickListener {
            Context_okhttp.setUri(mContext, "")
            Log.d("강산", Context_okhttp.getUri(mContext))
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            imgClicked = true
        }
        binding.btnBwritePush.setOnClickListener {

            var bwTitle = binding.edtBwriteTitle.text.toString()
            var bwTilteSpaceCheck = if(bwTitle == "") "공란입니다" else bwTitle
            var bwContent = binding.edtBwriteContent.text.toString()
            var bwContentSpaceCheck = if(bwContent == "") "공란입니다" else bwContent

            var selectedImageUri = Context_okhttp.getUri(mContext).toUri()
            Log.d("이미지", selectedImageUri.toString())
            val file = File(URIPathHelper().getPath(mContext, selectedImageUri))

            // 완성된 파일을, Retrofit에 첨부가능한 RequestBody 형태로 가공
            val fileReqBody = RequestBody.create("image/*".toMediaType(), file)

            // 실제로 첨부하자. 일반형태의 통신x  , Multipart 형태로 전송. MultipartBody 형태로 2차가공
            // cf) 파일이 같이 첨부되는 API통신은, Multipart 형태로 모든 데이터를 첨부해야함
            val multiPartBody = MultipartBody.Part.createFormData("image", "myProfile.jpg", fileReqBody)
            var images = ArrayList<MultipartBody.Part>()

            images.add(multiPartBody)
            Log.d("이미지",images.toString())
            apiList.postRequestWrite(
                bwTilteSpaceCheck,
                bwContentSpaceCheck,
                multiPartBody,
                "#하이"
            ).enqueue(object : Callback<PostData>{
                override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                    Log.d("이미지", response.toString())
                    if(response.isSuccessful){
                        runOnUiThread{
                        Log.d("이미지 성공","성공")
                        Toast.makeText(mContext, "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
                                val bwintent = Intent(mContext, MainActivity::class.java)

                                startActivity(bwintent)
                        }

                }
                    else{
                        runOnUiThread {
                            Toast.makeText(mContext, "게시글이 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()

                            Log.d("이미지 성공", "실패")
                        }
                    }
                }

                override fun onFailure(call: Call<PostData>, t: Throwable) {
                    runOnUiThread {
                        Toast.makeText(mContext, "게시글이 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()

                        Log.d("이미지 실패","연결조차 실패")
                    }

                }
            })

//            serverUtil_okhttp.postAnnounceBoard(
//                mContext,
//                bwTilteSpaceCheck,
//                bwContentSpaceCheck,
//                object : serverUtil_okhttp.JsonResponseHandler_login {
//                    override fun onResponse(jsonObject: JSONObject, RcCode: String) {
//                        Log.d("클라라", jsonObject.toString())
//                        Log.d("클라라", RcCode)
//                        if(RcCode == "[\"id\",\"author\",\"nickname\",\"title\",\"content\"]"){
//                            runOnUiThread {
//                                Toast.makeText(mContext, "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
//                                val bwintent = Intent(mContext, MainActivity::class.java)
//
//                                startActivity(bwintent)
//
//                            }
//                        }
//
//
//
//                    }//onResponse
//                })//serverUtil_okhttp.postAnnounceBoard


        }//binding.btnBwritePush.setOnClickListener
    }//Oncreate
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 100){
            val img_Bwrite_cam = findViewById<ImageView>(R.id.img_Bwrite_cam)
            Glide.with(mContext).load(data?.data).into(img_Bwrite_cam)



            Context_okhttp.setUri(mContext, data?.data.toString())
            Log.d("강산", Context_okhttp.getUri(mContext))

        }

    }
    override fun onBackPressed() {
        val bwintent = Intent(mContext, MainActivity::class.java)

        startActivity(bwintent)
    }
}