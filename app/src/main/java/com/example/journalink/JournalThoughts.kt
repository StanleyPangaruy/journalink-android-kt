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
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

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
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val sharedByUserId = intent.getStringExtra("userId") ?: ""

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
                sendNotificationToUser(sharedByUserId)
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

    private fun sendNotificationToUser(userId: String) {
        // Here, you would use Firebase Cloud Functions or your custom server to send the notification
        // to the user with the provided `userId`.
        // In this example, we'll show how to use the Firebase Cloud Messaging HTTP API directly to send the notification.

        // Replace YOUR_SERVER_KEY with your actual server key from the Firebase Console (Settings > Cloud Messaging)
        val serverKey = "AAAA8df_XrM:APA91bF9l4HesLw0Ym9niQEkALVJZTODe4_qGtVZ2FNYZ0jfpBM0dk2bzfRa0zAciU4VijPNooXnLvLiZJOjw4yvUlTP87Jz_IyvTPN3Lk8ZKSecPYvoAQli9_KIPB2diF3o4zzNw3YN"

        // Create the notification message
        val notificationMessage = mapOf(
            "to" to "/topics/$userId",
            "data" to mapOf(
                // Customize the notification payload as needed
                "title" to "New Comment on Your Shared Journal",
                "body" to "Someone commented on your shared journal.",
                "click_action" to "OPEN_COMMENTS_ACTIVITY", // Add the action you want to perform when the user taps the notification
                // Add any additional data you want to send along with the notification
            )
        )

        // Send the notification using Firebase Cloud Messaging HTTP API
        val url = "https://fcm.googleapis.com/fcm/send"
        val headers = mapOf(
            "Authorization" to "key=$serverKey",
            "Content-Type" to "application/json"
        )

        // You can use a networking library like Retrofit or OkHttp to make the HTTP POST request.
        // For simplicity, we'll use a simple HTTPURLConnection here.
        try {
            val jsonString = JSONObject(notificationMessage).toString()
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doOutput = true
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            for ((key, value) in headers) {
                connection.setRequestProperty(key, value)
            }
            val outputStream = connection.outputStream
            outputStream.write(jsonString.toByteArray())
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Notification sent successfully
                Log.d("FCM", "Notification sent successfully.")
            } else {
                // Notification sending failed
                Log.e("FCM", "Failed to send notification. Response code: $responseCode")
            }

            connection.disconnect()
        } catch (e: Exception) {
            Log.e("FCM", "Exception while sending notification: ${e.message}")
        }
    }

}

