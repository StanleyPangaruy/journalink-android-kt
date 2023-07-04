package com.example.journalink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ViewJournal : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var journalEntry: JournalEntry
    private lateinit var auth: FirebaseAuth
    private lateinit var adaptor: RVAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        recyclerView = findViewById(R.id.recyclerView)

        journalEntry = JournalEntry()
        auth = Firebase.auth

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        // get journalCollection collection from journalEntry
        // get currentUserId using auth
        val journalCollection = journalEntry.journalCollection
        val currentUserId = auth.currentUser!!.uid

        // create a query in noteCollection
        val query = journalCollection.whereEqualTo("uid",currentUserId).orderBy("timestamp")

        // create a firestoreRecyclerViewOption
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Journal>().setQuery(query,Journal::class.java).build()

        // add adaptor
        adaptor = RVAdaptor(recyclerViewOption)
        recyclerView.adapter = adaptor
    }

    override fun onStart() {
        super.onStart()
        adaptor.startListening()
    }

    override fun onStop() {
        super.onStop()
        adaptor.stopListening()
    }

}