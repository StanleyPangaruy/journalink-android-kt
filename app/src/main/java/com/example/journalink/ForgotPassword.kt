package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    private lateinit var etPassword: EditText
    private lateinit var sendBtnPassword: ImageButton

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pw)

        etPassword = findViewById(R.id.emailEditText)
        sendBtnPassword = findViewById(R.id.sendButton)

        auth = FirebaseAuth.getInstance()

        sendBtnPassword.setOnClickListener {
            val sPassword = etPassword.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    Toast.makeText(this,"Please Check your Email!", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this, ResetPassword:: class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
        val loginBtn = findViewById<TextView>(R.id.loginText)
        loginBtn.setOnClickListener {
            val intent = Intent (this, Login:: class.java)
            startActivity(intent)
            finish()
        }
    }
}

