package com.joo.miruni.domain.usecase

interface CancelCompleteTaskItemUseCase {
    suspend operator fun invoke(id: Long)
}
