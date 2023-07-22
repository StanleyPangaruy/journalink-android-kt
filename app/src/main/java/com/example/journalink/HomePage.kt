package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.createjournalbtn.setOnClickListener {
            val intent = Intent(this, CreateJournal::class.java)
            startActivity(intent)
        }

        val settings = findViewById<ImageButton>(R.id.imageButton5)
        settings.setOnClickListener {
            val intent = Intent (this@HomePage, SettingsPage:: class.java)
            startActivity(intent)
        }

        binding.viewjournalbtn.setOnClickListener {
            val intent = Intent(this, ViewJournal::class.java)
            startActivity(intent)
        }

        binding.profileBtn.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }
        binding.journalfeedbtn.setOnClickListener {
            val intent = Intent(this, JournalFeed::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        moveTaskToBack(true) // Minimize the app.
    }

}