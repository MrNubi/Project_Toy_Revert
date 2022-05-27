package com.beyond.project_toy_revert.avatara

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.board.BoardWriteActivity
import com.beyond.project_toy_revert.databinding.ActivityAvataraBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import java.io.FileOutputStream

class AvataraActivity : BasicActivity() {
    private lateinit var binding : ActivityAvataraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_avatara)

        binding.buttonAvataraHead.setOnClickListener {
            dialogAvataraChoice1()
        }
        binding.buttonAvataraBody.setOnClickListener {
            dialogAvataraChoice2()
        }

        binding.buttonAvataraFoot.setOnClickListener {
            dialogAvataraChoice3()
        }

        binding.buttonAvataraChoice.setOnClickListener {
            val img = viewToBitmap(binding.layoutAvatara)
            var resultUri=saveFile(RandomFileName(), "image/jpeg", img)
            Context_okhttp.setAvatara(mContext, resultUri.toString())
            Log.d("아바타 유알엘 저장", Context_okhttp.getAvatara(mContext))
            val I = Intent(mContext, MainActivity::class.java)
            startActivity(I)

            //+ Save , 서버에 데이터 전송, 이걸로 뭘 할 수 있을지
            //앞으로는 밑에 리사이클러 뷰 박아서 그거 클릭 이벤트로 처리해야할듯
        }
    }

    fun RandomFileName() : String
    {
        val fineName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fineName
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

    fun dialogAvataraChoice1(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_avatara_choicer, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("아바타 선택")
        val alertDialog = mBuilder.show()

        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice_1)?.setOnClickListener {
            Glide.with(mContext).load(R.drawable.samp_hat1).into(binding.imgAvataraHead)
            alertDialog.dismiss()
        }//avataraChoice_1)?.setOnClickListener
        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice_2)?.setOnClickListener {

            Glide.with(mContext).load(R.drawable.samp_hat2).into(binding.imgAvataraHead)
            alertDialog.dismiss()
        }
        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice_back)?.setOnClickListener {
            Glide.with(mContext).load(R.drawable.samp_clear).into(binding.imgAvataraHead)
            alertDialog.dismiss()
        }

    }
    fun dialogAvataraChoice2(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_avatara_choicer2, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("아바타 선택")
        val alertDialog = mBuilder.show()

        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice2_1)?.setOnClickListener {

            Glide.with(mContext).load(R.drawable.samp_human2).into(binding.imgAvataraBody)
            alertDialog.dismiss()
        }//avataraChoice_1)?.setOnClickListener
        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice2_2)?.setOnClickListener {

            Glide.with(mContext).load(R.drawable.samp_human1).into(binding.imgAvataraBody)
            alertDialog.dismiss()
        }
        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice_back)?.setOnClickListener {
            Glide.with(mContext).load(R.drawable.samp_human2).into(binding.imgAvataraBody)

            alertDialog.dismiss()
        }

    }
    fun dialogAvataraChoice3(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_avatara_choicer3, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("아바타 선택")
        val alertDialog = mBuilder.show()

        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice3_1)?.setOnClickListener {

            Glide.with(mContext).load(R.drawable.samp_shoe1_left).into(binding.imgAvataraLeftFoot)
            Glide.with(mContext).load(R.drawable.samp_shoe1_right).into(binding.imgAvataraRightFoot)
            alertDialog.dismiss()
        }//avataraChoice_1)?.setOnClickListener
        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice23_2)?.setOnClickListener {

            Glide.with(mContext).load(R.drawable.samp_shoe2_left).into(binding.imgAvataraLeftFoot)
            Glide.with(mContext).load(R.drawable.samp_shoe2_right).into(binding.imgAvataraRightFoot)
            alertDialog.dismiss()
        }
        alertDialog.findViewById<ImageView>(R.id.button_avataraChoice_back)?.setOnClickListener {
            Glide.with(mContext).load(R.drawable.samp_clear).into(binding.imgAvataraLeftFoot)
            Glide.with(mContext).load(R.drawable.samp_clear).into(binding.imgAvataraRightFoot)
            alertDialog.dismiss()
        }

    }
    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }




}