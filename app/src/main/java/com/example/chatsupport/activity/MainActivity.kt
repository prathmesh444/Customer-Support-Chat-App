package com.example.chatsupport.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsupport.adaptor.MainActivityAdapter
import com.example.chatsupport.R
import com.example.chatsupport.dao.IncomingMessageDao
import com.example.chatsupport.utilis.Constants
import com.example.chatsupport.utilis.Resource
import com.example.chatsupport.utilis.SharedPreferences
import com.example.chatsupport.viewModel.NetworkViewModel
import java.sql.Timestamp
import java.time.Instant


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: MainActivityAdapter
    private lateinit var header:Map<String,String>
    private var msgList:MutableMap<String, MutableList<IncomingMessageDao>> = mutableMapOf()
    val viewModel = NetworkViewModel()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        header = mapOf(Constants.HEADER to SharedPreferences.get(Constants.LOGIN_AUTH,this))

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = MainActivityAdapter(msgList)
        recyclerView.adapter = mAdapter
        setObservers()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setObservers()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers() {

        viewModel.getIncomingMessages(header)
        viewModel.incomingRes.observe(this){ it ->
            when(it){
                is Resource.Error -> {
                    Toast.makeText(this, "Error Loading the Messages", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    msgList.clear()
                    Constants.MSGLIST.clear()
                    it.data.let {resourse->
                        resourse?.forEach {
                            if(msgList.containsKey(it.thread_id)) {
                                msgList[it.thread_id]?.add(it)
                            } else {
                                msgList.set(it.thread_id, mutableListOf(it))
                            }
                        }
                    }
                    msgList.forEach{ listEntry ->
                        listEntry.value.sortByDescending { Timestamp.from(Instant.parse(it.timestamp)) }
                    }

                    Constants.MSGLIST = msgList
                    mAdapter.notifyDataSetChanged()

                }
            }
        }
    }
}