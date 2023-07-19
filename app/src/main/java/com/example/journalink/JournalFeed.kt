package com.example.journalink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase



class JournalFeed : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var journalAdapter: JournalAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_feed)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        journalAdapter = JournalAdaptor()
        recyclerView.adapter = journalAdapter

        getAllJournalsFromFirebase { journals ->
            journalAdapter.submitList(journals)
        }
    }

    // Function to fetch all journals from Firebase Realtime Database
    private fun getAllJournalsFromFirebase(onJournalsFetched: (List<Journal>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("journals/UID")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val journals = mutableListOf<Journal>()

                for (journalSnapshot in snapshot.children) {
                    val journalId = journalSnapshot.key.toString()
                    val title = journalSnapshot.child("title").value.toString()
                    val shortDesc = journalSnapshot.child("shortDescription").value.toString()
                    val date = journalSnapshot.child("date").value.toString()

                    val journal = Journal(journalId, title, shortDesc, date)
                    journals.add(journal)
                }

                onJournalsFetched(journals)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error, if necessary
            }
        })
    }
}

