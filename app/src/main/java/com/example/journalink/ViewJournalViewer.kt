package com.example.journalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class ViewJournalViewer : AppCompatActivity() {

    private lateinit var journTitle : TextView
    private lateinit var journshortDesc : TextView
    private lateinit var journContent : TextView
    private lateinit var shareBUTTON : ImageButton
    private lateinit var closeJournal : ImageButton
    private lateinit var deleteBUTTON : ImageButton
    private lateinit var editBUTTON : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal_viewer)

        initView()
        setValuesToViews()
    }

    private fun initView() {
        journTitle = findViewById(R.id.journTitle)
        journshortDesc = findViewById(R.id.journshortDesc)
        journContent = findViewById(R.id.journContent)

        editBUTTON = findViewById(R.id.editBUTTON)
        deleteBUTTON = findViewById(R.id.deleteBUTTON)
    }

    private fun setValuesToViews() {
        journTitle.text = intent.getStringExtra("title")
        journshortDesc.text = intent.getStringExtra("shortDescription")
        journContent.text = intent.getStringExtra("content")
    }
}