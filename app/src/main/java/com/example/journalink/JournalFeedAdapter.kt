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
import com.google.firebase.database.FirebaseDatabase

class JournalFeedAdapter(private val context: Context) : ListAdapter<JournalFeedData, JournalFeedAdapter.JournalViewHolder>(JournalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = getItem(position)
        holder.bind(journal)
        holder.likeButton.setOnClickListener {
            // Increment likes count and update in the Firebase Realtime Database
            val likesRef = FirebaseDatabase.getInstance().getReference("shared_journals").child(journal.id).child("likes")
            journal.likes++
            likesRef.setValue(journal.likes)
        }
        holder.commentButton.setOnClickListener {
            // Open the journal details page for comments
            val intent = Intent(holder.itemView.context, JournalContentViewer::class.java)
            intent.putExtra("journalId", journal.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val likeButton: ImageView = itemView.findViewById(R.id.heartIcon)
        val commentButton: ImageView = itemView.findViewById(R.id.commentIcon)
        private val titleTextView: TextView = itemView.findViewById(R.id.journalTitle)
        private val descTextView: TextView = itemView.findViewById(R.id.shortDesc)
        private val dateTextView: TextView = itemView.findViewById(R.id.timeStamp)
        private val likesTextView: TextView = itemView.findViewById(R.id.heartCounter)
        private val commentsTextView: TextView = itemView.findViewById(R.id.commentCounter)

        fun bind(journal: JournalFeedData) {
            titleTextView.text = journal.title
            descTextView.text = journal.shortDescription
            dateTextView.text = journal.date
            likesTextView.text = journal.likes.toString()
            commentsTextView.text = journal.comments.toString()

            itemView.setOnClickListener {
                // Open ActivityJournalContentViewer and pass journal data
                val intent = Intent(context, JournalContentViewer::class.java)
                intent.putExtra("title", journal.title)
                intent.putExtra("content", journal.content)
                intent.putExtra("journalId", journal.id)
                context.startActivity(intent)

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