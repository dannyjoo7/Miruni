package com.joo.miruni.presentation.addTask.addTodo

import com.joo.miruni.domain.model.TodoModel
import java.time.LocalDate
import java.time.LocalDateTime

data class AlarmDisplayDuration(
    val amount: Int?,
    val unit: String?,
)

data class TodoItem(
    val id: Long,
    val title: String,
    val descriptionText: String,
    val selectedDate: LocalDateTime,
    val adjustedDate: LocalDate,
    val isPinned: Boolean,
)

fun TodoModel.toTodoItem() = TodoItem(
    id = id,
    title = title,
    descriptionText = details.orEmpty(),
    selectedDate = deadLine ?: LocalDateTime.now(),
    adjustedDate = alarmDisplayDate,
    isPinned = isPinned
)