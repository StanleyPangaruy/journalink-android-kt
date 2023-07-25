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

    fun createComment(journalId: String, commentText: String, uid: String) {
        val commentId = getCommentReference(journalId).push().key ?: ""
        val commentObject = Comment(commentId, commentText, uid) // Correct the parameter order
        getCommentReference(journalId).child(commentId).setValue(commentObject)
    }

    private fun getCommentReference(journalId: String): DatabaseReference {
        return database.reference.child("shared_journals").child(journalId).child("comments")
    }
}
