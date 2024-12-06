package com.example.notificationapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class NotificationData(
    val id: Int = 1,
    val name: String = "Hello!",
    val notificationText: String = "Hello World!",
    val time: LocalDateTime = LocalDateTime.now()
)
