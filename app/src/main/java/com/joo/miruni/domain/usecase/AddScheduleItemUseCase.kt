package com.joo.miruni.domain.usecase

import com.joo.miruni.presentation.addTask.addSchedule.ScheduleItem


interface AddScheduleItemUseCase {
    suspend operator fun invoke(scheduleItem: ScheduleItem)
}
