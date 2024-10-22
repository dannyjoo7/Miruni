package com.joo.miruni.presentation.addTask.addSchedule

import java.time.LocalDate
import java.time.LocalDateTime

data class AlarmDisplayDuration(
    val amount: Int?,
    val unit: String?,
)

data class ScheduleItem(
    val title: String,
    val descriptionText: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val adjustedDate: LocalDate,
)