package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.ScheduleEntity
import com.joo.miruni.domain.model.TodoEntity
import com.joo.miruni.domain.model.toScheduleEntity
import com.joo.miruni.domain.model.toTodoEntity
import com.joo.miruni.domain.repository.TaskRepository
import javax.inject.Inject

class GetScheduleItemByIDUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetScheduleItemByIDUseCase {

    override suspend fun invoke(
        taskId: Long,
    ): ScheduleEntity {
        return taskRepository.getTaskItemById(taskId).toScheduleEntity()
    }
}



