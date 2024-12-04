package com.joo.miruni.service.unlock

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import com.joo.miruni.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnlockService() : Service(), Parcelable {

    private lateinit var unlockReceiver: UnlockReceiver

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate() {
        super.onCreate()
        unlockReceiver = UnlockReceiver()
        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "unlock_channel"
        createNotificationChannel(channelId)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Unlock Service")
            .setContentText("사용자 잠금 해제 감지 중...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Unlock Notifications"
            val descriptionText = "Notifications for unlock events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // BroadcastReceiver 등록
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(unlockReceiver, intentFilter)

        return START_STICKY
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

    companion object CREATOR : Parcelable.Creator<UnlockService> {
        override fun createFromParcel(parcel: Parcel): UnlockService {
            return UnlockService(parcel)
        }

        override fun newArray(size: Int): Array<UnlockService?> {
            return arrayOfNulls(size)
        }
    }
}
