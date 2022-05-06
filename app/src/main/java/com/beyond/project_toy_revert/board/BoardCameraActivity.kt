package com.beyond.project_toy_revert.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.ActivityBoardCameraBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity

class BoardCameraActivity : BasicActivity() {
    private lateinit var binding: ActivityBoardCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_camera)




    }



}