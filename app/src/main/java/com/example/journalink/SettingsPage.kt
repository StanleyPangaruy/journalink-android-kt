package com.example.journalink



import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton



class SettingsPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)


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

        var privacypolicy = findViewById<ImageButton>(R.id.privacyPolicybtn)
        privacypolicy.setOnClickListener {
            val intent1 = Intent(this, PrivacyPolicy::class.java)
            startActivity(intent1)
        }

        var about = findViewById<ImageButton>(R.id.aboutBtn)
        about.setOnClickListener{
            val intent2 = Intent(this, AboutUs::class.java)
            startActivity(intent2)
        }

    }
}