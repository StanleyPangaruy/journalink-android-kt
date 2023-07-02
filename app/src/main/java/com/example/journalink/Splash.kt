package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val getStartedBtn = findViewById<ImageButton>(R.id.getStartedButton)
        getStartedBtn.setOnClickListener {
            val intent = Intent (this, Login:: class.java)
            startActivity(intent)
            finish()
        }

        // Add other activity code here
    }
}