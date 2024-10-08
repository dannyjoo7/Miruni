package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TodoItemsEntity
import com.joo.miruni.domain.model.toTodoItemsEntity
import com.joo.miruni.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class GetTodoItemsForAlarmUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetTodoItemsForAlarmUseCase {

    override suspend fun invoke(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime?,
    ): Flow<TodoItemsEntity> {
        return taskRepository.getTasksForAlarmByDate(selectDate, lastDeadLine)
            .map { taskItemsEntity ->
                taskItemsEntity.toTodoItemsEntity()
            }
    }
}



