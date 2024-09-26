package com.joo.miruni.data.repository

import android.content.SharedPreferences
import com.joo.miruni.domain.repository.SharedPreferenceRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class SharedPreferenceRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SharedPreferenceRepository {

    private val timeKey = "ALARM_TIME"
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("a hh시 mm분")

    override suspend fun saveAlarmTime(time: LocalTime) {
        val formattedTime = time.format(formatter)
        sharedPreferences.edit().putString(timeKey, formattedTime).apply()
    }

    override suspend fun getAlarmTime(): LocalTime? {
        val timeString = sharedPreferences.getString(timeKey, null) ?: return null
        return try {
            LocalTime.parse(timeString, formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}

