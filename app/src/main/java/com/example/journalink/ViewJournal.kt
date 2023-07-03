package com.example.journalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class ViewJournal : AppCompatActivity() {


    private lateinit var viewJournal: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        viewJournal = findViewById(R.id.viewJournal)
    }
}