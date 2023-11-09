package com.example.chatsupport.dao

data class IncomingMessageDao(
    val id: String,
    val thread_id: String,
    val user_id: String,
    val agent_id: String?,
    var body: String,
    val timestamp: String,
)
