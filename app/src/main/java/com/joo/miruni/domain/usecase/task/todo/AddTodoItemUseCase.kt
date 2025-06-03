package com.joo.miruni.domain.usecase.task.todo

import com.joo.miruni.presentation.addTask.addTodo.TodoItem


interface AddTodoItemUseCase {
    suspend operator fun invoke(todoItem: TodoItem)
}
