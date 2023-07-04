package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

    var returntoSetting = findViewById<ImageButton>(R.id.closebutton)
    returntoSetting.setOnClickListener{
        val intent1 = Intent(this, SettingsPage::class.java)
        startActivity(intent1)
        finish()
        }
    }
}