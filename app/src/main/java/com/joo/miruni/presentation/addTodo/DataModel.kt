package com.joo.miruni.presentation.addTodo

import java.time.LocalDate
import java.time.LocalTime

data class AlarmDisplayDuration(
    val amount: Int?,
    val unit: String?,
)

data class TodoItem(
    val todoText: String,
    val descriptionText: String,
    val selectedDate: LocalDate,
    val selectedTime: LocalTime,
    val adjustedDate: LocalDate,    // 알림 표시 시작일
)