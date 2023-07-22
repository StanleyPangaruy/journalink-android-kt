package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewJournalViewer : AppCompatActivity() {

    private lateinit var journTitle: TextView
    private lateinit var journshortDesc: TextView
    private lateinit var journContent: TextView
    private lateinit var shareBUTTON: ImageButton
    private lateinit var closeJournal: ImageButton
    private lateinit var deleteBUTTON: ImageButton
    private lateinit var editBUTTON: ImageButton

    private lateinit var journalId: String // Assuming you have the journal ID available

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal_viewer)

        initView()
        setValuesToViews()

        shareBUTTON.setOnClickListener {
            shareJournal()
        }
    }

    private fun initView() {
        journTitle = findViewById(R.id.journTitle)
        journshortDesc = findViewById(R.id.journshortDesc)
        journContent = findViewById(R.id.journContent)

        editBUTTON = findViewById(R.id.editBUTTON)
        shareBUTTON = findViewById(R.id.shareBUTTON)
        deleteBUTTON = findViewById(R.id.deleteBUTTON)
    }

    private fun setValuesToViews() {
        journTitle.text = intent.getStringExtra("title")
        journshortDesc.text = intent.getStringExtra("shortDescription")
        journContent.text = intent.getStringExtra("content")

        journalId =
            intent.getStringExtra("journalId").toString() // Assuming you have the journal ID as an extra in the intent
    }

    private fun shareJournal() {
        val title = journTitle.text.toString()
        val shortDescription = journshortDesc.text.toString()
        val date = getCurrentDate() // Replace this with the actual date value you want to use

        // Store the shared data in Firebase Realtime Database under the specific journal ID
        val databaseReference = FirebaseDatabase.getInstance().getReference("shared_journals").push()
        val newJournalId = databaseReference.key.toString()
        val data = mapOf(
            "title" to title,
            "shortDescription" to shortDescription,
            "date" to date
        )

        databaseReference.setValue(data)
            .addOnSuccessListener {
                // Data shared successfully
            }
            .addOnFailureListener {
                // Failed to share data, handle the error
            }
    }

    // Function to get the current date (replace this with your actual date logic)
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
