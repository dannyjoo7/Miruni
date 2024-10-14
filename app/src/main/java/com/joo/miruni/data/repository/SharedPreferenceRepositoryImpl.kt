package com.joo.miruni.data.repository

import android.content.SharedPreferences
import com.joo.miruni.domain.repository.SharedPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class SharedPreferenceRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SharedPreferenceRepository {

    companion object {
        private const val TIME_KEY = "ALARM_TIME"
        private const val VISIBILITY_KEY = "SETTING_COMPLETED_ITEMS_VISIBILITY"
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("a hh시 mm분")
    }

    private val _settingCompletedItemsVisibility = MutableStateFlow(getCompletedItemsVisibility())
    val settingCompletedItemsVisibility: StateFlow<Boolean> get() = _settingCompletedItemsVisibility

    // 가시성 상태를 반환하는 메소드
    private fun getCompletedItemsVisibility(): Boolean {
        return sharedPreferences.getBoolean(VISIBILITY_KEY, true)
    }

    override suspend fun saveAlarmTime(time: LocalTime) {
        val formattedTime = time.format(FORMATTER)
        sharedPreferences.edit().putString(TIME_KEY, formattedTime).apply()
    }

    override suspend fun getAlarmTime(): LocalTime? {
        val timeString = sharedPreferences.getString(TIME_KEY, null) ?: return null
        return try {
            LocalTime.parse(timeString, FORMATTER)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    // 완료된 할 일 가시성 토글
    override suspend fun settingUpdateCompletedItemsVisibility() {
        val currentVisibility = getCompletedItemsVisibility()
        val newVisibility = !currentVisibility
        sharedPreferences.edit().putBoolean(VISIBILITY_KEY, newVisibility).apply()
        _settingCompletedItemsVisibility.value = newVisibility
    }

    override fun observeSettingCompletedItemsVisibility(): Flow<Boolean> =
        settingCompletedItemsVisibility
}




