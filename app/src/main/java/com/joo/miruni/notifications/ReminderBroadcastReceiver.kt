package com.joo.miruni.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "ReminderBroadcastReceiver"
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val todoId = intent.getLongExtra("TODO_ID", -1)
        val title = intent.getStringExtra("TODO_TITLE") ?: "할 일 알림"
        val alarmType = intent.getSerializableExtra("ALARM_TYPE") as? AlarmType

        val message = when (alarmType) {
            AlarmType.ONE_HOUR_BEFORE -> "1시간 전 알림"
            AlarmType.TEN_MINUTES_BEFORE -> "10분 전 알림"
            AlarmType.FIVE_MINUTES_BEFORE -> "5분 전 알림"
            AlarmType.NOW -> "시간이 되었습니다!"
            else -> "알림이 설정되었습니다."
        }

        Log.d(
            TAG,
            "title : $title, todoId : $todoId, alarmType : $alarmType"
        )

        val notificationId = todoId.toInt()
        notificationHelper.sendNotification(title, message, notificationId, alarmType)
    }
}

