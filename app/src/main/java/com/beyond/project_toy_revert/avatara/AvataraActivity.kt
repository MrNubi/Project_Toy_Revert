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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.adapter.AvataraShowWindowAdapter
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.board.BoardWriteActivity
import com.beyond.project_toy_revert.databinding.ActivityAvataraBinding
import com.beyond.project_toy_revert.datas.AvataraModel
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.io.FileOutputStream

class AvataraActivity : BasicActivity() {
    private lateinit var binding : ActivityAvataraBinding
    private var avataraShowList = mutableListOf<AvataraModel>()
    private lateinit var avataraShowAdapter : AvataraShowWindowAdapter
    private var tabNumber = 1
    private var HeadNumber = 0
    private var BodyNumber = 0
    private var ShoesNumber = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_avatara)



        binding.avataraTab1.setOnClickListener {
            binding.LinAvataraRvShower.visibility=View.VISIBLE
            binding.linearAvataraTabChoiceShower2.visibility = View.GONE
            binding.linearAvataraTabChoiceShower3.visibility = View.GONE
            binding.linearAvataraTabChoiceShower1.visibility = View.VISIBLE
            tabNumber=1
            avataraHatShow()
        }
        binding.avataraTab2.setOnClickListener {
            binding.LinAvataraRvShower.visibility=View.VISIBLE
            binding.linearAvataraTabChoiceShower2.visibility = View.VISIBLE
            binding.linearAvataraTabChoiceShower3.visibility = View.GONE
            binding.linearAvataraTabChoiceShower1.visibility = View.GONE
            tabNumber=2
            avataraBodyShow()
        }
        binding.avataraTab3.setOnClickListener {
            binding.LinAvataraRvShower.visibility=View.VISIBLE
            binding.linearAvataraTabChoiceShower2.visibility = View.GONE
            binding.linearAvataraTabChoiceShower3.visibility = View.VISIBLE
            binding.linearAvataraTabChoiceShower1.visibility = View.GONE
            tabNumber=3
            avataraShoesShow()
        }
        binding.buttonAvataraChoice.setOnClickListener {
           val avataOk= Context_okhttp.getAvataOk(mContext)
            saveProfileToURL()
            if(avataOk=="0"){
                postProfile()
            }
            else if(avataOk=="1"){
                patchProfile()
            }
            else{
                Toast.makeText(mContext, "오류: 아바타를 전송할 수 없습니다", Toast.LENGTH_SHORT).show()
                Log.d("아바타에러","아바타ok체크_${avataOk}")
            }
            val I = Intent(mContext, MainActivity::class.java)
            startActivity(I)

            //+ Save , 서버에 데이터 전송, 이걸로 뭘 할 수 있을지
            //앞으로는 밑에 리사이클러 뷰 박아서 그거 클릭 이벤트로 처리해야할듯
        }
    }



    fun saveProfileToURL(){
        val img = viewToBitmap(binding.layoutAvatara)
        var resultUri=saveFile(RandomFileName(), "image/jpeg", img)
        Context_okhttp.setAvatara(mContext, resultUri.toString())
        Log.d("아바타 유알엘 저장", Context_okhttp.getAvatara(mContext))



    }
    fun postProfile(){
        Log.d("머리가슴신발","${HeadNumber},${BodyNumber},${ShoesNumber}")
        serverUtil_okhttp.postProfile(mContext,HeadNumber.toString(),BodyNumber.toString(),ShoesNumber.toString(),object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode=="201"){
                    Toast.makeText(mContext, "성공:${RcCode.toString()}", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    runOnUiThread{
                        Toast.makeText(mContext, "에러:${RcCode.toString()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }
    fun patchProfile(){
        Log.d("머리가슴신발","${HeadNumber},${BodyNumber},${ShoesNumber}")
        serverUtil_okhttp.patchProfile(mContext,HeadNumber.toString(),BodyNumber.toString(),ShoesNumber.toString(),object :serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                if(RcCode=="200"){
                    Toast.makeText(mContext, "성공:${RcCode.toString()}", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    runOnUiThread{
                        Toast.makeText(mContext, "에러:${RcCode.toString()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
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


    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

    fun avataraHatShow(){
        avataraShowList = mutableListOf<AvataraModel>()
        for(i in 0..2){
            var k = if(i==0)R.drawable.samp_clear else if(i==1)R.drawable.samp_hat1 else R.drawable.samp_hat2
            var hatData : AvataraModel = AvataraModel(
                i,
                k,
                "Hat${i}"
            )
            avataraShowList.add(hatData)
            Log.d("리스토",avataraShowList.toString())
        }

        avataraShowAdapter = AvataraShowWindowAdapter(avataraShowList)
        avataraShowAdapter.notifyDataSetChanged()
        binding.avataraShowWindow.adapter = avataraShowAdapter
        binding.avataraShowWindow.layoutManager = LinearLayoutManager(mContext).also{it.orientation = LinearLayoutManager.HORIZONTAL}
        avataraShowAdapter.setItemClickListener(object : AvataraShowWindowAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {

                Glide.with(mContext).load(avataraShowList[position].avataraPicture).into(binding.imgAvataraHead)
                HeadNumber = position
            }
        })
    }

    fun avataraBodyShow(){
        avataraShowList = mutableListOf<AvataraModel>()
        for(i in 1..2){
            var k = if(i==1)R.drawable.samp_human1 else R.drawable.samp_human2
            var hatData : AvataraModel = AvataraModel(
                i,
                k,
                "Body${i}"
            )
            avataraShowList.add(hatData)
            Log.d("리스토",avataraShowList.toString())
        }

        avataraShowAdapter = AvataraShowWindowAdapter(avataraShowList)
        avataraShowAdapter.notifyDataSetChanged()
        binding.avataraShowWindow.adapter = avataraShowAdapter
        binding.avataraShowWindow.layoutManager = LinearLayoutManager(mContext).also{it.orientation = LinearLayoutManager.HORIZONTAL}
        avataraShowAdapter.setItemClickListener(object : AvataraShowWindowAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {

                Glide.with(mContext).load(avataraShowList[position].avataraPicture).into(binding.imgAvataraBody)
                BodyNumber = position
            }
        })
    }
    fun avataraShoesShow(){
        avataraShowList = mutableListOf<AvataraModel>()
        for(i in 0..2){
            var k = if(i==0)R.drawable.samp_clear else if(i==1)R.drawable.samp_shoe1_left else R.drawable.samp_shoe2_left
            var hatData : AvataraModel = AvataraModel(
                i,
                k,
                "Shoes${i}"
            )
            avataraShowList.add(hatData)
            Log.d("리스토",avataraShowList.toString())
        }

        avataraShowAdapter = AvataraShowWindowAdapter(avataraShowList)
        avataraShowAdapter.notifyDataSetChanged()
        binding.avataraShowWindow.adapter = avataraShowAdapter
        binding.avataraShowWindow.layoutManager = LinearLayoutManager(mContext).also{it.orientation = LinearLayoutManager.HORIZONTAL}

        avataraShowAdapter.setItemClickListener(object : AvataraShowWindowAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                if(position==0){
                    Glide.with(mContext).load(R.drawable.samp_clear).into(binding.imgAvataraLeftFoot)
                    Glide.with(mContext).load(R.drawable.samp_clear).into(binding.imgAvataraRightFoot)

                }
                if(position==1){
                    Glide.with(mContext).load(R.drawable.samp_shoe1_left).into(binding.imgAvataraLeftFoot)
                    Glide.with(mContext).load(R.drawable.samp_shoe1_right).into(binding.imgAvataraRightFoot)

                }
                if(position==2){
                    Glide.with(mContext).load(R.drawable.samp_shoe2_left).into(binding.imgAvataraLeftFoot)
                    Glide.with(mContext).load(R.drawable.samp_shoe2_right).into(binding.imgAvataraRightFoot)

                }
                ShoesNumber=position
            }
        })
    }






}