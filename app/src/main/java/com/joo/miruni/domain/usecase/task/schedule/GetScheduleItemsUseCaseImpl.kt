package com.joo.miruni.domain.usecase.task.schedule

import com.joo.miruni.domain.model.ScheduleItemsModel
import com.joo.miruni.domain.model.toScheduleItemsEntity
import com.joo.miruni.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetScheduleItemsUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : GetScheduleItemsUseCase {

    override suspend fun invoke(
        selectDate: LocalDate,
        lastStartDate: LocalDate?,
    ): Flow<ScheduleItemsModel> {
        return taskRepository.getSchedules(selectDate, lastStartDate)
            .map { taskItemsEntity ->
                taskItemsEntity.toScheduleItemsEntity()
            }
    }
}



