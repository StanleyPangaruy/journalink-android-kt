package com.example.journalink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)

        auth = Firebase.auth


        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            val intent4 = Intent(this, HomePage::class.java)
            startActivity(intent4)
            finish()
        }

        val gmailButton: ImageButton = findViewById(R.id.contactUs)
        gmailButton.setOnClickListener {
            val recipient = "imaginativevtechnologies@gmail.com"
            val subject = ""
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            startActivity(intent)
        }

        val privacypolicy = findViewById<ImageButton>(R.id.privacyPolicybtn)
        privacypolicy.setOnClickListener {
            val intent1 = Intent(this, PrivacyPolicy::class.java)
            startActivity(intent1)
        }

        val about = findViewById<ImageButton>(R.id.aboutBtn)
        about.setOnClickListener {
            val intent2 = Intent(this, AboutUs::class.java)
            startActivity(intent2)
        }

        val logoutButton = findViewById<ImageButton>(R.id.imageButton6)
        logoutButton.setOnClickListener {
            // Perform logout logic here
            Firebase.auth.signOut()
            val intent3 = Intent(this, Login::class.java)
            startActivity(intent3)
            finish()
        }
    }
}
