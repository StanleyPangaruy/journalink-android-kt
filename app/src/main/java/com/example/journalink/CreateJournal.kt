package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CreateJournal : AppCompatActivity() {

    private lateinit var journalTitleInput: EditText
    private lateinit var shortDescriptionTxt: EditText
    private lateinit var journalInputUser: EditText
    private lateinit var saveBtn: ImageButton
    private lateinit var journalEntry: JournalEntry

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
                journalEntry.createJournal(title, shortDescription, content)

                // navigate to View Journal
                val intent = Intent(this, ViewJournal::class.java)
                startActivity(intent)
            }
        }
    }
}
