package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.LoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, TermsandConditions::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordText.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (isEmailValid(email) && isPasswordValid(password)) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return email.matches(emailRegex)
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
        return password.matches(passwordRegex)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val userId = user.uid
            val email = user.email

            // Write the login credentials to the Realtime Database
            val userRef = database.child("users").child(userId)
            userRef.child("email").setValue(email)
                .addOnSuccessListener {
                    Log.d(TAG, "Login credentials written to the database.")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Failed to write login credentials to the database.", exception)
                }

            // TODO: Implement UI updates after successful login
            val intent = Intent(this@Login, HomePage::class.java)
            startActivity(intent)
            finish()
        } else {
            // Handle the case where the user is null (failed login)
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
    
    override fun onBackPressed() {
        moveTaskToBack(true) // Minimize the app.
    }

}
