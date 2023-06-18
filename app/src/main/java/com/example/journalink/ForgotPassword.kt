package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pw_code)

        val verifyBtn = findViewById<ImageButton>(R.id.verifyButton)
        verifyBtn.setOnClickListener {
            val intent = Intent (this, ResetPassword:: class.java)
            startActivity(intent)
        }
    }
}
