package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ViewJournal() : AppCompatActivity(), Parcelable {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var journalRef: DatabaseReference
    private lateinit var journal: ArrayList<Journal>

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        val returnbtn = findViewById<ImageButton>(R.id.returnButton)
        returnbtn.setOnClickListener {
            val intent = Intent (this, HomePage:: class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        auth = Firebase.auth

        journal = arrayListOf<Journal>()

        getJournalData()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ViewJournal> {
        override fun createFromParcel(parcel: Parcel): ViewJournal {
            return ViewJournal(parcel)
        }

        override fun newArray(size: Int): Array<ViewJournal?> {
            return arrayOfNulls(size)
        }
    }

    private fun getJournalData() {
        recyclerView.visibility = View.GONE

        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        if (uid != null) {
            journalRef = FirebaseDatabase.getInstance().getReference("journals").child(uid)
            
            journalRef.orderByChild("time").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    journal.clear()
                    if (snapshot.exists()) {
                        for (journalSnap in snapshot.children) {
                            val journalData = journalSnap.getValue(Journal::class.java)
                            journal.add(journalData!!)
                        }
                        // Reverse the list to display the newest entry at the top
                        journal.reverse()

                        val jRVAdapter = RVAdapter(journal)
                        recyclerView.adapter = jRVAdapter

                        recyclerView.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }
}
