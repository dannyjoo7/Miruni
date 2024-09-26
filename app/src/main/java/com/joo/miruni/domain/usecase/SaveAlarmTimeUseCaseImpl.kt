package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import java.time.LocalTime
import javax.inject.Inject

class SaveAlarmTimeUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) : SaveAlarmTimeUseCase {
    override suspend fun invoke(time: LocalTime) {
        sharedPreferenceRepository.saveAlarmTime(time)
    }
}

