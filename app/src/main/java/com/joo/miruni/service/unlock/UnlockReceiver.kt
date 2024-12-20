package com.joo.miruni.service.unlock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.joo.miruni.presentation.unlock.UnlockActivity

class UnlockReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "UnlockReceiver"
        private const val CHANNEL_ID = "unlock_channel"
        private const val CHANNEL_NAME = "Unlock Notifications"
        private const val SERVICE_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_USER_PRESENT) {
            startUnlockActivity(context)
        }
    }

    private fun startUnlockActivity(context: Context) {
        context.startActivity(
            Intent(context, UnlockActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
        )
    }
}
