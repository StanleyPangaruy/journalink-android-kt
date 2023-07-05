package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Splash : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        auth = Firebase.auth
        super.onStart()
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
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        // Adjust the layout based on screen dimensions
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.setMargin(R.id.appLogo, ConstraintSet.TOP, getPixelValue(90))
        constraintSet.setMargin(R.id.shareYourThoughtsText, ConstraintSet.TOP, getPixelValue(20))
        constraintSet.setMargin(R.id.appName, ConstraintSet.TOP, getPixelValue(16))
        constraintSet.setMargin(R.id.appDescripText, ConstraintSet.TOP, getPixelValue(24))
        constraintSet.setMargin(R.id.getStartedButton, ConstraintSet.TOP, getPixelValue(15))

        constraintSet.applyTo(constraintLayout)
    }

    private fun reload() {
        val intent = Intent(this@Splash, HomePage::class.java)
        startActivity(intent)
        finish()
    }

    private fun getPixelValue(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
