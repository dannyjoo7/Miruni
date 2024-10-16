package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.toTodoEntity
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.presentation.modifyPage.TodoItem
import javax.inject.Inject

class UpdateTodoItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : UpdateTodoItemUseCase {
    override suspend fun invoke(todoItem: TodoItem) {
        taskRepository.updateTask(todoItem.toTodoEntity())
    }
}