package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.ScheduleItemsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


interface GetScheduleItemsUseCase {
    suspend operator fun invoke(
        selectDate: LocalDate,
        lastStartDate: LocalDate?
    ): Flow<ScheduleItemsEntity>
}
