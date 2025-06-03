package com.joo.miruni.domain.usecase.task.todo

import com.joo.miruni.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class DelayTodoItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : DelayTodoItemUseCase {
    override suspend fun invoke(id: Long, delayDateTime: LocalDateTime) {
        taskRepository.delayTodoEntity(id, delayDateTime)
    }
}