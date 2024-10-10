package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TodoEntity
import com.joo.miruni.domain.model.toTodoEntity
import com.joo.miruni.domain.repository.TaskRepository
import javax.inject.Inject

class GetTodoItemByIDUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetTodoItemByIDUseCase {

    override suspend fun invoke(
        taskId: Long,
    ): TodoEntity {
        return taskRepository.getTodoItemById(taskId).toTodoEntity()
    }
}



