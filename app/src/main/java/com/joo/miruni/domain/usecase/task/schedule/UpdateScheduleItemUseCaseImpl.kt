package com.joo.miruni.domain.usecase.task.schedule

import com.joo.miruni.domain.model.toScheduleEntity
import com.joo.miruni.domain.model.toTaskEntity
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.presentation.addTask.addSchedule.ScheduleItem
import javax.inject.Inject

class UpdateScheduleItemUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : UpdateScheduleItemUseCase {
    override suspend fun invoke(scheduleItem: ScheduleItem) {
        taskRepository.updateTask(scheduleItem.toScheduleEntity().toTaskEntity())
    }
}