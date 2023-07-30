package com.example.journalink

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JournalFeedAdapter(private val context: Context) : ListAdapter<JournalFeedData, JournalFeedAdapter.JournalViewHolder>(JournalDiffCallback()) {
    private val likedJournalsSet = mutableSetOf<String>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    init {
        // Retrieve the liked journals of the current user from Firebase Realtime Database
        val uid = currentUser?.uid
        if (uid != null) {
            val userLikedJournalsRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("likedJournals")
            userLikedJournalsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    likedJournalsSet.clear()
                    for (journalSnapshot in snapshot.children) {
                        val journalId = journalSnapshot.key
                        if (!journalId.isNullOrEmpty()) {
                            likedJournalsSet.add(journalId)
                        }
                    }
                    notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error, if necessary
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = getItem(position)
        holder.bind(journal)

        if (journal.id in likedJournalsSet) {
            holder.likeButton.setImageResource(R.drawable.ic_liked) // Set the liked icon
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_unliked) // Set the unliked icon
        }

        holder.likeButton.setOnClickListener {
            if (journal.id in likedJournalsSet) {
                // The user has already liked this journal, so unlike it
                journal.likedByUser = false
                likedJournalsSet.remove(journal.id)
                journal.likes--

                // Update the like button icon to unliked
                holder.likeButton.setImageResource(R.drawable.ic_unliked)
            } else {
                // The user has not liked this journal, so like it
                journal.likedByUser = true
                likedJournalsSet.add(journal.id)
                journal.likes++

                // Update the like button icon to liked
                holder.likeButton.setImageResource(R.drawable.ic_liked)
            }

            // Update the likes count in the Firebase Realtime Database
            val likesRef = FirebaseDatabase.getInstance().getReference("shared_journals").child(journal.id).child("likes")
            likesRef.setValue(journal.likes)

            holder.likesTextView.text = journal.likes.toString()

            // Update the liked journals node for the current user in the Firebase Realtime Database
            val uid = currentUser?.uid
            if (uid != null) {
                val userLikedJournalsRef =
                    FirebaseDatabase.getInstance().getReference("users").child(uid).child("likedJournals")
                if (journal.likedByUser) {
                    // If the journal is liked, add it to the likedJournals node
                    userLikedJournalsRef.child(journal.id).setValue(true)
                } else {
                    // If the journal is unliked, remove it from the likedJournals node
                    userLikedJournalsRef.child(journal.id).removeValue()
                }
            }

            // Send the like notification to the user who shared the journal
            sendLikeNotificationToUser(journal.uid)
        }
    }

    private fun sendLikeNotificationToUser(uid: String) {
        // ... Your code to send the like notification ...
        // You can use the `sendNotificationToUser` function from the previous response to send the notification.
        // Make sure to replace "YOUR_SERVER_KEY" with your actual Firebase server key.
        // Example:
        sendNotificationToUser(uid, "Your Journal received a Like", "Someone liked your journal.")
    }

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val likeButton: ImageView = itemView.findViewById(R.id.heartIcon)
        private val commentButton: ImageButton = itemView.findViewById(R.id.commentIcon)
        private val titleTextView: TextView = itemView.findViewById(R.id.journalTitle)
        private val descTextView: TextView = itemView.findViewById(R.id.shortDesc)
        private val dateTextView: TextView = itemView.findViewById(R.id.timeStamp)
        val likesTextView: TextView = itemView.findViewById(R.id.heartCounter)
        private val commentsTextView: TextView = itemView.findViewById(R.id.commentCounter)

        fun bind(journal: JournalFeedData) {
            titleTextView.text = journal.title
            descTextView.text = journal.shortDescription
            dateTextView.text = journal.date
            likesTextView.text = journal.likes.toString()
            commentsTextView.text = journal.comments.toString()

            if (journal.likedByUser) {
                likeButton.setImageResource(R.drawable.ic_liked) // Set the liked icon
            } else {
                likeButton.setImageResource(R.drawable.ic_unliked) // Set the unliked icon
            }

            itemView.setOnClickListener {
                // Open ActivityJournalContentViewer and pass journal data
                val intent = Intent(context, JournalContentViewer::class.java)
                intent.putExtra("title", journal.title)
                intent.putExtra("content", journal.content)
                intent.putExtra("journalId", journal.id)
                context.startActivity(intent)

            }
            commentButton.setOnClickListener{
                val intent = Intent(context, JournalThoughts::class.java)
                intent.putExtra("journalId", journal.id)
                context.startActivity(intent)

            }
        }
    }

    private fun sendNotificationToUser(userId: String, title: String, message: String) {
        // Replace "YOUR_SERVER_KEY" with your actual Firebase server key from the Firebase Console
        val serverKey = "AAAA8df_XrM:APA91bF9l4HesLw0Ym9niQEkALVJZTODe4_qGtVZ2FNYZ0jfpBM0dk2bzfRa0zAciU4VijPNooXnLvLiZJOjw4yvUlTP87Jz_IyvTPN3Lk8ZKSecPYvoAQli9_KIPB2diF3o4zzNw3YN"

        // Retrieve the FCM token of the user from the "tokens" node in Firebase Realtime Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("tokens").child(userId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("MissingPermission")
            override fun onDataChange(snapshot: DataSnapshot) {
                val token = snapshot.child("token").value.toString()

                // Create a notification channel for Android Oreo and higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelId = "journalink_notification_channel"
                    val channelName = "JournaLink Notifications"
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val notificationChannel = NotificationChannel(channelId, channelName, importance)
                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(notificationChannel)
                }

                val notificationBuilder = NotificationCompat.Builder(context, "journalink_notification_channel")
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

                // Create an ID for the notification
                val notificationId = System.currentTimeMillis().toInt()

                // Show the notification
                try {
                    val notificationManagerCompat = NotificationManagerCompat.from(context)
                    notificationManagerCompat.notify(notificationId, notificationBuilder.build())
                } catch (e: SecurityException) {
                    // Handle the SecurityException here, e.g., show a toast or log the error.
                    // This can happen if the app lacks the required permission to show notifications.
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error, if necessary
            }
        })
    }

    private class JournalDiffCallback : DiffUtil.ItemCallback<JournalFeedData>() {
        override fun areItemsTheSame(oldItem: JournalFeedData, newItem: JournalFeedData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JournalFeedData, newItem: JournalFeedData): Boolean {
            return oldItem == newItem
        }
    }
}