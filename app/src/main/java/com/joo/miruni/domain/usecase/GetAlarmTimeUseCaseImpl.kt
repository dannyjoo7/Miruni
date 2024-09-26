package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import java.time.LocalTime
import javax.inject.Inject

class GetAlarmTimeUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : GetAlarmTimeUseCase {
    override suspend fun invoke(): LocalTime? {
        return sharedPreferenceRepository.getAlarmTime()
    }
}