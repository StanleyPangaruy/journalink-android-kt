package com.example.journalink

import android.content.Intent
import android.os.Bundle
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
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Check email and password validity
            fun isEmailValid(email: String): Boolean {
                val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
                return email.matches(emailRegex)
            }

            fun isPasswordValid(password: String): Boolean {
                val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
                return password.matches(passwordRegex)
            }

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (isEmailValid(email)) {
                    if (isPasswordValid(password)) {
                        if (password == confirmPassword) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Write user data to the Realtime Database
                                        val userId = firebaseAuth.currentUser?.uid
                                        if (userId != null) {
                                            val userRef = database.child("users").child(userId)
                                            userRef.child("email").setValue(email)
                                                .addOnSuccessListener {
                                                    // User data written to the database
                                                    val intent = Intent(this@SignUp, Login::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                    Toast.makeText(
                                                        this,
                                                        "Account Created Successfully.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                .addOnFailureListener { exception ->
                                                    // Failed to write user data to the database
                                                    Toast.makeText(
                                                        this,
                                                        "Failed to create an account.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            task.exception.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and one special character.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        val arrowBtn = findViewById<ImageButton>(R.id.arrow)
        arrowBtn.setOnClickListener {
            val intent = Intent (this, Login:: class.java)
            startActivity(intent)
        }

    }

    // Check email and password validity functions
    // ...
    override fun onBackPressed() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}
