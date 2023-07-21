package com.example.journalink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter(private val journal: ArrayList<Journal>) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return journal.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = journal[position]
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.shortDescription
        holder.textViewDate.text = model.date
        holder.textViewTime.text = model.time
    }
}
