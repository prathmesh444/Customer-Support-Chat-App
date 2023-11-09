package com.example.chatsupport.network

import com.example.chatsupport.dao.IncomingMessageDao
import com.example.chatsupport.dao.OutgoingMessageDao
import com.example.chatsupport.dao.ResLogin
import com.example.chatsupport.dao.loginDao
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface ApiService {
    @POST("api/login")
    suspend fun login(@Body loginDao: loginDao): Response<ResLogin>


    @GET("api/messages")
    suspend fun getMessages(@HeaderMap headers: Map<String, String>): Response<ArrayList<IncomingMessageDao>>


    @POST("api/messages")
    suspend fun sendMessage(@HeaderMap headers: Map<String, String>, @Body outgoingmessageDao: OutgoingMessageDao) : Response<IncomingMessageDao>
}