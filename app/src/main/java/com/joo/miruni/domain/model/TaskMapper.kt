package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import java.time.LocalDate

/*
* TaskEntity to TodoEntity*/

// TaskItemsEntity to TaskItemsEntity
fun TaskItemsEntity.toTaskItemsModel() = TaskItemsModel(
    taskEntities = taskItemsEntity.map { it.toTaskModel() }
)

// TaskEntity to TaskEntity
fun TaskEntity.toTaskModel() = TaskModel(
    id = id,
    title = title,
    details = details,
    startDate = startDate,
    endDate = endDate,
    deadLine = deadLine,
    alarmDisplayDate = alarmDisplayDate ?: LocalDate.now(),
    isComplete = isComplete,
    completeDate = completeDate,
    type = type,
    isPinned = isPinned
)