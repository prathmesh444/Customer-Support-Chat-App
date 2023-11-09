package com.example.chatsupport.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatsupport.R
import com.example.chatsupport.R.layout.login_layout

import com.example.chatsupport.dao.loginDao
import com.example.chatsupport.utilis.Constants
import com.example.chatsupport.utilis.Resource
import com.example.chatsupport.utilis.SharedPreferences
import com.example.chatsupport.viewModel.NetworkViewModel
import com.google.gson.Gson


class Login : AppCompatActivity(){
    private lateinit var _editTextEmail: EditText
    private lateinit var _editTextPassword: EditText
    private lateinit var _buttonLogin: Button
    private lateinit var viewModel: NetworkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(login_layout)
        if(SharedPreferences.get(Constants.LOGIN_AUTH,this) != "-1"){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        this._editTextEmail = findViewById(R.id.editTextEmail)
        this._editTextPassword = findViewById(R.id.editTextPassword)
        this._buttonLogin = findViewById(R.id.buttonLogin)
        this.viewModel = NetworkViewModel()

        _buttonLogin.setOnClickListener{
            val username = _editTextEmail.text.toString().trim()
            val password = _editTextPassword.text.toString().trim()

            if (username.isNotBlank()){
                viewModel.login(loginDao(username,password))
            }else{
                Toast.makeText(this,"Please input proper value", Toast.LENGTH_SHORT).show()
            }
        }
        setObservers()
    }

    private fun setObservers() {
        viewModel.loginResp.observe(this) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { it ->
                        SharedPreferences.insert(Constants.USER_DATA, Gson().toJson(it),this)
                        SharedPreferences.insert(Constants.LOGIN_AUTH,it.auth_token,this)
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()

                }
                else -> {}
            }
        }

    }
}