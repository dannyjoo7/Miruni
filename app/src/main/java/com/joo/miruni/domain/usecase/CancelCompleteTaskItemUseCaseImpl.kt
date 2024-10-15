package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CancelCompleteTaskItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : CancelCompleteTaskItemUseCase {
    override suspend fun invoke(id: Long) {
        taskRepository.markTaskAsCancelCompleted(id)
    }
}