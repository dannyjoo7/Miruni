package com.joo.miruni.presentation.detail.detailTodo

import com.joo.miruni.domain.model.TodoModel
import java.time.LocalDate
import java.time.LocalDateTime

data class AlarmDisplayDuration(
    val amount: Int?,
    val unit: String?,
)

data class TodoItem(
    val id: Long?,
    val todoText: String,
    val descriptionText: String?,
    val selectedDate: LocalDateTime?,
    val adjustedDate: LocalDate?,    // 알림 표시 시작일
    val isComplete: Boolean,
)

fun TodoModel.toTodoItem() = TodoItem(
    id = id,
    todoText = title,
    descriptionText = details,
    selectedDate = deadLine,
    adjustedDate = alarmDisplayDate,
    isComplete = isComplete
)