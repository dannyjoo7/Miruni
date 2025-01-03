package com.joo.miruni.service.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.joo.miruni.service.ForegroundService
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ReminderManagerUtil @Inject constructor(
    private val context: Context,
) {
    companion object {
        const val TAG = "AlarmManagerUtil"
    }

    // 초기 알람 설정
    fun setInitAlarm(deadLine: LocalDateTime?, id: Long, title: String) {
        if (deadLine != null) {
            val deadLineTime =
                deadLine.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()

            // 현재 시간
            val nowTime = System.currentTimeMillis()

            // 알람 시간
            val reminderTime: Long

            // 알람 설정
            val reminderType: ReminderType = when {
                nowTime >= deadLineTime -> {
                    return
                }

                nowTime < deadLineTime - 3600000 -> {
                    // 1시간 전 알람 설정
                    reminderTime = deadLineTime - 3600000
                    ReminderType.ONE_HOUR_BEFORE
                }

                nowTime < deadLineTime - 600000 -> {
                    // 10분 전 알람 설정
                    reminderTime = deadLineTime - 600000
                    ReminderType.TEN_MINUTES_BEFORE
                }

                nowTime < deadLineTime - 300000 -> {
                    // 5분 전 알람 설정
                    reminderTime = deadLineTime - 300000
                    ReminderType.FIVE_MINUTES_BEFORE
                }

                else -> {
                    reminderTime = deadLineTime
                    ReminderType.NOW
                }
            }

            Log.d(TAG, "리마인더 날짜 시간 : $reminderTime")

            val intent = Intent(context, ForegroundService::class.java).apply {
                putExtra("TODO_ID", id)
                putExtra("TODO_TITLE", title)
                putExtra("REMINDER_TYPE", reminderType)
                putExtra("REMINDER_TIME", reminderTime)
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
                reminderTime,
                pendingIntent
            )
        }
    }


    // 다음 알람 설정
    fun setNextAlarm(
        reminderTime: Long,
        id: Long,
        title: String,
        reminderType: ReminderType,
    ) {
        val intent = Intent(context, ForegroundService::class.java).apply {
            putExtra("TODO_ID", id)
            putExtra("TODO_TITLE", title)
            putExtra("REMINDER_TYPE", reminderType)
            putExtra("REMINDER_TIME", reminderTime)
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
            reminderTime,
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