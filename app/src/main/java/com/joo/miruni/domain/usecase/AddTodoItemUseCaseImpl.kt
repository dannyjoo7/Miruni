package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.toTodoModel
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.presentation.addTask.addTodo.TodoItem
import javax.inject.Inject

class AddTodoItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : AddTodoItemUseCase {
    override suspend fun invoke(todoItem: TodoItem) {
        taskRepository.addTodo(todoItem.toTodoModel())
    }
}