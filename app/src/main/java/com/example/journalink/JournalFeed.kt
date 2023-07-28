package com.example.journalink

import ItemSpacingDecoration
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.Locale

class JournalFeed : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var journalAdapter: JournalFeedAdapter
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_feed)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ItemSpacingDecoration(1/32)) // Adjust spacing as needed

        database = FirebaseDatabase.getInstance()

        journalAdapter = JournalFeedAdapter(this)
        recyclerView.adapter = journalAdapter

        // Fetch journals from all users
        getAllSharedJournalsFromFirebase { journals ->
            // Sort journals by date in ascending order before reversing the list
            val sortedJournals = journals.sortedBy { it.date.toTimestamp() }.reversed()
            journalAdapter.submitList(sortedJournals)
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
                    val likes = journalSnapshot.child("likes").getValue(Int::class.java) ?: 0
                    val likedByUser = journalSnapshot.child("subscribed").getValue(Boolean::class.java) ?: false
                    val commentCount = journalSnapshot.child("comments").childrenCount.toInt()

                    val journal = JournalFeedData(
                        journalId, title, shortDesc, date, content,
                        likes, commentCount, likedByUser // Include the comment count here
                    )
                    journals.add(journal)
                }

                onJournalsFetched(journals)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error, if necessary
            }
        })
    }
    // Extension function to convert date string to timestamp
    private fun String.toTimestamp(): Long {
        val format = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        val date = format.parse(this)
        return date?.time ?: 0L
    }
}
