package com.beyond.project_toy_revert.board

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.ActivityBoardWriteBinding
import com.beyond.project_toy_revert.datas.PostData
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.beyond.project_toy_revert.util.URIPathHelper
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class BoardWriteActivity : BasicActivity() {


    private var imgClicked = false
    private var imgClickedInt = 1
    private var imgStyle = "none"
    //1 -> 초기값(imgClicked == false), false 2 -> img 1clicked(imgClicked == true) , 3-> img 2Clicked(imgClicked == false)
    val CAMERA_CODE = 98
    val intentActionPick = 100
    val VIDEOFILE_REQUEST = 120
    val imgUrlList = mutableListOf<Uri>()
    val images = ArrayList<MultipartBody.Part>()

    private lateinit var binding : ActivityBoardWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)


        binding.imgBwriteCam.setOnClickListener {
            if(imgClickedInt==1){
                dialogChoicePictureType()
            }
            if(imgClickedInt==2){
                dialogImgDel()
            }

            Log.d("이미지여부 in imgBwriteCam.setOnClickListener",imgClicked.toString()+imgClickedInt.toString())
        }//imgBwriteCam.setOnClickListener
        binding.btnBwritePush.setOnClickListener {

            val bwTitle = binding.edtBwriteTitle.text.toString()
            val bwTilteSpaceCheck = if(bwTitle == "") "공란입니다" else bwTitle
            val bwContent = binding.edtBwriteContent.text.toString()
            val bwContentSpaceCheck = if(bwContent == "") "공란입니다" else bwContent

            val selectedOneImageUri = Context_okhttp.getUri(mContext).toUri()

            Log.d("이미지_zhx", selectedOneImageUri.toString())
            Log.d("이미지_zhx", imgUrlList.size.toString())
            Log.d("이미지_zhx", imgUrlList.toString())


            if(imgUrlList.size !=null)
            for(i in 0 .. imgUrlList.size-1){

                val file = File(URIPathHelper().getPath(mContext, imgUrlList[i]))
                val fileReqBody = file.asRequestBody("image/*".toMediaType())
                val fileNameChoicer = "${Context_okhttp.getID(mContext)}.${RandomFileName()}.${i}"
                val multiPartBody = MultipartBody.Part.createFormData("image", "${fileNameChoicer}", fileReqBody)
                images.add(multiPartBody)
            }
//            val file = File(URIPathHelper().getPath(mContext, selectedOneImageUri))
//
//            // 완성된 파일을, Retrofit에 첨부가능한 RequestBody 형태로 가공
//            val fileReqBody = file.asRequestBody("image/*".toMediaType())
//
//            // 실제로 첨부하자. 일반형태의 통신x  , Multipart 형태로 전송. MultipartBody 형태로 2차가공
//            // cf) 파일이 같이 첨부되는 API통신은, Multipart 형태로 모든 데이터를 첨부해야함
//            val fileNameChoicer = "${Context_okhttp.getID(mContext)}.${RandomFileName()}"
//            val multiPartBody = MultipartBody.Part.createFormData("image", "${fileNameChoicer}", fileReqBody)
//
//
//            images.add(multiPartBody)
            Log.d("이미지",images.toString())
            apiList.postRequestWrite(
                bwTilteSpaceCheck,
                bwContentSpaceCheck,
                images,
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

            Context_okhttp.setUri(mContext,"")

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
    fun dialogChoicePictureType(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_cammer_picker, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("선택")
        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.btn_dialog_cammera_img)?.setOnClickListener {
            choicePictureWithCam()
            alertDialog.dismiss()
        }

        alertDialog.findViewById<Button>(R.id.btn_dialog_cammera_vidio)?.setOnClickListener {

            val galleryV = Intent(Intent.ACTION_VIEW, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
            galleryV.setType("video/*")
            galleryV.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(galleryV, VIDEOFILE_REQUEST);


        }

        alertDialog.findViewById<Button>(R.id.btn_dialog_gallery)?.setOnClickListener{
            choicePictureWithGallery()
            alertDialog.dismiss()

        }
    }
    fun choicePictureWithCam(){
        val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(itt, CAMERA_CODE)
        imgClicked = true
        imgClickedInt =2
        Log.d("이미지여부 in choicePictureWithCam",imgClicked.toString()+imgClickedInt.toString())


    }
    fun dialogImgDel(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_login_intro, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("선택")
        val alertDialog = mBuilder.show()
        alertDialog.findViewById<TextView>(R.id.txt_dialog_topbar)?.text = "이미지를 초기화하시겠습니까"
        alertDialog.findViewById<Button>(R.id.btn_dialog_loginYes)?.setOnClickListener {
            Glide.with(mContext).load(R.drawable.camera_icon).into(binding.imgBwriteCam)
            imgStyle = "none"
            imgClicked = false
            imgClickedInt =1
            Log.d("이미지여부 in dialogImgDel",imgClicked.toString()+imgClickedInt.toString()+imgStyle)

            Context_okhttp.setUri(mContext,"")
            alertDialog.dismiss()
        }
        alertDialog.findViewById<Button>(R.id.btn_dialog_loginNo)?.setOnClickListener{
            alertDialog.dismiss()
        }
    }


    fun choicePictureWithGallery(){
        Context_okhttp.setUri(mContext, "")
        Log.d("강산", Context_okhttp.getUri(mContext))
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        gallery.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(gallery, intentActionPick)
        imgClicked = true
        imgClickedInt =2
        imgStyle="Img-notCounted"
        Log.d("이미지여부 in choicePictureWithGallery",imgClicked.toString()+", "+imgClickedInt.toString()+", "+imgStyle)

    }

    fun RandomFileName() : String
    {
        val fineName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fineName
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == intentActionPick){
            //갤러리에서 선택
            if(data?.clipData != null){
                //여러장 선택시
                val imgCount = data.clipData!!.itemCount
                if(imgCount>10){
                    Toast.makeText(mContext, "이미지는 10장까지 가능해요", Toast.LENGTH_SHORT).show()
                    return
                    Log.d("이미지여부 in onActivityResult - 다중픽 실패",imgClicked.toString()+imgClickedInt.toString())
                    // 10장 넘기면 거부
                }//if(imgCount>10) 리턴 블록문
                for(i in 0 until imgCount){
                    val imgUri = data.clipData!!.getItemAt(i).uri
                    imgUrlList.add(imgUri)
                }
                binding.imgBshowMultiImgChecker.isVisible = true
                imgStyle = "multipleImg"
                Glide.with(mContext).load(data.clipData!!.getItemAt(0).uri).into(binding.imgBwriteCam)
                Log.d("이미지여부 in onActivityResult - 다중픽 성공",imgClicked.toString()+", "+imgClickedInt.toString()+", "+imgStyle)

            }//if(data?.clipData != null)

            else{
                //한 장 선택시
                binding.imgBshowMultiImgChecker.isVisible = false
                val img_Bwrite_cam = findViewById<ImageView>(R.id.img_Bwrite_cam)
                Glide.with(mContext).load(data?.data).into(img_Bwrite_cam)
                imgUrlList.add(data?.data!!)
                imgStyle="singleImg"
                imgClicked = true
                imgClickedInt =2
                Log.d("이미지여부 in onActivityResult - 단일픽 성공",imgClicked.toString()+imgClickedInt.toString()+imgStyle)

                Context_okhttp.setUri(mContext, data?.data.toString())
                Log.d("강산", Context_okhttp.getUri(mContext))
            }//else
        }// if(resultCode == RESULT_OK && requestCode == intentActionPick)
        if(resultCode == RESULT_OK && requestCode == CAMERA_CODE){
            //카메라 픽
            if (data?.extras?.get("data") != null) {
                val img = data?.extras?.get("data") as Bitmap
                val uri = saveFile(RandomFileName(), "image/jpeg", img)
                val kt = uri.toString()
                if (uri != null) {
                    imgUrlList.add(uri)
                    Log.d("이미지_카메라 리스트", imgUrlList.toString())
                }
                Context_okhttp.setUri(mContext, kt)
                Log.d("도모", kt)
                imgClicked = true
                imgClickedInt =2
                binding.imgBwriteCam.setImageURI(uri)
            }
        }


    }

    fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri?// mime타입: 전송되는 파일의 타입을 알려줌
    {
        var CV = ContentValues()
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)

        if (uri != null) {
            var scriptor = contentResolver.openFileDescriptor(uri, "w")

            if (scriptor != null) {
                val fos = FileOutputStream(scriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CV.clear()
                    CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, CV, null, null)
                }
            }
        }

        return uri;
    }


    override fun onBackPressed() {
        val bwintent = Intent(mContext, MainActivity::class.java)

        startActivity(bwintent)
    }
}