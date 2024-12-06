package com.example.notificationapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationapp.R
import com.example.notificationapp.data.NotificationData
import com.example.notificationapp.ui.theme.DarkColorScheme
import com.example.notificationapp.viewmodel.NotificationViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier, notificationViewModel: NotificationViewModel) {
    Scaffold(
        topBar = {
            MainTopBar()
        },
        content = { innerPadding ->
            MainContent(modifier.padding(innerPadding), notificationViewModel)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(modifier: Modifier = Modifier, notificationViewModel: NotificationViewModel) {

    val listOfNotifications = notificationViewModel.listOfNotifications.collectAsState()

    val isAdding = notificationViewModel.isAdding.collectAsState()

    if (isAdding.value) {
        NotificationAddDialog(
            notificationViewModel = notificationViewModel,
            onDismiss = { notificationViewModel.changeAddingStatus() }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        DarkColorScheme.tertiary,
                        DarkColorScheme.background
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.padding(12.dp))
        Text(
            text = "What notification do you need today?",
            color = Color.White
        )
        Spacer(Modifier.padding(8.dp))
        Row(
            modifier = modifier.height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                DarkColorScheme.secondary,
                                DarkColorScheme.background
                            )
                        ),
                        shape = ButtonDefaults.shape
                    )
                    .shadow(
                        elevation = 20.dp
                    ),
                onClick = {
                    notificationViewModel.changeAddingStatus()
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(
                    text = "Add notification",
                    color = Color.White
                )
            }
        }
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .height(550.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(listOfNotifications.value) { notification ->
                NotificationDataItems(notification)
                Spacer(Modifier.padding(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                listOf(
                    DarkColorScheme.background,
                    DarkColorScheme.tertiary
                )
            )
        ),
        title = {
            Text(
                text = "Notification creator",
                color = Color.White
            )
        },
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = Color.White
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationDataItems(notificationData: NotificationData) {

    val context = LocalContext.current

    var builder = NotificationCompat.Builder(context, "NotificationApp")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(notificationData.name)
        .setContentText(notificationData.notificationText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

//    val notificationManager = NotificationManagerCompat.from(context)
//    notificationManager.

    Card(
        modifier = Modifier
            .clip(shape = CardDefaults.shape)
            .height(100.dp)
            .width(300.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        DarkColorScheme.tertiary,
                        DarkColorScheme.background
                    )
                )
            )
            .border(
                color = Color.White,
                width = 1.dp,
                shape = CardDefaults.shape
            ),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .height(100.dp)
                .width(150.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Spacer(
                    Modifier
                        .padding(8.dp)
                        .width(5.dp)
                )
                Text(
                    text = notificationData.name,
                    color = Color.White
                )
            }
            Row {
                Spacer(
                    Modifier
                        .padding(8.dp)
                        .width(5.dp)
                )
                Text(
                    text = notificationData.notificationText,
                    color = Color.White
                )
            }
            Row {
                Spacer(
                    Modifier
                        .padding(8.dp)
                        .width(5.dp)
                )
                Text(
                    text = notificationData.time.toString(),
                    color = Color.White
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationAddDialog(
    notificationViewModel: NotificationViewModel,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var notificationText by remember { mutableStateOf("") }
    var time by remember { mutableStateOf(LocalDateTime.now()) }

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkColorScheme.tertiary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create notification", color = Color.White)

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    )
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = notificationText,
                    onValueChange = {
                        notificationText = it
                    },
                    label = {
                        Text("Notification text", color = Color.White)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    )
                )

                Spacer(Modifier.height(16.dp))

                TimePicker(
                    onConfirm = { timePickerState ->
                        time = LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(timePickerState.hour, timePickerState.minute)
                        )
                    },
                    onDismiss = {}
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank() && notificationText.isNotBlank()) {
                                notificationViewModel.addNewNotification(
                                    name = name,
                                    notificationText = notificationText,
                                    time = time
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkColorScheme.secondary)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    Column {
        TimeInput(
            state = timePickerState
        )
        Row {
            Button(
                onClick = {
                    onConfirm(timePickerState)
                }
            ) {
                Text(
                    "Pick time"
                )
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    "Cancel"
                )
            }
        }
    }
}
