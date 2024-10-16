package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TodoItemsEntity
import com.joo.miruni.domain.model.toTodoItemsEntity
import com.joo.miruni.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class GetOverDueTodoItemsForAlarmUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetOverDueTodoItemsForAlarmUseCase {
    override suspend operator fun invoke(selectDate: LocalDateTime): Flow<TodoItemsEntity> {
        return taskRepository.getOverdueTaskEntities(selectDate).map { taskEntities ->
            taskEntities.toTodoItemsEntity()
        }
    }
}