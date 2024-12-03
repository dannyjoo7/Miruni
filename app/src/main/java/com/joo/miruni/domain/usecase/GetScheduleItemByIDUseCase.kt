package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.ScheduleModel


interface GetScheduleItemByIDUseCase {
    suspend operator fun invoke(
        taskId: Long,
    ): ScheduleModel
}
