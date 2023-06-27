package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.SignUpPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var binding: SignUpPageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var signupName: EditText? = null
    private var signupUsername: EditText? = null
    private var signupEmail: EditText? = null
    private var signupPassword: EditText? = null
    private var signupButton: Button? = null
    private var database: FirebaseDatabase? = null
    private var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)
        binding = SignUpPageBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        signupName = findViewById(R.id.completeNameEditText)
        signupEmail = findViewById(R.id.emailEditText)
        signupUsername = findViewById(R.id.usernameEditText)
        signupPassword = findViewById(R.id.passwordEditText)
        val signupButton = findViewById<ImageButton>(R.id.registerButton)
        signupButton.setOnClickListener {
            database = FirebaseDatabase.getInstance()
            reference = database!!.getReference("users")
            val name = signupName!!.text.toString()
            val email = signupEmail!!.text.toString()
            val username = signupUsername!!.text.toString()
            val password = signupPassword!!.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }}
            val helperClass = HelperClass(
                name = name,
                email = email,
                username = username,
                password = password
            )
            reference!!.child(username).setValue(helperClass)
            Toast.makeText(this@SignUp, "You have signed up successfully!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@SignUp, MainActivity::class.java)
            startActivity(intent)
        }

        val arrowBtn = findViewById<ImageButton>(R.id.arrow)
        arrowBtn.setOnClickListener {
            val intent = Intent (this, MainActivity:: class.java)
            startActivity(intent)
        }

        // Add other activity code here
    }
}
