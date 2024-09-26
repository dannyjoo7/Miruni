package com.joo.miruni.domain.repository

import java.time.LocalTime


interface SharedPreferenceRepository {
    suspend fun saveAlarmTime(time: LocalTime)
    suspend fun getAlarmTime(): LocalTime?
}