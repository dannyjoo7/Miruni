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
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("a hh시 mm분")

        private const val TIME_KEY = "ALARM_TIME"
        private const val COMPLETE_VISIBILITY_KEY = "SETTING_COMPLETED_ITEMS_VISIBILITY"
        private const val UNLOCK_STATE_KEY = "SETTING_UNLOCK_STATE"
    }

    private val _settingCompletedItemsVisibility = MutableStateFlow(getCompletedItemsVisibility())
    private val settingCompletedItemsVisibility: StateFlow<Boolean> get() = _settingCompletedItemsVisibility

    private val _settingUnlockState = MutableStateFlow(getUnlockState())
    private val settingUnlockState: StateFlow<Boolean> get() = _settingUnlockState

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

    // 가시성 상태를 반환 메소드
    private fun getCompletedItemsVisibility(): Boolean {
        return sharedPreferences.getBoolean(COMPLETE_VISIBILITY_KEY, true)
    }

    // 잠금화면 활성화 상태 반환 메소드
    private fun getUnlockState(): Boolean {
        return sharedPreferences.getBoolean(UNLOCK_STATE_KEY, true)
    }

    // 완료된 할 일 가시성 토글
    override suspend fun settingUpdateCompletedItemsVisibility() {
        val currentVisibility = getCompletedItemsVisibility()
        val newVisibility = !currentVisibility
        sharedPreferences.edit().putBoolean(COMPLETE_VISIBILITY_KEY, newVisibility).apply()
        _settingCompletedItemsVisibility.value = newVisibility
    }

    // 잠금 화면 표시 설정 토글
    override suspend fun settingActiveUnlockScreen() {
        val currentState = getUnlockState()
        val newState = !currentState
        sharedPreferences.edit().putBoolean(UNLOCK_STATE_KEY, newState).apply()
        _settingUnlockState.value = newState
    }

    // 완료된 할 일 가시성 상태 반환
    override fun getSettingCompletedItemsVisibilityState(): Boolean {
        return getCompletedItemsVisibility()
    }

    // 잠금 화면 상태 반환
    override fun getSettingUnlockScreenState(): Boolean {
        return getUnlockState()
    }

    // 완료된 할 일 가시성 관찰
    override fun observeSettingCompletedItemsVisibility(): Flow<Boolean> =
        settingCompletedItemsVisibility

    // 잠금 화면 표시 설정 관찰
    override fun observeSettingUnlockState(): Flow<Boolean> =
        settingUnlockState
}