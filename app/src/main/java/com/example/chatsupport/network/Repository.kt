package com.example.chatsupport.network

import com.example.chatsupport.dao.IncomingMessageDao
import com.example.chatsupport.dao.OutgoingMessageDao
import com.example.chatsupport.dao.ResLogin
import com.example.chatsupport.dao.loginDao
import retrofit2.Response

class Repository {
    suspend fun login(LoginDao: loginDao): Response<ResLogin> {
        return RetrofitInstance.api.login(LoginDao)
    }

    suspend fun getIncomingMessages(header: Map<String,String>): Response<ArrayList<IncomingMessageDao>> {
        return RetrofitInstance.api.getMessages(header)
    }

    suspend fun sendMessages(header: Map<String,String>, outgoingMessageDao: OutgoingMessageDao): Response<IncomingMessageDao> {
        return RetrofitInstance.api.sendMessage(header, outgoingMessageDao)
    }
}