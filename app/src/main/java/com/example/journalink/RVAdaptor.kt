package com.example.journalink

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*

class RVAdapter(options: FirebaseRecyclerOptions<Journal>) :
    FirebaseRecyclerAdapter<Journal, RVAdapter.RVViewHolder>(options) {

    class RVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        return RVViewHolder(view)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int, model: Journal) {
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.shortDescription
        holder.textViewContent.text = model.content
        holder.textViewDate.text = model.date
        holder.textViewTime.text = model.time
    }
}
