package com.joo.miruni.service.unlock

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.joo.miruni.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnlockService() : Service(), Parcelable {

    private lateinit var unlockReceiver: UnlockReceiver

    companion object {
        private const val CHANNEL_ID = "unlock_channel"
        private const val CHANNEL_NAME = "Unlock Notifications"
        private const val SERVICE_ID = 1

        @JvmField
        val CREATOR: Parcelable.Creator<UnlockService> =
            object : Parcelable.Creator<UnlockService> {
                override fun createFromParcel(parcel: Parcel): UnlockService {
                    return UnlockService(parcel)
                }

                override fun newArray(size: Int): Array<UnlockService?> {
                    return arrayOfNulls(size)
                }
            }
    }

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate() {
        super.onCreate()
        unlockReceiver = UnlockReceiver()
        startForegroundService()
    }

    private fun startForegroundService() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("리마인더 서비스 실행중")
            .setSmallIcon(getIcon())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(ContextCompat.getColor(this, R.color.ios_gray_calander_font))
            .setAutoCancel(true)
            .setOngoing(false)
            .build()

        startForeground(SERVICE_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "사용자가 잠금 해제를 했는지 판단하는 서비스"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // BroadcastReceiver 등록
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)
//            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(unlockReceiver, intentFilter)

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlockReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
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
