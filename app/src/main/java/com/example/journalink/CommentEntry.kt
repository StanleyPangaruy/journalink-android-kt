package com.example.journalink

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentEntry {

    private val database = FirebaseDatabase.getInstance()
    private val auth = Firebase.auth

    fun createComment(journalId: String, comment: String) {
        val currentUserId = auth.currentUser?.uid ?: return
        val commentId = getCommentReference(journalId).push().key ?: ""
        val currentDate = getCurrentDate()
        val currentTime = getCurrentTime()
        val commentObject = Comment(commentId, comment, currentDate, currentTime, currentUserId)
        getCommentReference(journalId).child(commentId).setValue(commentObject)
    }

    private fun getCommentReference(journalId: String): DatabaseReference {
        return database.reference.child("shared_journals").child(journalId).child("comments")
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getCurrentTime(): String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(Date())
    }
}
