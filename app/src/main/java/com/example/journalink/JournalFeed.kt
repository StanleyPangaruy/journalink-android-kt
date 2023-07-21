package com.example.journalink

import ItemSpacingDecoration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase

class JournalFeed : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var journalAdapter: JournalAdaptor
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_feed)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ItemSpacingDecoration(1/32)) // Adjust spacing as needed

        database = FirebaseDatabase.getInstance()

        journalAdapter = JournalAdaptor()
        recyclerView.adapter = journalAdapter

        // Fetch journals from all users
        getAllUserJournalsFromFirebase { journals ->
            journalAdapter.submitList(journals)
        }
    }

    // Function to fetch journals from Firebase Realtime Database for all users
    private fun getAllUserJournalsFromFirebase(onJournalsFetched: (List<Journal>) -> Unit) {
        val ref = database.getReference("journals")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val journals = mutableListOf<Journal>()

                for (userSnapshot in snapshot.children) {
                    for (journalSnapshot in userSnapshot.children) {
                        val journalId = journalSnapshot.key.toString()
                        val title = journalSnapshot.child("title").value.toString()
                        val shortDesc = journalSnapshot.child("shortDescription").value.toString()
                        val date = journalSnapshot.child("date").value.toString()
                        val uid = journalSnapshot.child("uid").value.toString()
                        val content = journalSnapshot.child("content").value.toString()

                        val journal = Journal(journalId, title, shortDesc, date, uid, content)
                        journals.add(journal)
                    }
                }

                onJournalsFetched(journals)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error, if necessary
            }
        })
    }
}

