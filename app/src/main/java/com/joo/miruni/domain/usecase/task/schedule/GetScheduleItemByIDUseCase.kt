package com.joo.miruni.domain.usecase.task.schedule

import com.joo.miruni.domain.model.ScheduleModel


interface GetScheduleItemByIDUseCase {
    suspend operator fun invoke(
        taskId: Long,
    ): ScheduleModel
}
