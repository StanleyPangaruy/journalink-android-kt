package com.example.journalink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class PrivacyPolicy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

    var returntoSetting = findViewById<ImageButton>(R.id.returnbutton)
    returntoSetting.setOnClickListener{
        val intent1 = Intent(this, SettingsPage::class.java)
        startActivity(intent1)
        }
    }
}