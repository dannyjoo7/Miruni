package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TodoItemsModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime


interface GetOverDueTodoItemsForAlarmUseCase {
    suspend operator fun invoke(
        selectDate: LocalDateTime,
    ): Flow<TodoItemsModel>
}
