package com.joo.miruni.domain.usecase.task.todo

import com.joo.miruni.domain.model.TodoModel


interface GetTodoItemByIDUseCase {
    suspend operator fun invoke(
        taskId: Long,
    ): TodoModel
}
