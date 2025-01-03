package com.joo.miruni.domain.usecase

interface TogglePinStatusUseCase {
    suspend operator fun invoke(id: Long)
}
