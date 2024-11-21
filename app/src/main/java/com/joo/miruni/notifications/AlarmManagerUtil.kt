package com.joo.miruni.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.joo.miruni.domain.model.TodoEntity
import javax.inject.Inject

class AlarmManagerUtil @Inject constructor(
    private val context: Context,
) {

    // 푸쉬 알람 설정
    fun setExactAlarm(alarmTimeInMillis: Long, todoEntity: TodoEntity, alarmType: AlarmType) {

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("TODO_ID", todoEntity.id)
            putExtra("TODO_TITLE", todoEntity.title)
            putExtra("ALARM_TYPE", alarmType)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            todoEntity.id.hashCode() + alarmType.ordinal,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)
    }

    // 푸쉬 알람 취소
    fun cancelAllAlarmsForTodoItem(todoEntity: TodoEntity) {

        val alarmTypes = listOf(
            AlarmType.ONE_HOUR_BEFORE,
            AlarmType.TEN_MINUTES_BEFORE,
            AlarmType.FIVE_MINUTES_BEFORE,
            AlarmType.NOW
        )

        for (alarmType in alarmTypes) {
            val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
                putExtra("TODO_ID", todoEntity.id)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                todoEntity.id.hashCode() + alarmType.ordinal,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

    // TODO Test
    fun setTestAlarm() {
        val alarmTimeInMillis = System.currentTimeMillis()

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)
    }
}