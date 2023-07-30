package com.example.journalink

class Repository {

    private val apiManager: ApiManager = ApiManager()

    suspend fun sendNotification(notification: PushNotification) {
        apiManager.postNotification(notification)
    }

}