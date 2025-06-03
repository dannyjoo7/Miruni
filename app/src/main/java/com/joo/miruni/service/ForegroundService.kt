package com.joo.miruni.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.joo.miruni.R
import com.joo.miruni.presentation.main.MainActivity
import com.joo.miruni.service.notification.ReminderManagerUtil
import com.joo.miruni.service.notification.ReminderType
import com.joo.miruni.service.unlock.UnlockReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundService : Service() {

    companion object {
        private val TAG = "ForegroundService"
        private const val CHANNEL_ID = "combined_channel"
        private const val CHANNEL_NAME = "Foreground Channel"
        private const val SERVICE_ID = 1
    }

    @Inject
    lateinit var reminderManagerUtil: ReminderManagerUtil

    @Inject
    lateinit var unlockReceiver: UnlockReceiver

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    // 서비스 시작
    private fun startForegroundService() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("미루니 서비스 실행 중")
            .setSmallIcon(getIcon())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(ContextCompat.getColor(this, R.color.ios_gray_calander_font))
            .setAutoCancel(true)
            .setOngoing(false)
            .build()

        startForeground(SERVICE_ID, notification)
    }

    // 메인 로직
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*
        * 리마인더 서비스
        * */
        intent?.let {
            val id = it.getLongExtra("TODO_ID", -1)
            val title = it.getStringExtra("TODO_TITLE") ?: "리마인더"
            val reminderType = it.getSerializableExtra("REMINDER_TYPE") as? ReminderType
            val reminderTime = it.getLongExtra("REMINDER_TIME", -1)
            val deadLineTime = it.getLongExtra("DEADLINE_TIME", -1)

            if (id.toInt() != -1) {
                sendReminder(id, title, reminderType)
                // 다음 알람 예약
                scheduleNextAlarm(id, title, deadLineTime, reminderType, reminderTime)
            }
        }


        /*
        * Unlock 서비스
        * */
        registerReceiver(unlockReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)
        })

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlockReceiver)
    }

    // 채널 생성
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "리마인더 및 잠금 해제 알림 채널"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }



    private fun sendReminder(id: Long, title: String, reminderType: ReminderType?) {
        val message = when (reminderType) {
            ReminderType.ONE_HOUR_BEFORE -> "1시간 전 알림"
            ReminderType.TEN_MINUTES_BEFORE -> "10분 전 알림"
            ReminderType.FIVE_MINUTES_BEFORE -> "5분 전 알림"
            ReminderType.NOW -> "시간이 되었습니다!"
            null -> "알림"
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(getIcon())
            .setColor(ContextCompat.getColor(this, R.color.ios_gray_calander_font))
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColorized(true)
            .setOngoing(false)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(id.toInt(), notification)
    }

    private fun scheduleNextAlarm(
        id: Long,
        title: String,
        deadLineTime: Long,
        reminderType: ReminderType?,
        reminderTime: Long,
    ) {
        // 다음 알람 결정
        val nextReminderType = when (reminderType) {
            ReminderType.ONE_HOUR_BEFORE -> ReminderType.TEN_MINUTES_BEFORE
            ReminderType.TEN_MINUTES_BEFORE -> ReminderType.FIVE_MINUTES_BEFORE
            ReminderType.FIVE_MINUTES_BEFORE -> ReminderType.NOW
            ReminderType.NOW -> null
            else -> null
        }

        val nextAlarmTime = calculateNextAlarmTime(nextReminderType, deadLineTime)

        if (nextReminderType != null && nextAlarmTime != null) {
            reminderManagerUtil.setNextAlarm(
                id,
                title,
                deadLineTime,
                nextAlarmTime,
                nextReminderType
            )
        } else {
            reminderManagerUtil.cancelAlarmsForTodoItem(id)
        }
    }

    private fun calculateNextAlarmTime(reminderType: ReminderType?, deadLineTime: Long): Long? {
        return when (reminderType) {
            ReminderType.ONE_HOUR_BEFORE -> deadLineTime - 3600000
            ReminderType.TEN_MINUTES_BEFORE -> deadLineTime - 600000
            ReminderType.FIVE_MINUTES_BEFORE -> deadLineTime - 300000
            ReminderType.NOW -> deadLineTime
            else -> null
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getIcon(): Int {
        return if (isSamsungDevice()) {
            R.drawable.ic_notification_samsung
        } else {
            R.drawable.ic_notification
        }
    }

    private fun isSamsungDevice(): Boolean {
        return Build.MANUFACTURER.equals("Samsung", ignoreCase = true)
    }
}
