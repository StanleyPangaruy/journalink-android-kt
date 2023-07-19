package com.example.journalink

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class JournalEntry {

    private val db = FirebaseFirestore.getInstance() // get db instance
    val journalCollection = db.collection("Journals") // create journal collections
    private val auth = Firebase.auth

    fun createJournal(title: String, shortDescription: String, content: String) {
        val currentUserId = auth.currentUser!!.uid
        val journal = Journal(title, shortDescription, content, currentUserId, Calendar.getInstance().time)
        journalCollection.document().set(journal)
    }
}
