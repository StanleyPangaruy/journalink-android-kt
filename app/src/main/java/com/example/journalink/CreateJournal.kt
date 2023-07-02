package com.example.journalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

<<<<<<<< HEAD:app/src/main/java/com/example/journalink/HomePage.kt
class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
========
class CreateJournal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_journal)
>>>>>>>> createJournalpage:app/src/main/java/com/example/journalink/CreateJournal.kt
    }
}