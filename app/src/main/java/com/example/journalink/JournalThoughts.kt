package com.example.journalink

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalThoughts : AppCompatActivity() {

    private var CommentsList: RecyclerView? = null
    private var CommentInputText: EditText? = null
    private var PostsRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var Post_Key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_thoughts)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val sharedByUserId = intent.getStringExtra("uid") ?: ""

        val closeComments = findViewById<ImageButton>(R.id.xbutton)
        closeComments.setOnClickListener {
            finish()
        }

        CommentsList = findViewById(R.id.recyclerView)
        CommentInputText = findViewById(R.id.commentBox)
        val postCommentButton = findViewById<ImageButton>(R.id.imageButton2)

        Post_Key = intent.getStringExtra("journalId")

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        CommentsList?.layoutManager = linearLayoutManager

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser
        val currentUserId = currentUser?.uid ?: ""

        PostsRef =
            FirebaseDatabase.getInstance("https://journalink-a083a-default-rtdb.firebaseio.com/")
                .reference.child("shared_journals").child(Post_Key!!).child("comments")

        postCommentButton.setOnClickListener {
            val commentText = CommentInputText!!.text.toString()
            if (commentText.isNotEmpty()) {
                val commentEntry = CommentEntry()
                commentEntry.createComment(Post_Key!!, commentText, currentUserId)

                Toast.makeText(
                    this@JournalThoughts,
                    "You have commented successfully",
                    Toast.LENGTH_SHORT
                ).show()

                CommentInputText?.setText("")
                sendNotificationToUser(sharedByUserId)
            } else {
                Toast.makeText(this@JournalThoughts, "Please write a comment", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val options: FirebaseRecyclerOptions<Comment> =
            FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(PostsRef!!, Comment::class.java)
                .build()

        val adapter = CommentAdapter(options)
        adapter.startListening()
        CommentsList!!.adapter = adapter
    }

    private fun sendNotificationToUser(sharedByUserId: String) {
        val notificationData = NotificationData(
            title = "New Comment on Your Shared Journal",
            message = "Someone commented on your shared journal."
        )

        val notification = PushNotification(
            data = notificationData,
            to = "/topics/$sharedByUserId"
        )

        // Call the repository to send the notification using Retrofit
        val repository = Repository()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.sendNotification(notification)
            } catch (e: Exception) {
                Log.e("FCM", "Exception while sending notification: ${e.message}")
            }
        }
    }
}


