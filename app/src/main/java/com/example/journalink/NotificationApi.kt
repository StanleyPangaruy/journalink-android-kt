package com.example.journalink

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    companion object{
        const val CONTENT_TYPE = "application/json"
        const val API_KEY = "AAAA8df_XrM:APA91bF9l4HesLw0Ym9niQEkALVJZTODe4_qGtVZ2FNYZ0jfpBM0dk2bzfRa0zAciU4VijPNooXnLvLiZJOjw4yvUlTP87Jz_IyvTPN3Lk8ZKSecPYvoAQli9_KIPB2diF3o4zzNw3YN"
    }

    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}