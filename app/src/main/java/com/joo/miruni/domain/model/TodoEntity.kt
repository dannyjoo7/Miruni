package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskType
import java.time.LocalDate
import java.time.LocalTime

data class TodoEntity(
    val id: Long,
    val title: String,
    val details: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val deadLine: LocalDate?,
    val deadLineTime: LocalTime?,
    val alarmDisplayDate: LocalDate,
    val type: TaskType,
)

data class TodoItemsEntity(
    val todoEntities: List<TodoEntity>,
)