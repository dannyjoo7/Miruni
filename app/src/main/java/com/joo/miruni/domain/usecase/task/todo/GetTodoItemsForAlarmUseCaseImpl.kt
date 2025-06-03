package com.joo.miruni.domain.usecase.task.todo

import com.joo.miruni.domain.model.TodoItemsModel
import com.joo.miruni.domain.model.toTodoItemsModel
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
    ): Flow<TodoItemsModel> {
        return taskRepository.getTasksForAlarmByDate(selectDate)
            .map { taskItemsEntity ->
                taskItemsEntity.toTodoItemsModel()
            }
    }
}



