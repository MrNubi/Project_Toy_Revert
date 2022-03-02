package com.beyond.project_toy_revert.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FBRef {


    companion object{

        private val database = Firebase.database

        val storage = Firebase.storage




        val storageRef = storage.reference

        val category1 = database.getReference("contents")

        val bookMarkRef = database.getReference("bookmark_list")

        val commentRef = database.getReference("comment")

        val iconRef = database.getReference("icon")

        val proNickRef = database.getReference("Nickname")

        val proTXTONERef = database.getReference("TXTOneLine")




        val re_commentRef = database.getReference("re_comment")
        val profile_Ref = storage.getReference("profile")





        val boardRef = database.getReference("board")


    }}