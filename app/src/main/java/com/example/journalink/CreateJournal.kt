package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class CreateJournal : AppCompatActivity() {

    private lateinit var journaltitleInput: EditText
    private lateinit var shortDescriptiontxt: EditText
    private lateinit var journalInputuser: EditText
    private lateinit var savebtn: ImageButton
    private lateinit var journalEntry: JournalEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_journal)

        journaltitleInput = findViewById(R.id.journaltitleInput)
        shortDescriptiontxt = findViewById(R.id.shortDescriptiontxt)
        journalInputuser = findViewById(R.id.journalInputuser)
        savebtn = findViewById(R.id.savebtn)
        journalEntry = JournalEntry()

        savebtn.setOnClickListener {
            val journal = journaltitleInput.text.toString()
            if(journal.isNotEmpty()) {
                journalEntry.createJournal(journal)

                // navigate to View Journal
                val intent = Intent(this, ViewJournal::class.java)
                startActivity(intent)
            }
        }

    }
}