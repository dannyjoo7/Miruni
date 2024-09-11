package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SaveAlarmTimeUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) : SaveAlarmTimeUseCase {
    override suspend fun invoke(time: String) {
        sharedPreferenceRepository.saveAlarmTime(time)
    }
}

