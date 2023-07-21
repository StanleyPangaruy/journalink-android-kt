package com.example.journalink

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CreateJournal : AppCompatActivity() {

    private lateinit var journalTitleInput: EditText
    private lateinit var shortDescriptionTxt: EditText
    private lateinit var journalInputUser: EditText
    private lateinit var saveBtn: ImageButton
    private lateinit var journalEntry: JournalEntry
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_journal)

        journalTitleInput = findViewById(R.id.journaltitleInput)
        shortDescriptionTxt = findViewById(R.id.shortDescriptiontxt)
        journalInputUser = findViewById(R.id.journalInputuser)
        saveBtn = findViewById(R.id.savebtn)
        journalEntry = JournalEntry()

        saveBtn.setOnClickListener {
            val title = journalTitleInput.text.toString()
            val shortDescription = shortDescriptionTxt.text.toString()
            val content = journalInputUser.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                showProgressDialog("Saving Journal...")

                // Simulate a delay to show the loading dialog
                Handler().postDelayed({
                    journalEntry.createJournal(title, shortDescription, content)

                    // Dismiss the loading dialog after saving is done
                    dismissProgressDialog()

                    // Navigate to View Journal
                    val intent = Intent(this, ViewJournal::class.java)
                    startActivity(intent)
                }, 2000) // Simulate 2 seconds delay, replace with actual saving process

            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

    private fun showProgressDialog(message: String) {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun dismissProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}
