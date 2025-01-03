package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.toTaskEntity
import com.joo.miruni.domain.model.toTodoModel
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.presentation.addTask.addTodo.TodoItem
import javax.inject.Inject

class UpdateTodoItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : UpdateTodoItemUseCase {
    override suspend fun invoke(todoItem: TodoItem) {
        taskRepository.updateTask(todoItem.toTodoModel().toTaskEntity())
    }
}