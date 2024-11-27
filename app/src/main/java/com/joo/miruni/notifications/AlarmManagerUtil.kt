package com.joo.miruni.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.joo.miruni.domain.model.TodoEntity
import javax.inject.Inject

class AlarmManagerUtil @Inject constructor(
    private val context: Context,
) {
    companion object {
        const val TAG = "AlarmManagerUtil"
    }

    // 푸쉬 알람 설정
    fun setExactAlarm(alarmTimeInMillis: Long, id: Int, title: String, alarmType: AlarmType) {

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_ID", id.toLong())
            putExtra("TODO_TITLE", title)
            putExtra("ALARM_TYPE", alarmType)
        }

        Log.d(
            TAG,
            "ID: $id, alarmTimeInMillis : $alarmTimeInMillis, alarmType : $alarmType"
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)
    }

    // 푸쉬 알람 취소
    fun cancelAlarmsForTodoItem(id: Long) {

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_ID", id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}