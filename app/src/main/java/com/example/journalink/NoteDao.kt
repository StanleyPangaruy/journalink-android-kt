package com.example.journalink


import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



class NoteDao {
    private val db = FirebaseFirestore.getInstance()
    val noteCollection = db.collection("Notes")

    private val auth = Firebase.auth

    fun addNote(text: String){
        val currentUserId = auth.currentUser!!.uid
        val note = Note(text,currentUserId)
        noteCollection.document().set(note)

    }


}