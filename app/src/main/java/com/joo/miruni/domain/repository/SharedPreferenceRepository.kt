package com.joo.miruni.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalTime


interface SharedPreferenceRepository {
    suspend fun saveAlarmTime(time: LocalTime)
    suspend fun getAlarmTime(): LocalTime?
    suspend fun settingUpdateCompletedItemsVisibility()
    fun observeSettingCompletedItemsVisibility(): Flow<Boolean>
}
