package com.example.journalink
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.LoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    // Perform login with email or username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        binding = LoginPageBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (isEmailValid(email) && isPasswordValid(password)) { // Validate email and password
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Logged In Successfully.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomePage::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }


        val signUpBtn = findViewById<ImageButton>(R.id.signUpButton)
        signUpBtn.setOnClickListener {
            val intent = Intent (this, SignUp:: class.java)
            startActivity(intent)
        }
        val forgotPasswordTxt = findViewById<TextView>(R.id.forgotPasswordText)
        forgotPasswordTxt.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return email.matches(emailRegex)
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
        return password.matches(passwordRegex)
    }
}