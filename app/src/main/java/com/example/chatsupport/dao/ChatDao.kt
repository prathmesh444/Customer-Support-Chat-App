package com.example.chatsupport.dao
import java.util.Date


data class ChatDao(
    val thread_id: String? = "",
    val _id: String? = "",
    val sender: Boolean = true,
    val message: String? = "",
    val time: Date?
)
