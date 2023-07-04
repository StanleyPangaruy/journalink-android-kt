package com.example.journalink

import java.util.*

data class Journal(
    val title: String = "",
    val shortDescription: String = "",
    val content: String = "",
    val uid: String = "",
    val timestamp: Date = Date()
)
