package com.example.journalink

import java.text.SimpleDateFormat
import java.util.*

data class JournalFeedData(
    val id: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val date: String = "",
    val content: String = "",
    var likes: Int = 0,
    var comments: Int = 0,
    var likedByUser: Boolean,
    val uid: String = ""

) {
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