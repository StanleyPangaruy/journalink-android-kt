package com.example.journalink

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Comment(
    val id: String = "",
    val comment: String = "",
    val uid: String = "",
    val date: String = "",
    val time: String = ""
) {
    constructor(id: String, comment: String, uid: String) : this(
        id,
        comment,
        uid,
        getCurrentDate(),
        getCurrentTime()
    )

    companion object {
        private fun getCurrentDate(): String {
            val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            return format.format(Date())
        }

        private fun getCurrentTime(): String {
            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return format.format(Date())
        }
    }
}
