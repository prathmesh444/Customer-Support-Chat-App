package com.example.chatsupport.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatsupport.dao.IncomingMessageDao
import com.example.chatsupport.dao.OutgoingMessageDao
import com.example.chatsupport.dao.ResLogin
import com.example.chatsupport.dao.loginDao
import com.example.chatsupport.network.Repository
import com.example.chatsupport.utilis.Resource
import kotlinx.coroutines.launch

class NetworkViewModel: ViewModel() {
    private val repo = Repository()

    val loginResp: MutableLiveData<Resource<ResLogin>> = MutableLiveData()
    val incomingRes: MutableLiveData<Resource<ArrayList<IncomingMessageDao>>> = MutableLiveData()
    val sendData: MutableLiveData<Resource<IncomingMessageDao>> = MutableLiveData()

    fun login(LoginDao: loginDao) {
        loginResp.value = Resource.Loading()
        viewModelScope.launch {
            val res = repo.login(LoginDao)
            if (res.isSuccessful()){
                res.body()?.let {
                    loginResp.postValue(Resource.Success(it))
                }
            }else{
                loginResp.postValue(Resource.Error(res.message()))
            }
        }
    }
    fun getIncomingMessages(header: Map<String,String>){
        incomingRes.value = Resource.Loading()
        viewModelScope.launch {
            val res = repo.getIncomingMessages(header)
            if (res.isSuccessful){
                res.body()?.let {
                    incomingRes.postValue(Resource.Success(it))
                }
            }else{
                incomingRes.postValue(Resource.Error(res.message()))
            }
        }
    }
    fun sendMessages(header: Map<String, String>, outgoingMessageDao: OutgoingMessageDao){
        sendData.value = Resource.Loading()
        viewModelScope.launch {
            val res = repo.sendMessages(header, outgoingMessageDao)
            res.body()?.let {
                if(res.isSuccessful){
                    sendData.postValue(Resource.Success(it))
                }
            }
        }
    }
}