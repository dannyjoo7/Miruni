package com.joo.miruni.domain.repository


interface SharedPreferenceRepository {
    suspend fun saveAlarmTime(time: String)
    suspend fun getAlarmTime(): String
}