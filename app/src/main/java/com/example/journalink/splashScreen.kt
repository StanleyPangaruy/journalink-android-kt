package com.example.journalink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper


class splashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val splash =Intent(this, MainActivity::class.java)
            startActivity(splash)
            finish()
        },5000
        )

    }
}