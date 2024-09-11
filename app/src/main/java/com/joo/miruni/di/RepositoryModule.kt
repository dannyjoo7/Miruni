package com.joo.miruni.di

import android.content.Context
import android.content.SharedPreferences
import com.joo.miruni.data.repository.SharedPreferenceRepositoryImpl
import com.joo.miruni.domain.repository.SharedPreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("settings_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceRepository(sharedPreferences: SharedPreferences): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImpl(sharedPreferences)
    }
}


