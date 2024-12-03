package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskType
import java.time.LocalDate
import java.time.LocalDateTime

data class ScheduleModel(
    val id: Long,
    val title: String,
    val details: String?,

    val startDate: LocalDate?,
    val endDate: LocalDate?,

    val alarmDisplayDate: LocalDate?,
    val isComplete: Boolean,
    val completeDate: LocalDateTime?,

    val type: TaskType,
)

data class ScheduleItemsModel(
    val scheduleEntities: List<ScheduleModel>,
)