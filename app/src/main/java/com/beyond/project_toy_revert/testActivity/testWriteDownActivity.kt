package com.beyond.project_toy_revert.testActivity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.Dimension
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.ActivityTestWriteDownBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import okhttp3.MultipartBody
import java.io.FileOutputStream

class testWriteDownActivity : BasicActivity() {
    private lateinit var binding: ActivityTestWriteDownBinding
    private var imgClicked = false
    private var imgClickedInt = 1
    private var hightReturn = 0
    private var clicker =0
    private var imgStyle = "none"
    //1 -> 초기값(imgClicked == false), false 2 -> img 1clicked(imgClicked == true) , 3-> img 2Clicked(imgClicked == false)
    val CAMERA_CODE = 98
    var textColor = 0
    val intentActionPick = 100
    val VIDEOFILE_REQUEST = 120
    val imgUrlList = mutableListOf<Uri>()
    val VdoUrlList = mutableListOf<Uri>()
    val images = ArrayList<MultipartBody.Part>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_write_down)

        binding.btnAddtextView.setOnClickListener {
            setTextDialog()

        }
        binding.btnAddImageView.setOnClickListener {
            dialogChoicePictureType()

        }
        binding.btnFinishBtn.setOnClickListener {

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun createTextView(text:String,color:Int): View {
        val newText = TextView(mContext)
        newText.text = text
        newText.setBackgroundResource(R.drawable.background)
        newText.setPadding(20)
        when(color){
            0->newText.setTextColor(getResources().getColor(R.color.white))
            1->newText.setTextColor(getResources().getColor(R.color.purple_200))
            2->newText.setTextColor(getResources().getColor(R.color.purple_500))
            3->newText.setTextColor(getResources().getColor(R.color.Yellow))
            4->newText.setTextColor(getResources().getColor(R.color.red))
            5->newText.setTextColor(getResources().getColor(R.color.black))
            else -> {
                Log.d("색상픽 오류", "0~5 바깥")
                newText.setTextColor(getResources().getColor(R.color.white))
            }
        }
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,)
        newText.layoutParams = lp
        newText.setTextSize(30.0f)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        lp.topMargin = 20
        newText.setOnClickListener {
            Toast.makeText(mContext, "${newText.text}", Toast.LENGTH_SHORT).show()
            Log.d("newText","클릭")
        }
        var moveX = 0f
        var moveY = 0f

        newText.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveX = v.x - event.rawX
                    moveY = v.y - event.rawY
                    Log.d("newText_moveX",moveX.toString())
                    Log.d("newText_moveY",moveY.toString())



                }

                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + moveX)
                        .y(event.rawY + moveY)
                        .setDuration(0)
                        .start()

                    Log.d("newText_animateX", (event.rawX + moveX).toString())
                    Log.d("newText_animateY", (event.rawX + moveX).toString())
                }
            }

            true
        }

        newText.id = ViewCompat.generateViewId()
        Log.d("newText_id",newText.id.toString())

        hightReturn = newText.height
        return newText
    }

    fun imgSetting(uri:String):View{
        val newImage = ImageView(mContext)

        val lp = LinearLayout.LayoutParams(
            150,
            150,
        )
        lp.gravity = Gravity.CENTER_HORIZONTAL
//        lp.setMargins(80,5,0,0)
        lp.topMargin = 20

        newImage.layoutParams = lp
//        newImage.setOnClickListener {
//            clicker++
//            var curWidth = newImage.width
//            val originWidth = 150
//            var curHeight = newImage.height
//            val originHeight = 150
//            if(clicker<3){
//
//                newImage.layoutParams.width = curWidth + 100
//                newImage.layoutParams.height = curHeight + 100
//                newImage.requestLayout()
//            }
//            else if(clicker>=3){
//                clicker = 0
//                newImage.layoutParams.width = originWidth
//                newImage.layoutParams.height = originHeight
//                newImage.requestLayout()
//            }
//
//            Toast.makeText(mContext, "이미지가 생성되었습니다", Toast.LENGTH_SHORT).show()
//            Log.d("newText","클릭")
//        }
        var moveX = 0f
        var moveY = 0f

        newImage.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveX = v.x - event.rawX
                    moveY = v.y - event.rawY
                    Log.d("newText_moveX",moveX.toString())
                    Log.d("newText_moveY",moveY.toString())



                }

                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + moveX)
                        .y(event.rawY + moveY)
                        .setDuration(0)
                        .start()

                    Log.d("newText_animateX", (event.rawX + moveX).toString())
                    Log.d("newText_animateY", (event.rawX + moveX).toString())
                }
            }

            true
        }

        newImage.id = ViewCompat.generateViewId()
        Log.d("newText_id",newImage.id.toString())

        Glide.with(mContext).load(uri).into(newImage)
        return newImage
    }

    fun setTextDialog(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edt, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("택스트를 입력해주세요")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<LinearLayout>(R.id.Lin_dialogColorBar)?.visibility=View.VISIBLE
        alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.hint = "입력"

        alertDialog.findViewById<ImageView>(R.id.img_dialogColor1)?.setOnClickListener{
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setTextColor(getResources().getColor(R.color.purple_200))
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setHintTextColor(getResources().getColor(R.color.purple_200))
            textColor = 1
            Log.d("sampleWrite_textColor", textColor.toString())
        }
        alertDialog.findViewById<ImageView>(R.id.img_dialogColor2)?.setOnClickListener{
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setTextColor(getResources().getColor(R.color.purple_500))
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setHintTextColor(getResources().getColor(R.color.purple_500))
            textColor = 2
            Log.d("sampleWrite_textColor", textColor.toString())
        }
        alertDialog.findViewById<ImageView>(R.id.img_dialogColor3)?.setOnClickListener{
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setTextColor(getResources().getColor(R.color.Yellow))
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setHintTextColor(getResources().getColor(R.color.Yellow))
            textColor = 3
            Log.d("sampleWrite_textColor", textColor.toString())
        }
        alertDialog.findViewById<ImageView>(R.id.img_dialogColor4)?.setOnClickListener{
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setTextColor(getResources().getColor(R.color.red))
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setHintTextColor(getResources().getColor(R.color.red))
            textColor = 4
            Log.d("sampleWrite_textColor", textColor.toString())
        }
        alertDialog.findViewById<ImageView>(R.id.img_dialogColor5)?.setOnClickListener{
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setTextColor(getResources().getColor(R.color.black))
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setHintTextColor(getResources().getColor(R.color.black))
            textColor = 5
            Log.d("sampleWrite_textColor", textColor.toString())
        }
        alertDialog.findViewById<ImageView>(R.id.img_dialogColor6)?.setOnClickListener{
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setTextColor(getResources().getColor(R.color.white))
            alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.setHintTextColor(getResources().getColor(R.color.white))
            textColor = 0
            Log.d("sampleWrite_textColor", textColor.toString())
        }

        alertDialog.findViewById<ImageView>(R.id.btn_dialog_commentPush)?.setOnClickListener{
            val text =  alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.text.toString()
            binding.LinTestWriteParent.addView(createTextView(text,textColor))
            alertDialog.dismiss()
        }
    }


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

            val galleryV = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
            galleryV.setType("video/*")
            galleryV.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(galleryV, VIDEOFILE_REQUEST)
            Log.d("vidio", "rqtype")
            alertDialog.dismiss()



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



    fun choicePictureWithGallery(){
        Context_okhttp.setUri(mContext, "")
        Log.d("강산", Context_okhttp.getUri(mContext))
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        gallery.action = Intent.ACTION_PICK

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
        Log.d("결과",resultCode.toString()+", "+requestCode+", "+data.toString())
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
                imgStyle = "multipleImg"
                binding.LinTestWriteParent.addView(imgSetting(data.clipData!!.getItemAt(0).uri.toString()))

                Log.d("이미지여부 in onActivityResult - 다중픽 성공",imgClicked.toString()+", "+imgClickedInt.toString()+", "+imgStyle)

            }//if(data?.clipData != null)

            else{
                //한 장 선택시

                imgUrlList.add(data?.data!!)
                imgStyle="singleImg"
                imgClicked = true
                imgClickedInt =2
                Log.d("이미지여부 in onActivityResult - 단일픽 성공",imgClicked.toString()+imgClickedInt.toString()+imgStyle)

                Context_okhttp.setUri(mContext, data?.data.toString())
                binding.LinTestWriteParent.addView(imgSetting(data?.data.toString()))

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
                binding.LinTestWriteParent.addView(imgSetting(kt))
            }
        }// if(resultCode == RESULT_OK && requestCode == CAMERA_CODE)
        if(resultCode == RESULT_OK && requestCode == VIDEOFILE_REQUEST){


            val img_Bwrite_cam = findViewById<ImageView>(R.id.img_Bwrite_cam)
            // 비디오뷰 생성 후 걸어줘야됨

            imgStyle="video"
            imgClicked = true
            imgClickedInt =2
            Log.d("이미지여부 in onActivityResult - 비디오 성공",imgClicked.toString()+imgClickedInt.toString()+imgStyle)

            Context_okhttp.setVideoUri(mContext, data?.data.toString())
            Log.d("강산", Context_okhttp.getUri(mContext))
        }//if(resultCode == RESULT_OK && requestCode == VIDEOFILE_REQUEST)


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