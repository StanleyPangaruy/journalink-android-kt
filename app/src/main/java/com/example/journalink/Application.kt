package com.example.journalink

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enable Firebase Database persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
