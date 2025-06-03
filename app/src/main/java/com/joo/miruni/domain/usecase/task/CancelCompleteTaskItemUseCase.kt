package com.joo.miruni.domain.usecase.task

interface CancelCompleteTaskItemUseCase {
    suspend operator fun invoke(id: Long)
}
