package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResetPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_pw)

        val loginBtn = findViewById<ImageButton>(R.id.loginButton)
        loginBtn.setOnClickListener {
            val intent = Intent (this, Login:: class.java)
            startActivity(intent)
            finish()
        }
        val resendBtn = findViewById<TextView>(R.id.resendText)
        resendBtn.setOnClickListener {
            val intent = Intent (this, ForgotPassword:: class.java)
            startActivity(intent)
            finish()
        }

        // Add other activity code here
    }
}