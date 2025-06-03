package com.joo.miruni.domain.usecase.task

import com.joo.miruni.domain.repository.TaskRepository
import javax.inject.Inject

class TogglePinStatusUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : TogglePinStatusUseCase {
    override suspend fun invoke(id: Long) {
        return taskRepository.togglePinStatus(id)
    }
}