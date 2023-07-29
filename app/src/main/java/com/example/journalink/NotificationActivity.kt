package com.example.journalink

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "journalink_channel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_POLICY_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val notificationTitle = intent.getStringExtra("title") ?: "Notification Title"
        val notificationMessage = intent.getStringExtra("message") ?: "Notification Message"

        if (hasNotificationPolicyPermission()) {
            showNotification(notificationTitle, notificationMessage)
        } else {
            requestNotificationPolicyPermission()
        }

        val titleTextView = findViewById<TextView>(R.id.notificationTitle)
        val messageTextView = findViewById<TextView>(R.id.notificationMessage)

        titleTextView.text = notificationTitle
        messageTextView.text = notificationMessage
    }

    private fun hasNotificationPolicyPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPolicyPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
            NOTIFICATION_POLICY_REQUEST_CODE
        )
    }

    private fun showNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        try {
            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        } catch (e: SecurityException) {
            // Handle the exception here
            requestNotificationPolicyPermission()
        }
    }
}
