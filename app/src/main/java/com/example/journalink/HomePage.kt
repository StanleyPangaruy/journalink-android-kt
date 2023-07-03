package com.example.journalink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.google.firebase.FirebaseApp

class HomePage : AppCompatActivity() {
    private lateinit var createjournalbtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_home_page)

        createjournalbtn = findViewById(R.id.createjournalbtn)

        createjournalbtn.setOnClickListener {
            val intent = Intent(this, CreateJournal::class.java)
            startActivity(intent)
        }
    }
}