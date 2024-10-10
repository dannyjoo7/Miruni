package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TodoEntity


interface GetTodoItemByIDUseCase {
    suspend operator fun invoke(
        taskId: Long,
    ): TodoEntity
}
