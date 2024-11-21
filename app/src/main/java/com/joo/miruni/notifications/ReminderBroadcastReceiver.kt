package com.joo.miruni.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TODO_TITLE") ?: "할 일 알림"
        val alarmType = intent.getSerializableExtra("ALARM_TYPE") as? AlarmType

        val message = when (alarmType) {
            AlarmType.ONE_HOUR_BEFORE -> "1시간 전 알림"
            AlarmType.TEN_MINUTES_BEFORE -> "10분 전 알림"
            AlarmType.FIVE_MINUTES_BEFORE -> "5분 전 알림"
            AlarmType.NOW -> "시간이 되었습니다!"
            else -> "알림이 설정되었습니다."
        }

        val notificationHelper = NotificationHelper(context)
        notificationHelper.sendNotification(title, message)
    }
}
