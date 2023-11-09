package com.example.chatsupport.utilis

import com.example.chatsupport.dao.IncomingMessageDao

sealed class Constants{
    companion object{
        const val BASE_URL:String = "https://android-messaging.branch.co/"
        const val LOGIN_AUTH: String = "login_auth"
        const val USER_DATA : String = "user_data"
        const val HEADER : String = "X-Branch-Auth-Token"
        var MSGLIST: MutableMap<String, MutableList<IncomingMessageDao>> = mutableMapOf()
    }
}
