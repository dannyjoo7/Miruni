package com.joo.miruni.domain.usecase.task

interface TogglePinStatusUseCase {
    suspend operator fun invoke(id: Long)
}
