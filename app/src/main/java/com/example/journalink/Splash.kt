package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Splash : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    public override fun onStart() {
        auth = Firebase.auth
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

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

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
    private fun reload() {
        val intent = Intent(this@Splash, HomePage::class.java)
        startActivity(intent)
        finish()
    }
}