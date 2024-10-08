package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskType
import java.time.LocalDate
import java.time.LocalDateTime

data class TodoEntity(
    val id: Long,
    val title: String,
    val details: String?,
    val deadLine: LocalDateTime?,
    val alarmDisplayDate: LocalDate,
    val type: TaskType,
    val isComplete: Boolean,
    val completeDate: LocalDateTime?,
)

data class TodoItemsEntity(
    val todoEntities: List<TodoEntity>,
)