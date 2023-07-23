package com.example.journalink

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewJournalViewer : AppCompatActivity() {

    private lateinit var journTitle: TextView
    private lateinit var journshortDesc: TextView
    private lateinit var journContent: TextView
    private lateinit var shareBUTTON: ImageButton
    private lateinit var closeJournal: ImageButton
    private lateinit var deleteBUTTON: ImageButton
    private lateinit var editBUTTON: ImageButton

    private lateinit var journalId: String // Assuming you have the journal ID available

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal_viewer)

        initView()
        setValuesToViews()

        editBUTTON.setOnClickListener {
            val id = intent.getStringExtra("id").toString()
            val title = intent.getStringExtra("title").toString()
            openEditDialog(id, title)
        }
        shareBUTTON.setOnClickListener {
            shareJournal()
        }
        deleteBUTTON.setOnClickListener {
            deleteJournal(
                intent.getStringExtra("id").toString()
            )
        }
    }

    private fun initView() {
        journTitle = findViewById(R.id.journTitle)
        journshortDesc = findViewById(R.id.journshortDesc)
        journContent = findViewById(R.id.journContent)

        editBUTTON = findViewById(R.id.editBUTTON)
        shareBUTTON = findViewById(R.id.shareBUTTON)
        deleteBUTTON = findViewById(R.id.deleteBUTTON)

        closeJournal = findViewById(R.id.closeJournal)
        closeJournal.setOnClickListener {
            finish()
        }

    }

    private fun setValuesToViews() {
        journTitle.text = intent.getStringExtra("title")
        journshortDesc.text = intent.getStringExtra("shortDescription")
        journContent.text = intent.getStringExtra("content")

        journalId =
            intent.getStringExtra("journalId").toString() // Assuming you have the journal ID as an extra in the intent
    }

    private fun openEditDialog(id: String, title: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.edit_dialog, null)

        mDialog.setView(mDialogView)

        val titleEditdialog = mDialogView.findViewById<EditText>(R.id.titleEditdialog)
        val shortdescEditdialog = mDialogView.findViewById<EditText>(R.id.shortdescEditdialog)
        val contentEditdialog = mDialogView.findViewById<EditText>(R.id.contentEditdialog)

        val updatebtn = mDialogView.findViewById<ImageButton>(R.id.updatebtn)

        titleEditdialog.setText(intent.getStringExtra("title").toString())
        shortdescEditdialog.setText(intent.getStringExtra("shortDescription").toString())
        contentEditdialog.setText(intent.getStringExtra("content").toString())
        val id = intent.getStringExtra("id").toString()
        val uid = intent.getStringExtra("uid").toString()
        val date = getCurrentDate()
        val time = getCurrentTime()

        val alertDialog = mDialog.create()
        alertDialog.show()

        updatebtn.setOnClickListener {
            val id = intent.getStringExtra("id").toString()

            updateJournalEntry(
                id,
                titleEditdialog.text.toString(),
                shortdescEditdialog.text.toString(),
                contentEditdialog.text.toString(),
                uid,
                date,
                time,
            )

            Toast.makeText(applicationContext, "Journal Entry Updated", Toast.LENGTH_LONG).show()

            journTitle.text = titleEditdialog.text.toString()
            journshortDesc.text = shortdescEditdialog.text.toString()
            journContent.text = contentEditdialog.text.toString()

            alertDialog.dismiss()

        }
    }

    private fun updateJournalEntry(
        id: String,
        title: String,
        shortDescription: String,
        content: String,
        uid: String,
        date: String,
        time: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("journals").child(uid).child(id)
        val journalData = Journal(id, title, shortDescription, content, uid, date, time)

        dbRef.setValue(journalData)
    }

    private fun shareJournal() {
        val title = journTitle.text.toString()
        val shortDescription = journshortDesc.text.toString()
        val content = journContent.text.toString()
        val date = getCurrentDate() // Replace this with the actual date value you want to use

        // Store the shared data in Firebase Realtime Database under the specific journal ID
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("shared_journals").push()
        val data = mapOf(
            "title" to title,
            "shortDescription" to shortDescription,
            "content" to content,
            "date" to date
        )

        databaseReference.setValue(data)
            .addOnSuccessListener {
                // Data shared successfully
            }
            .addOnFailureListener {
                // Failed to share data, handle the error
            }
    }

    private fun deleteJournal(id:String){
        val uid = intent.getStringExtra("uid").toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("journals").child(uid).child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Journal Entry Removed.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ViewJournal::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Journal Entry Removal Error.", Toast.LENGTH_LONG).show()
        }
    }

    // Function to get the current date (replace this with your actual date logic)
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getCurrentTime(): String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(Date())
    }
}
