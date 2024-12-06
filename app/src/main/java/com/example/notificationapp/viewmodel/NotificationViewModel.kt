package com.example.notificationapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.notificationapp.data.NotificationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NotificationViewModel: ViewModel(){
    private val _listOfNotifications = MutableStateFlow(listOf<NotificationData>())
    val listOfNotifications = _listOfNotifications.asStateFlow()

    private val _isAdding = MutableStateFlow(false)
    private val _isEditing = MutableStateFlow(false)
    val isAdding = _isAdding.asStateFlow()
    val isEditing = _isEditing.asStateFlow()

    private var id: Int = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewNotification(name: String, notificationText: String, time: LocalDateTime){
        val list = _listOfNotifications.value.toMutableList()
        list.add(
            NotificationData(
                id = id++,
                name = name,
                notificationText = notificationText
            )
        )
        _listOfNotifications.value = list.toList()
    }

    fun changeAddingStatus(){
        _isAdding.value = !_isAdding.value
    }
}