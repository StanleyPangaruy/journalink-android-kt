package com.example.journalink

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class JournalEntry {

    private val database = FirebaseDatabase.getInstance()
    private val auth = Firebase.auth

    fun createJournal(title: String, shortDescription: String, content: String) {
        val currentUserId = auth.currentUser!!.uid
        val journalId = getJournalReference().push().key ?: ""
        val journal = Journal(journalId, title, shortDescription, content, currentUserId)
        getJournalReference().child(journalId).setValue(journal)
    }

    private fun getJournalReference(): DatabaseReference {
        val currentUserId = auth.currentUser?.uid
        return database.reference.child("journals").child(currentUserId.orEmpty())
    }
}
