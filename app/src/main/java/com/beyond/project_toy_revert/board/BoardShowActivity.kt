package com.beyond.project_toy_revert.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.ActivityBoardShowBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity
import com.beyond.project_toy_revert.util.Context_okhttp
import org.json.JSONObject

class BoardShowActivity : BasicActivity() {
    private lateinit var binding: ActivityBoardShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_board_show)

        setText()
        isBtnShow(setText())
        ClickListenerSetter()




    }
    fun setText():String{
        val AnnounceContent = intent.getStringExtra("AnnounceContent").toString()
        val AnnounceTitle = intent.getStringExtra("AnnounceTitle").toString()
        val AnnounceNickname = intent.getStringExtra("AnnounceNickname").toString()
        val AnnounceAuthor = intent.getStringExtra("AnnounceAuthor").toString()
        val AnnounceId = intent.getStringExtra("AnnounceId").toString()
        Log.d("쿨톤", AnnounceContent )
        Log.d("쿨톤", AnnounceTitle )
        Log.d("쿨톤", AnnounceNickname )
        Log.d("쿨톤", AnnounceAuthor )
        Log.d("쿨톤", AnnounceId )

        binding.txtBshowTitle.text=AnnounceTitle
        binding.txtBshowContent.text=AnnounceContent
        binding.txtBshowTime.text=AnnounceNickname

        return AnnounceNickname
    }//setText()
    fun isBtnShow(nick:String){
        serverUtil_okhttp.getUserDetail(mContext, object : serverUtil_okhttp.JsonResponseHandler_login{
            override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                Log.d("유저확인", RcCode)
                if(RcCode == "200"){
                    val id = jsonObject.getString("username")
                    Log.d("유저", id)
                    if(id == nick){
                        binding.imgBshowEditOrDel.isVisible == true
                    }
                    else{
                        binding.imgBshowEditOrDel.isVisible == false
                    }

                }
            }
        })
    }//fun isBtnShow

    fun ClickListenerSetter(){
        binding.imgBshowEditOrDel.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_or_del, null)
            val mBuilder = AlertDialog.Builder(mContext)
                .setView(mDialogView)
                .setTitle("수정 / 삭제")

            val alertDialog = mBuilder.show()
            alertDialog.findViewById<Button>(R.id.btn_dialog_edit)?.setOnClickListener {
                startActivity(Intent(mContext, BoardEditActivity::class.java))
                finish()

            }

            alertDialog.findViewById<Button>(R.id.btn_dialog_del)?.setOnClickListener {




            }
        }
    }
}