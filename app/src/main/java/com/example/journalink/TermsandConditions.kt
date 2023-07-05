package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.ActivityTermsandConditionsBinding


class TermsandConditions : AppCompatActivity() {
    private lateinit var binding: ActivityTermsandConditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsandConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn = findViewById<ImageButton>(R.id.proceedBtn)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        btn.visibility = GONE
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                btn.visibility = VISIBLE
                btn.setOnClickListener {
                    val intent = Intent(this, SignUp::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                btn.visibility = GONE
            }
        }
    }
}