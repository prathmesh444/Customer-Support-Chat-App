package com.example.chatsupport.dao

data class SpecificMsgDao(
    val thread_id: Int,
    val data: ArrayList<ChatDao>
)
