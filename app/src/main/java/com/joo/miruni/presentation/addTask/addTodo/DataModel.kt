package com.joo.miruni.presentation.addTask.addTodo

import java.time.LocalDate
import java.time.LocalDateTime

data class AlarmDisplayDuration(
    val amount: Int?,
    val unit: String?,
)

data class TodoItem(
    val todoText: String,
    val descriptionText: String,
    val selectedDate: LocalDateTime,
    val adjustedDate: LocalDate,    // 알림 표시 시작일
)