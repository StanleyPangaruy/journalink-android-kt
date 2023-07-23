package com.example.journalink

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class JournalContentViewer : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_content_viewer)

        // Retrieve the journal title and content from the intent extras
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        // Find TextViews in the layout
        val titleTextView = findViewById<TextView>(R.id.titletxt)
        val contentTextView = findViewById<TextView>(R.id.contentTxt)
        val backButton = findViewById<ImageView>(R.id.backBtn)


        // Set the title and content to the TextViews
        titleTextView.text = title
        contentTextView.text = content

        backButton.setOnClickListener {
            finish()
        }
    }
}
