package com.joo.miruni.domain.usecase

interface SaveAlarmTimeUseCase {
    suspend operator fun invoke(time: String)
}
