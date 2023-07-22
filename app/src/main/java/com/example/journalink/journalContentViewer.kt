package com.example.journalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

<<<<<<<< HEAD:app/src/main/java/com/example/journalink/journalFeed.kt
class journalFeed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_feed)
========
class journalContentViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_content_viewer)
>>>>>>>> journalFeedContentViewer:app/src/main/java/com/example/journalink/journalContentViewer.kt
    }
}