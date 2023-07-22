package com.example.journalink

import ItemSpacingDecoration
import android.os.Bundle
import android.widget.ImageView
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
        getAllSharedJournalsFromFirebase { journals ->
            journalAdapter.submitList(journals)
        }
        val backButton = findViewById<ImageView>(R.id.backBtn)
        backButton.setOnClickListener{
            finish()
        }
    }

    // Function to fetch journals from Firebase Realtime Database for all users
    private fun getAllSharedJournalsFromFirebase(onJournalsFetched: (List<Journal>) -> Unit) {
        val ref = database.getReference("shared_journals")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val journals = mutableListOf<Journal>()

                for (journalSnapshot in snapshot.children) {
                    val journalId = journalSnapshot.key.toString()
                    val title = journalSnapshot.child("title").value.toString()
                    val shortDesc = journalSnapshot.child("shortDescription").value.toString()
                    val date = journalSnapshot.child("date").value.toString()
                    val content = journalSnapshot.child("content").value.toString()

                    val journal = Journal(journalId, title, shortDesc, date, content)
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

