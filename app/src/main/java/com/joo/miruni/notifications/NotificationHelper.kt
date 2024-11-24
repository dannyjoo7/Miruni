package com.joo.miruni.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.joo.miruni.R
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    private val context: Context,
    private val alarmManagerUtil: AlarmManagerUtil,
) {
    private val channelId = "reminder_channel"
    private val channelName = "Reminder Notifications"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(channelId) == null) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH,
            )
            manager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(
        title: String,
        message: String,
        notificationId: Int,
        alarmType: AlarmType?,
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 알림 전송
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(getIcon())
                .setColor(ContextCompat.getColor(context, R.color.ios_gray_calander_font))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColorized(true)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, notificationBuilder.build())

            // 다음 알람 결정
            val nextAlarmType = when (alarmType) {
                AlarmType.ONE_HOUR_BEFORE -> AlarmType.TEN_MINUTES_BEFORE
                AlarmType.TEN_MINUTES_BEFORE -> AlarmType.FIVE_MINUTES_BEFORE
                AlarmType.FIVE_MINUTES_BEFORE -> AlarmType.NOW
                AlarmType.NOW -> null
                else -> null
            }
            val nextAlarmTime = calculateAlarmTime(nextAlarmType)

            if (alarmType != null && nextAlarmType != null && nextAlarmTime != null) {
                alarmManagerUtil.setExactAlarm(nextAlarmTime, notificationId, title, nextAlarmType)
            }
        } else {
            Log.e("Notification Error", "알림 권한이 부여되지 않았습니다.")
        }
    }

    // 시간 계산 메서드
    private fun calculateAlarmTime(alarmType: AlarmType?): Long? {
        return when (alarmType) {
            AlarmType.ONE_HOUR_BEFORE -> System.currentTimeMillis() + 3600000
            AlarmType.TEN_MINUTES_BEFORE -> System.currentTimeMillis() + 600000
            AlarmType.FIVE_MINUTES_BEFORE -> System.currentTimeMillis() + 300000
            AlarmType.NOW -> System.currentTimeMillis() + 300000
            else -> null
        }
    }

    // 삼성 디바이스는 png 사용
    private fun getIcon(): Int {
        return if (isSamsungDevice()) {
            R.drawable.ic_notification_samsung
        } else {
            R.drawable.ic_notification
        }
    }

    // 삼성 디바이스 확인
    private fun isSamsungDevice(): Boolean {
        return Build.MANUFACTURER.equals("Samsung", ignoreCase = true)
    }
}

