package com.example.journalink

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        }

    }

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val likeButton: ImageView = itemView.findViewById(R.id.heartIcon)
        val commentButton: ImageView = itemView.findViewById(R.id.commentIcon)
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
            likeButton.setOnClickListener {
                // Same as before

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
            }

        }
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