package com.example.journalink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RVAdaptor(options: FirestoreRecyclerOptions<Journal>) : FirestoreRecyclerAdapter<Journal,RVAdaptor.RVViewHolder>(
    options
) {
    class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        val textViewTimestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        return RVViewHolder(view)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int, model: Journal) {
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.shortDescription
        holder.textViewContent.text = model.content
        holder.textViewTimestamp.text = model.timestamp.toString()
    }
}