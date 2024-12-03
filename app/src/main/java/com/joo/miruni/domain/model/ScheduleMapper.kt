package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.presentation.addTask.addSchedule.ScheduleItem


/*
* TaskEntity to ScheduleEntity
* */

// TaskItemsEntity to TodoItemsEntity
fun TaskItemsEntity.toScheduleItemsEntity() = ScheduleItemsModel(
    scheduleEntities = taskItemsEntity.map { it.toScheduleEntity() }
)

// TaskEntity to ScheduleEntity
fun TaskEntity.toScheduleEntity() = ScheduleModel(
    id = id,
    title = title,
    details = details,
    startDate = startDate,
    endDate = endDate,
    alarmDisplayDate = alarmDisplayDate,
    isComplete = isComplete,
    completeDate = completeDate,
    type = type,
)

/*
* ScheduleEntity to TaskEntity
* */

// TodoEntity to TaskEntity
fun ScheduleModel.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    details = details,
    startDate = startDate,
    endDate = endDate,
    deadLine = null,
    alarmDisplayDate = null,
    isComplete = isComplete,
    completeDate = completeDate,
    type = type
)

/*
* ScheduleItem to ScheduleEntity
* */

// AddSchedule
fun ScheduleItem.toScheduleEntity() = ScheduleModel(
    id = id,
    title = title,
    details = descriptionText,
    startDate = startDate,
    endDate = endDate,
    alarmDisplayDate = null,
    isComplete = false,
    completeDate = null,
    type = TaskType.SCHEDULE,
)