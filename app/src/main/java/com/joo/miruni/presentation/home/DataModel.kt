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
    val id: Long,
    val title: String,
    val deadline: LocalDateTime,
    val description: String? = null,
    val isCompleted: Boolean = false,
)

enum class Importance {
    RED, ORANGE, YELLOW, GREEN
}