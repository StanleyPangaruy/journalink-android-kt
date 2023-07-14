package com.example.journalink

import java.text.SimpleDateFormat
import java.util.*

data class Journal(
    val id: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val content: String = "",
    val uid: String = ""
) {
    val date: String
        get() {
            val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            return format.format(Date())
        }

    val time: String
        get() {
            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return format.format(Date())
        }
}

