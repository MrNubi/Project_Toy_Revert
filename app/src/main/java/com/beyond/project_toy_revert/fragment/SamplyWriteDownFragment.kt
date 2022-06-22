package com.beyond.project_toy_revert.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.FragmentSamplyWriteDownBinding
import com.beyond.project_toy_revert.inheritance.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class SamplyWriteDownFragment : BaseFragment() {
    private lateinit var binding:FragmentSamplyWriteDownBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_samply_write_down, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.btnSamplyWrite.setOnClickListener {

            setTextDialog()
        }
    }

    override fun setValues() {
    }
    fun createTextView(text:String):View{
        var newText = TextView(mContext)
        newText.text = "${text}"
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1F)
        newText.layoutParams = lp
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


        return newText
    }

    fun setTextDialog(){
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edt, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("택스트를 입력해주세요")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<ImageView>(R.id.btn_dialog_commentPush)?.setOnClickListener{
            val text =  alertDialog.findViewById<EditText>(R.id.edt_dialog_comment)?.text.toString()
            binding.LinsamplyWrite.addView(createTextView(text))
            alertDialog.dismiss()
        }
    }


}