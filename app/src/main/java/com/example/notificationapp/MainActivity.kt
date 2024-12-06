package com.example.notificationapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notificationapp.screens.MainScreen
import com.example.notificationapp.ui.theme.NotificationAppTheme
import com.example.notificationapp.viewmodel.NotificationViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val notificationViewModel: NotificationViewModel = viewModel()

            NotificationAppTheme(
                darkTheme = true
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MainScreen(modifier = Modifier, notificationViewModel)
                }
            }
        }
    }
}
