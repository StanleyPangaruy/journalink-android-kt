package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.journalink.databinding.ActivityProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfilePage : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var profileRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        binding.editProfilebtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        profileRef = database.reference.child("users").child(userId ?: "")

        // Read data from the database
        profileRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = snapshot.getValue(Profile::class.java)
                updateUI(profile)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(
                    this@ProfilePage.applicationContext,
                    "Failed to load profile.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateUI(profile: Profile?) {
        // Update the UI with the retrieved profile data
        binding.EntryName.text = profile?.name ?: ""
        binding.EntryEmail.text = profile?.email ?: ""
        binding.EntryPhoneNumber.text = profile?.phoneNumber ?: ""
        binding.EntryNickname.text = profile?.nickname ?: ""
    }
}
