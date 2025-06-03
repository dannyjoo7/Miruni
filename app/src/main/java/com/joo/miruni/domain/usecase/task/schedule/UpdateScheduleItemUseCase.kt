package com.joo.miruni.domain.usecase.task.schedule

import com.joo.miruni.presentation.addTask.addSchedule.ScheduleItem

interface UpdateScheduleItemUseCase {
    suspend operator fun invoke(scheduleItem: ScheduleItem)
}
