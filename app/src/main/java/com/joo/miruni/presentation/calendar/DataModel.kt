package com.joo.miruni.presentation.calendar

import com.joo.miruni.data.entities.TaskType
import java.time.LocalDate
import java.time.LocalDateTime

data class TaskItem(
    val id: Long?,

    val deadline: LocalDateTime?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,

    val title: String,
    val details: String?,
    val isComplete: Boolean,
    val completeDate: LocalDateTime?,
    val type: TaskType,
)
