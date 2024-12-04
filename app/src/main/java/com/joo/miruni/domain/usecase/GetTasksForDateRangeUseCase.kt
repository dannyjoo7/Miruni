package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TaskItemsModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface GetTasksForDateRangeUseCase {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<TaskItemsModel>
}
