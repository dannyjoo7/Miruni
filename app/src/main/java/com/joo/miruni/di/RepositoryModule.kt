package com.joo.miruni.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.joo.miruni.data.database.AppDatabase
import com.joo.miruni.data.database.TaskDao
import com.joo.miruni.data.repository.SharedPreferenceRepositoryImpl
import com.joo.miruni.data.repository.TaskRepositoryImpl
import com.joo.miruni.domain.repository.SharedPreferenceRepository
import com.joo.miruni.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    /*
    * SharedPreferences
    * */
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


    /*
    * RoomDB
    * */
    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao,
    ): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

}


