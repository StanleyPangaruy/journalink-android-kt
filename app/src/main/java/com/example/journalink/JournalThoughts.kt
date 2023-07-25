package com.example.journalink

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JournalThoughts : AppCompatActivity() {

    private var CommentsList: RecyclerView? = null
    private var CommentInputText: EditText? = null
    private var PostsRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var Post_Key: String? = null
    private lateinit var closeComments: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_thoughts)

        closeComments = findViewById(R.id.xbutton)
        closeComments.setOnClickListener {
            finish()
        }

        // Initialize the necessary views and variables
        CommentsList = findViewById(R.id.recyclerView)
        CommentInputText = findViewById(R.id.commentBox)
        val postCommentButton = findViewById<ImageButton>(R.id.imageButton2)

        // Get the selected journal's ID from the intent
        Post_Key = intent.getStringExtra("journalId")

        // Set up the RecyclerView to display comments
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        CommentsList?.layoutManager = linearLayoutManager

        // Get the current user ID from FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser
        val currentUserId = currentUser?.uid ?: "" // Get the current user's UID or an empty string for anonymous comments

        // Get the reference to the selected journal's comments node in Firebase
        PostsRef = FirebaseDatabase.getInstance("https://journalink-a083a-default-rtdb.firebaseio.com/")
            .reference.child("shared_journals").child(Post_Key!!).child("comments")

        // Set a click listener for the "Save" button to post a new comment
        postCommentButton.setOnClickListener {
            val commentText = CommentInputText!!.text.toString()
            if (commentText.isNotEmpty()) {
                // Create a new comment using the CommentEntry class
                val commentEntry = CommentEntry()
                commentEntry.createComment(Post_Key!!, commentText, currentUserId)

                // Show a toast message if the comment is posted successfully
                Toast.makeText(
                    this@JournalThoughts,
                    "You have commented successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Clear the comment input text
                CommentInputText?.setText("")
            } else {
                // Show a toast message if the comment is empty
                Toast.makeText(this@JournalThoughts, "Please write a comment", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the FirebaseRecyclerAdapter to display comments in the RecyclerView
        val options: FirebaseRecyclerOptions<Comment> =
            FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(PostsRef!!, Comment::class.java)
                .build()

        val adapter = CommentAdapter(options)
        adapter.startListening()
        CommentsList!!.adapter = adapter
    }
}

