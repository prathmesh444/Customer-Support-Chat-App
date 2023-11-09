package com.example.chatsupport.activity

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsupport.R
import com.example.chatsupport.adaptor.SpecificThreadAdapter
import com.example.chatsupport.dao.ChatDao
import com.example.chatsupport.dao.OutgoingMessageDao
import com.example.chatsupport.utilis.Constants
import com.example.chatsupport.utilis.Resource
import com.example.chatsupport.utilis.SharedPreferences
import com.example.chatsupport.viewModel.NetworkViewModel
import java.sql.Timestamp
import java.time.Instant
import java.util.Date


class SpecificThread : AppCompatActivity(){

    private lateinit var _recyclerView: RecyclerView
    private lateinit var _inputMessage: EditText
    private lateinit var _sendButton: Button
    private lateinit var _mAdapter: SpecificThreadAdapter
    private lateinit var _header:Map<String,String>

    private val _dataList: MutableList<ChatDao> = mutableListOf<ChatDao>()
    private val viewmodel = NetworkViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.specific_messages_layout)


        _recyclerView = findViewById(R.id.specificRecyclerView)
        _inputMessage = findViewById(R.id.InputMessageText)
        _sendButton = findViewById(R.id.buttonSend)
        _header = mapOf(Constants.HEADER to SharedPreferences.get(Constants.LOGIN_AUTH,this))


        val data = Constants.MSGLIST
        val threadID = intent.extras?.getString("thread_id")
        val position = intent.extras?.getInt("position")
        print(data[threadID])
        data[threadID]?.forEach {
            val userTimestamp = Timestamp.from(Instant.parse(it.timestamp))
            if(it.agent_id == null) {
                _dataList.add(ChatDao(it.thread_id, it.user_id, true, it.body, userTimestamp))
            }
            else {
                _dataList.add(ChatDao(it.thread_id, it.agent_id, false, it.body, userTimestamp))
            }
        }

        _recyclerView.layoutManager = LinearLayoutManager(this)
        _mAdapter = SpecificThreadAdapter(_dataList)
        _recyclerView.adapter = _mAdapter


        _sendButton.setOnClickListener {
            val message = _inputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewmodel.sendMessages(_header, OutgoingMessageDao(threadID!!, message))
                setObservers()
            }
            _inputMessage.text.clear()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers() {
        viewmodel.sendData.observe(this){ resource ->
            when(resource){
                is Resource.Error ->  Toast.makeText(this, "Error Sending the Messages", Toast.LENGTH_SHORT).show()
                is Resource.Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                is Resource.Success -> {
                    resource.data?.let { it ->
                        val agenttimestamp: Date? = Timestamp.from(Instant.parse(it.timestamp))
                        _dataList.add(ChatDao(it.thread_id, it.agent_id, false, it.body, agenttimestamp))
                        _dataList.sortBy { it.time }
                        _mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}