package com.joo.miruni.service.unlock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.joo.miruni.R

class UnlockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_USER_PRESENT) {
            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        val channelId = "unlock_channel"

        // 알림 생성
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("잠금 해제됨")
            .setContentText("사용자가 화면을 잠금 해제했습니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
