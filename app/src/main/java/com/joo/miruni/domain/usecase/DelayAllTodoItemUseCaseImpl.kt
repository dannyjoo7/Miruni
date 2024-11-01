package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class DelayAllTodoItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : DelayAllTodoItemUseCase {
    override suspend fun invoke(itemIds: List<Long>, delayDateTime: LocalDateTime) {
        taskRepository.delayAllTodoEntity(itemIds, delayDateTime)
    }
}