package com.beyond.project_toy_revert.testActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.databinding.ActivitySearchTestBinding
import com.beyond.project_toy_revert.inheritance.BasicActivity

class SearchTestActivity : BasicActivity() {
    private lateinit var binding: ActivitySearchTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_test)

        val searchData = arrayOf("data1","data2","data3","usedData")
        //  searchData = 자동검색 띄우는 데이터 리스트
        // history..?

        binding.autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, searchData))
        // ArrayAdapter<String>(어디에, 어떤 형태로, 어떤 데이터를)
        binding.autoCompleteTextView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //검색 버튼 누를 시, 뭘 할건지
                    var Atext = binding.autoCompleteTextView.text.toString()
                    Toast.makeText(mContext, "${Atext}", Toast.LENGTH_LONG).show()
                    Log.d("로그","${Atext}")
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.autoCompleteTextView.windowToken, 0)
                    return true
                }
                return false
            }
        })
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            // 자동완성 아이템 클릭시 해야할 것
            Log.d("로그", "position: ${i}, rowId:${l}, string: ${adapterView.getItemAtPosition(i)}")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.autoCompleteTextView.windowToken, 0)
        }

        // 히스토리 뷰,
    }
}