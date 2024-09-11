package com.joo.miruni.data.repository

import android.content.SharedPreferences
import com.joo.miruni.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SharedPreferenceRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SharedPreferenceRepository {

    override suspend fun saveAlarmTime(time: String) {
        sharedPreferences.edit().putString("ALARM_TIME", time).apply()
    }

    override suspend fun getAlarmTime(): String {
        return sharedPreferences.getString("ALARM_TIME", "") ?: ""
    }
}
