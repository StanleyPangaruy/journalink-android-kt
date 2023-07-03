package com.example.journalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

<<<<<<<< HEAD:app/src/main/java/com/example/journalink/CreateJournal.kt
class CreateJournal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_journal)
========
class viewJournal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)
>>>>>>>> origin/viewJournalpage:app/src/main/java/com/example/journalink/viewJournal.kt
    }
}