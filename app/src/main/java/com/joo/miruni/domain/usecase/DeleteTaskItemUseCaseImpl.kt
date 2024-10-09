package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : DeleteTaskItemUseCase {
    override suspend fun invoke(id: Long) {
        taskRepository.deleteTaskById(id)
    }
}