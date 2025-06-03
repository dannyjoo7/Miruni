package com.joo.miruni.domain.usecase.task.todo

import com.joo.miruni.domain.model.TodoItemsModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime


interface GetTodoItemsForAlarmUseCase {
    suspend operator fun invoke(
        selectDate: LocalDateTime,
    ): Flow<TodoItemsModel>
}
