package com.joo.miruni.domain.usecase.task.schedule

import com.joo.miruni.domain.model.ScheduleItemsModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface GetScheduleItemsUseCase {
    suspend operator fun invoke(
        selectDate: LocalDate,
        lastStartDate: LocalDate?
    ): Flow<ScheduleItemsModel>
}
