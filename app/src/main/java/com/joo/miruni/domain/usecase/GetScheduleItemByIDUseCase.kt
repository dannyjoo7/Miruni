package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.ScheduleEntity


interface GetScheduleItemByIDUseCase {
    suspend operator fun invoke(
        taskId: Long,
    ): ScheduleEntity
}
