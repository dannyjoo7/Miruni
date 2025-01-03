package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskType
import java.time.LocalDate
import java.time.LocalDateTime

data class TaskModel(
    val id: Long,
    val title: String,
    val details: String?,

    val startDate: LocalDate?,
    val endDate: LocalDate?,

    val deadLine: LocalDateTime?,

    val alarmDisplayDate: LocalDate?,
    val isComplete: Boolean,
    val completeDate: LocalDateTime?,
    val type: TaskType,
    val isPinned: Boolean,
)

data class TaskItemsModel(
    val taskEntities: List<TaskModel>,
)