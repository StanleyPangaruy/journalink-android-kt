package com.example.journalink

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpBtn = findViewById<ImageButton>(R.id.signUpButton)
        signUpBtn.setOnClickListener {
            val intent = Intent (this, SignUp:: class.java)
            startActivity(intent)
        }

        val forgotPasswordTxt = findViewById<TextView>(R.id.forgotPasswordText)
        forgotPasswordTxt.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }
}