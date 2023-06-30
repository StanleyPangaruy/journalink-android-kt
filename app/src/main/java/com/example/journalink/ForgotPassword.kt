package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pw)

        val sendBtn = findViewById<ImageButton>(R.id.sendButton)
        sendBtn.setOnClickListener {
            val intent = Intent (this, ResetPassword:: class.java)
            startActivity(intent)
        }

        val loginBtn = findViewById<TextView>(R.id.loginText)
        loginBtn.setOnClickListener {
            val intent = Intent (this, ResetPassword:: class.java)
            startActivity(intent)
        }
    }
}
