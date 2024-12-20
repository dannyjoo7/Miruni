package com.joo.miruni.service.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.joo.miruni.service.ForegroundService
import javax.inject.Inject

class ReminderManagerUtil @Inject constructor(
    private val context: Context,
) {
    companion object {
        const val TAG = "AlarmManagerUtil"
    }

    // 푸쉬 알람 설정
    fun setExactAlarm(alarmTimeInMillis: Long, id: Int, title: String, reminderType: ReminderType) {
        val intent = Intent(context, ForegroundService::class.java).apply {
            putExtra("TODO_ID", id.toLong())
            putExtra("TODO_TITLE", title)
            putExtra("REMINDER_TYPE", reminderType)
            putExtra("REMINDER_TIME", alarmTimeInMillis)
        }

        val pendingIntent = PendingIntent.getService(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeInMillis,
            pendingIntent
        )
    }

    // 푸쉬 알람 취소
    fun cancelAlarmsForTodoItem(id: Long) {
        val intent = Intent(context, ForegroundService::class.java).apply {
            putExtra("TODO_ID", id)
        }
        val pendingIntent = PendingIntent.getService(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}