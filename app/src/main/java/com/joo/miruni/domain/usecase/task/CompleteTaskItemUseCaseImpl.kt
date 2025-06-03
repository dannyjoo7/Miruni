package com.joo.miruni.domain.usecase.task

import com.joo.miruni.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CompleteTaskItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : CompleteTaskItemUseCase {
    override suspend fun invoke(id: Long, completionTime: LocalDateTime) {
        taskRepository.markTaskAsCompleted(id, completionTime)
    }
}