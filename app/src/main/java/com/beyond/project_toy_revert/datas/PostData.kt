package com.beyond.project_toy_revert.datas

import com.google.gson.JsonArray
import org.json.JSONObject

class PostData(

    val id: Int,
    val author: String,
    val nickname: String,
    val title: String,
    val content : String,
    val images:JsonArray,
    val tag_set: JsonArray,
    val is_like: Boolean,
    val created_at: String,
    val updated_at: String,


) {
}