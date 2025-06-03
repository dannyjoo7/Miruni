package com.joo.miruni.domain.usecase.task.schedule

import com.joo.miruni.domain.model.toScheduleEntity
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.presentation.addTask.addSchedule.ScheduleItem
import javax.inject.Inject

class AddScheduleItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : AddScheduleItemUseCase {
    override suspend fun invoke(scheduleItem: ScheduleItem) {
        taskRepository.addSchedule(scheduleItem.toScheduleEntity())
    }
}