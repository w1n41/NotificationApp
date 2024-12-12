import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.notificationapp.data.NotificationData
import com.example.notificationapp.screens.NotificationReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random

class NotificationViewModel : ViewModel() {

    private val _listOfNotifications = MutableStateFlow(listOf<NotificationData>())
    val listOfNotifications = _listOfNotifications.asStateFlow()

    private val _isAdding = MutableStateFlow(false)
    private val _isEditing = MutableStateFlow(false)
    val isAdding = _isAdding.asStateFlow()
    val isEditing = _isEditing.asStateFlow()

    private var id: Int = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNewNotification(name: String, notificationText: String, time: LocalDateTime) {
        val list = _listOfNotifications.value.toMutableList()
        list.add(
            NotificationData(
                id = id++,
                name = name,
                notificationText = notificationText,
                time = time
            )
        )
        _listOfNotifications.value = list.toList()
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification(
        context: Context,
        name: String,
        notificationText: String,
        time: LocalDateTime
    ) {
        val triggerTimeInMillis = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                return
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", name)
            putExtra("text", notificationText)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
    }

    fun changeAddingStatus() {
        _isAdding.value = !_isAdding.value
    }
}
