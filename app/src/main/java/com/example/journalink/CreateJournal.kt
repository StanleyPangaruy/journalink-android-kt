package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class CreateJournal : AppCompatActivity() {
    private lateinit var journalInputuser: EditText
    private lateinit var savebtn: ImageButton
    private lateinit var noteDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_create_journal)

        journalInputuser = findViewById(R.id.journalInputuser)
        savebtn = findViewById(R.id.savebtn)
        noteDao = NoteDao()


        savebtn.setOnClickListener {
            val note = journalInputuser.text.toString()
            if(note.isNotEmpty()){
                noteDao.addNote(note)
                val intent = Intent(this,ViewJournal::class.java)
                startActivity(intent)
            }
        }
    }
}