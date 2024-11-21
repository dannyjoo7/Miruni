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

class NotificationHelper(private val context: Context) {
    private val channelId = "reminder_channel"
    private val channelName = "Reminder Notifications"
    private var notificationIdCounter = 0

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

    fun sendNotification(title: String, message: String) {
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
            notificationManager.notify(notificationIdCounter++, notificationBuilder.build())
        } else {
            Log.e("Notification Error", "알림 권한이 부여되지 않았습니다.")
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

