package com.joo.miruni.domain.usecase

import com.joo.miruni.presentation.addTodo.TodoItem


interface AddTodoItemUseCase {
    suspend operator fun invoke(todoItem: TodoItem)
}
