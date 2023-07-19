package com.example.journalink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class JournalAdaptor : ListAdapter<Journal, JournalAdaptor.JournalViewHolder>(JournalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_journal_feed, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = getItem(position)
        holder.bind(journal)
    }

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descTextView: TextView = itemView.findViewById(R.id.descTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(journal: Journal) {
            titleTextView.text = journal.title
            descTextView.text = journal.content.toString()

            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val timestamp = Date(journal.timestamp)
            dateTextView.text = dateFormat.format(timestamp)
        }
    }

    private class JournalDiffCallback : DiffUtil.ItemCallback<Journal>() {
        override fun areItemsTheSame(oldItem: Journal, newItem: Journal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Journal, newItem: Journal): Boolean {
            return oldItem == newItem
        }
    }
}
