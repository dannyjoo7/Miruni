package com.joo.miruni.di

import android.content.Context
import com.joo.miruni.service.notification.ReminderManagerUtil
import com.joo.miruni.service.unlock.UnlockReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideUnlockReceiver(): UnlockReceiver {
        return UnlockReceiver()
    }

    @Provides
    @Singleton
    fun provideReminderManagerManagerUtil(@ApplicationContext context: Context): ReminderManagerUtil {
        return ReminderManagerUtil(context)
    }
}
