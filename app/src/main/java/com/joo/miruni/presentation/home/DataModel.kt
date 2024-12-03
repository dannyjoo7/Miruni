package com.joo.miruni.presentation.home

import java.time.LocalDate
import java.time.LocalDateTime

data class Schedule(
    val id: Long,
    val title: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val description: String? = null,
    val daysBefore: Int,
    val isComplete: Boolean,
    val completeDate: LocalDateTime?,
)

data class ThingsTodo(
    val id: Long,
    val title: String,
    val deadline: LocalDateTime,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val completeDate: LocalDateTime?,
)

enum class DateChange {
    RIGHT,
    LEFT
}