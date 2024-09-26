package com.joo.miruni.presentation.home

import java.time.LocalDateTime

data class Schedule(
    val title: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val description: String? = null,
    val reminderDaysBefore: Int,
)

data class ThingsTodo(
    val title: String,
    val deadline: LocalDateTime,
    val description: String?= null,
    val reminderDaysBefore: Int,
    val isCompleted: Boolean = false,
)