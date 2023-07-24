package com.example.journalink

import ItemSpacingDecoration
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase

class JournalFeed : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var journalAdapter: JournalFeedAdapter
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_feed)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ItemSpacingDecoration(1/32)) // Adjust spacing as needed

        database = FirebaseDatabase.getInstance()

        journalAdapter = JournalFeedAdapter(this)
        recyclerView.adapter = journalAdapter

        // Fetch journals from all users
        getAllSharedJournalsFromFirebase { journals ->
            journalAdapter.submitList(journals)
        }
        val backButton = findViewById<ImageView>(R.id.backBtn)
        backButton.setOnClickListener{
            finish()
        }
    }

    // Function to fetch journals from Firebase Realtime Database for all users
    private fun getAllSharedJournalsFromFirebase(onJournalsFetched: (List<JournalFeedData>) -> Unit) {
        val ref = database.getReference("shared_journals")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val journals = mutableListOf<JournalFeedData>()

                for (journalSnapshot in snapshot.children) {
                    val journalId = journalSnapshot.key.toString()
                    val title = journalSnapshot.child("title").value.toString()
                    val shortDesc = journalSnapshot.child("shortDescription").value.toString()
                    val date = journalSnapshot.child("date").value.toString()
                    val content = journalSnapshot.child("content").value.toString()
                    val time = journalSnapshot.child("time").value.toString()
                    val likes = journalSnapshot.child("likes").getValue(Int::class.java) ?: 0
                    val comments = journalSnapshot.child("comments").getValue(Int::class.java) ?: 0

                    val journal = JournalFeedData(journalId, title, shortDesc, date, content, time, likes, comments)
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