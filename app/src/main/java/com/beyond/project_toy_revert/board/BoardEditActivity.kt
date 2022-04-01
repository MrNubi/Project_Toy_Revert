package com.beyond.project_toy_revert.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.MainActivity
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.api.serverUtil_okhttp
import com.beyond.project_toy_revert.databinding.ActivityBoardWriteBinding
import com.beyond.project_toy_revert.fragment.AnnounceFragment
import com.beyond.project_toy_revert.inheritance.BasicActivity
import org.json.JSONObject

class BoardEditActivity : BasicActivity() {
    private lateinit var binding : ActivityBoardWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.btnBwritePush.setOnClickListener {
            var bwTitle = binding.edtBwriteTitle.text.toString()
            var bwTilteSpaceCheck = if(bwTitle == "") "공란입니다" else bwTitle
            var bwContent = binding.edtBwriteContent.text.toString()
            var bwContentSpaceCheck = if(bwContent == "") "공란입니다" else bwContent

            serverUtil_okhttp.postAnnounceBoard(
                mContext,
                bwTilteSpaceCheck,
                bwContentSpaceCheck,
                object : serverUtil_okhttp.JsonResponseHandler_login {
                    override fun onResponse(jsonObject: JSONObject, RcCode: String) {
                        Log.d("클라라", jsonObject.toString())
                        Log.d("클라라", RcCode)
                        if(RcCode == "[\"id\",\"author\",\"nickname\",\"title\",\"content\"]"){
                            runOnUiThread {
                                Toast.makeText(mContext, "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
                                val bwintent = Intent(mContext, MainActivity::class.java)

                                startActivity(bwintent)

                            }
                        }



                    }//onResponse
                })//serverUtil_okhttp.postAnnounceBoard


        }//binding.btnBwritePush.setOnClickListener
    }
}