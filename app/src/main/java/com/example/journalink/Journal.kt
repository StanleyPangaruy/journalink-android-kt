package com.example.journalink

import java.text.SimpleDateFormat
import java.util.*

data class Journal(
    val id: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val content: String = "",
    val uid: String = "",
    val timestamp: Date = Date()
) {
    val date: String
        get() {
            val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            return format.format(timestamp)
        }

    val time: String
        get() {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            return format.format(timestamp)
        }
}
