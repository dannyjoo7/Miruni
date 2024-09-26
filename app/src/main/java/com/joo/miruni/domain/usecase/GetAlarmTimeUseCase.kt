package com.joo.miruni.domain.usecase

import java.time.LocalTime

interface GetAlarmTimeUseCase {
    suspend operator fun invoke(): LocalTime?
}