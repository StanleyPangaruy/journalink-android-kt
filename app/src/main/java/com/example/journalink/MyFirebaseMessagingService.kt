package com.example.journalink

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle incoming messages here and show notifications
        if (remoteMessage.data.isNotEmpty()) {
            // Get notification data
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]

            // Show the notification
            showNotification(title ?: "", body ?: "")
        }
    }

    private fun showNotification(title: String, body: String) {
        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel_id"
            val channelName = "Default Channel"
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_foreground) // Add your notification icon
            .setAutoCancel(true)

        // Open the comments activity when the notification is tapped
        val commentsIntent = Intent(this, JournalThoughts::class.java)
        commentsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, commentsIntent, PendingIntent.FLAG_ONE_SHOT)
        notificationBuilder.setContentIntent(pendingIntent)

        // Show the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // Handle the token generation and registration here
        // You can send this token to your server or store it locally for sending notifications to this device
        // For simplicity, we'll print the token for now
        println("FCM Token: $token")
    }
}
