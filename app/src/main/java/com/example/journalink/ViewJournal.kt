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
import com.example.journalink.databinding.ActivityViewJournalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class ViewJournal : AppCompatActivity(), Parcelable {

    private lateinit var binding: ActivityViewJournalBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var journalRef: DatabaseReference
    private lateinit var journal: ArrayList<Journal>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewJournalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.createBtn.setOnClickListener {
            val intent = Intent(this, CreateJournal::class.java)
            startActivity(intent)
        }

        val returnbtn = findViewById<ImageButton>(R.id.returnButton)
        returnbtn.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        auth = Firebase.auth

        journal = ArrayList()

        getJournalData()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ViewJournal> {
        override fun createFromParcel(parcel: Parcel): ViewJournal {
            return ViewJournal()
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
                        // Sort the list by time in descending order to display the newest entry at the top
                        journal.sortByDescending { it.date.toTimestamp() }

                        val jRVAdapter = RVAdapter(journal)
                        recyclerView.adapter = jRVAdapter

                        jRVAdapter.setOnItemClickListener(object : RVAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@ViewJournal, ViewJournalViewer::class.java)

                                //put extras
                                intent.putExtra("title", journal[position].title)
                                intent.putExtra("shortDescription", journal[position].shortDescription)
                                intent.putExtra("content", journal[position].content)
                                intent.putExtra("uid", journal[position].uid)
                                intent.putExtra("date", journal[position].date)
                                intent.putExtra("time", journal[position].time)
                                intent.putExtra("id", journal[position].id)
                                startActivity(intent)
                            }
                        })

                        recyclerView.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error, if needed
                }
            })
        }
    }

    private fun String.toTimestamp(): Long {
        val format = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        val date = format.parse(this)
        return date?.time ?: 0L
    }
}
