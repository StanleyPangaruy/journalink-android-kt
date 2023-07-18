package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfile : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var profileRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        profileRef = database.reference.child("users").child(userId ?: "")

        // Set an OnClickListener to the Save button
        binding.saveBtn.setOnClickListener {
            saveProfile()
        }

        // Set an OnClickListener to the Return button
        binding.returnButton.setOnClickListener {
            val hasUnsavedChanges = hasUnsavedChanges()
            if (hasUnsavedChanges) {
                showConfirmationDialog()
            } else {
                navigateToProfilePage()
            }
        }
    }

    private fun saveProfile() {
        val fullName = binding.fullNametxtbox.text.toString().trim()
        val email = binding.emailTxtBox.text.toString().trim()
        val phoneNumber = binding.phoneNumbertxtbox.text.toString().trim()
        val nickname = binding.nicknametxtbox.text.toString().trim()

        val profile = Profile(fullName, email, phoneNumber, nickname)
        profileRef.setValue(profile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Profile saved successfully
                    val intent = Intent(this, ProfilePage::class.java)
                    Toast.makeText(
                        this,
                        "Profile Saved Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(intent)
                    finish()
                } else {
                    // Handle error
                    Toast.makeText(
                        this,
                        "Failed to edit profile.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun hasUnsavedChanges(): Boolean {
        val fullName = binding.fullNametxtbox.text.toString().trim()
        val email = binding.emailTxtBox.text.toString().trim()
        val phoneNumber = binding.phoneNumbertxtbox.text.toString().trim()
        val nickname = binding.nicknametxtbox.text.toString().trim()

        // Check if any fields have been modified
        return fullName.isNotEmpty() || email.isNotEmpty() || phoneNumber.isNotEmpty() || nickname.isNotEmpty()
    }

    private fun showConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage("You have unsaved changes. Do you want to save them?")
            .setPositiveButton("Save") { _, _ ->
                saveProfile()
            }
            .setNegativeButton("Discard") { _, _ ->
                navigateToProfilePage()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun navigateToProfilePage() {
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
    }
}
