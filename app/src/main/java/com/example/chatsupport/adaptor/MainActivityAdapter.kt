package com.example.chatsupport.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsupport.R
import com.example.chatsupport.activity.SpecificThread
import com.example.chatsupport.dao.IncomingMessageDao

class MainActivityAdapter(val msgList: MutableMap<String, MutableList<IncomingMessageDao>>): RecyclerView.Adapter<MainActivityAdapter.MainViewHolder>() {

    var my_data: MutableList<IncomingMessageDao> = mutableListOf()
    inner class MainViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val thread_id: TextView = view.findViewById(R.id.thread_id)
        val body: TextView = view.findViewById(R.id.body)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thread_item, parent, false)
        msgList.forEach{
            my_data.add(it.value[0])
        }
        return MainViewHolder(view)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val currentItem = my_data[position]
        holder.thread_id.text = currentItem.thread_id
        holder.body.text = currentItem.body
        holder.timestamp.text = currentItem.timestamp

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, SpecificThread()::class.java)
            intent.putExtra("position", position)
            intent.putExtra("thread_id",currentItem.thread_id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

}