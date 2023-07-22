package com.example.journalink

import java.text.SimpleDateFormat
import java.util.*

data class JournalFeedData(
    val id: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val date: String = "",
    val uid: String = "",
    val content: String = "",
    val time: String = ""
) {
    constructor(id: String, title: String, shortDescription: String, content: String, uid: String) : this(
        id,
        title,
        shortDescription,
        content,
        uid,
//        getCurrentDate(),
        getCurrentTime()
    )

    companion object {
//        private fun getCurrentDate(): String {
//            val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
//            return format.format(Date())
//        }

        private fun getCurrentTime(): String {
            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return format.format(Date())
        }
    }
}