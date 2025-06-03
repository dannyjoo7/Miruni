package com.joo.miruni.domain.usecase.task

import com.joo.miruni.domain.repository.TaskRepository
import javax.inject.Inject

class CancelCompleteTaskItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : CancelCompleteTaskItemUseCase {
    override suspend fun invoke(id: Long) {
        taskRepository.markTaskAsCancelCompleted(id)
    }
}