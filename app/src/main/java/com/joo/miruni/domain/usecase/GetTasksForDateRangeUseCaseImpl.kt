package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TaskItemsModel
import com.joo.miruni.domain.model.toTaskItemsModel
import com.joo.miruni.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForDateRangeUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetTasksForDateRangeUseCase {

    override suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<TaskItemsModel> {
        return taskRepository.getTasksForDateRange(startDate, endDate).map { taskItemsEntity ->
            taskItemsEntity.toTaskItemsModel()
        }
    }
}