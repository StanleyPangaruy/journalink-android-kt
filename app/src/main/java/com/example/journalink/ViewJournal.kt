package com.example.journalink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ViewJournal : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var journalRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        recyclerView = findViewById(R.id.recyclerView)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        journalRef = database.reference.child("journals")

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUserId = auth.currentUser?.uid

        val query = journalRef.orderByChild("uid").equalTo(currentUserId)

        val options = FirebaseRecyclerOptions.Builder<Journal>()
            .setQuery(query, Journal::class.java)
            .build()

        adapter = RVAdapter(options)
        recyclerView.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}