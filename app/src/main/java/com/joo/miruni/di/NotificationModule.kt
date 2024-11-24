package com.joo.miruni.di

import android.content.Context
import com.joo.miruni.notifications.AlarmManagerUtil
import com.joo.miruni.notifications.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {

    @Provides
    fun provideNotificationHelper(
        @ApplicationContext context: Context,
        alarmManagerUtil: AlarmManagerUtil
    ): NotificationHelper {
        return NotificationHelper(context, alarmManagerUtil)
    }
}


