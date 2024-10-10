package com.joo.miruni.domain.usecase

import com.joo.miruni.presentation.detailPage.TodoItem


interface UpdateTodoItemUseCase {
    suspend operator fun invoke(todoItem: TodoItem)
}
