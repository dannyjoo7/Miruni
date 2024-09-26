package com.joo.miruni.domain.usecase

import java.time.LocalTime

interface SaveAlarmTimeUseCase {
    suspend operator fun invoke(time: LocalTime)
}
