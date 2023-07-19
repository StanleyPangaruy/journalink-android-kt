package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfile : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var profileRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityEditProfileBinding
    private var hasUnsavedChanges = false

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
            showSaveConfirmationDialog()
        }

        // Set an OnClickListener to the Return button
        binding.returnButton.setOnClickListener {
            showUnsavedConfirmationDialog()
        }

        // Populate the fields with previously saved credentials
        populateFields()
    }

    private fun saveProfile() {
        val fullName = binding.fullNametxtbox.text.toString().trim()
        val email = binding.emailTxtBox.text.toString().trim()
        val phoneNumber = binding.phoneNumbertxtbox.text.toString().trim()
        val nickname = binding.nicknametxtbox.text.toString().trim()

        // Get the current user from Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null && email != currentUser.email) {
            // Update the email address in Firebase Authentication
            currentUser.updateEmail(email)
                .addOnCompleteListener { emailUpdateTask ->
                    if (emailUpdateTask.isSuccessful) {
                        // Email address updated successfully
                        // Now update the profile data in the Firebase Realtime Database
                        val profile = Profile(fullName, email, phoneNumber, nickname)
                        profileRef.setValue(profile)
                            .addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    // Profile saved successfully
                                    Toast.makeText(
                                        this,
                                        "Profile Saved Successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navigateToProfilePage()
                                } else {
                                    // Handle error while updating profile
                                    Toast.makeText(
                                        this,
                                        "Failed to edit profile.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        // Handle error while updating email address
                        Toast.makeText(
                            this,
                            "Failed to update email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // No changes to email address or user not authenticated
            // Proceed with saving the profile data only
            val profile = Profile(fullName, email, phoneNumber, nickname)
            profileRef.setValue(profile)
                .addOnCompleteListener { profileUpdateTask ->
                    if (profileUpdateTask.isSuccessful) {
                        // Profile saved successfully
                        Toast.makeText(
                            this,
                            "Profile Saved Successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToProfilePage()
                    } else {
                        // Handle error while updating profile
                        Toast.makeText(
                            this,
                            "Failed to edit profile.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }



    private fun showUnsavedConfirmationDialog() {
        if (hasUnsavedChanges) {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you want to save them?")
                .setPositiveButton("Save") { _, _ ->
                    saveProfile()
                }
                .setNegativeButton("Discard") { _, _ ->
                    navigateToProfilePage()
                }
                .setNeutralButton("Cancel", null)
                .setCancelable(false)
                .create()

            alertDialog.show()
        } else {
            navigateToProfilePage()
        }
    }

    private fun showSaveConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Save Changes")
            .setMessage("Do you want to save these changes?")
            .setPositiveButton("Save") { _, _ ->
                saveProfile()
            }
            .setNegativeButton("Go Back") { _, _ ->
                // No action needed here, as pressing "Go Back" will automatically close the dialog
            }
            .setNeutralButton("Cancel", null)
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun navigateToProfilePage() {
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
        finish()
    }

    private fun populateFields() {
        // Read data from the database
        profileRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = snapshot.getValue(Profile::class.java)
                if (profile != null) {
                    // Populate the EditText fields with the retrieved profile data
                    binding.fullNametxtbox.setText(profile.name)
                    binding.emailTxtBox.setText(profile.email)
                    binding.phoneNumbertxtbox.setText(profile.phoneNumber)
                    binding.nicknametxtbox.setText(profile.nickname)
                    hasUnsavedChanges = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(
                    this@EditProfile,
                    "Failed to load profile.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onBackPressed() {
        showUnsavedConfirmationDialog()
    }
}
