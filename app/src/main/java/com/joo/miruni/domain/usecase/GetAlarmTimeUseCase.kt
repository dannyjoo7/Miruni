package com.joo.miruni.domain.usecase

interface GetAlarmTimeUseCase {
    suspend operator fun invoke(): String
}