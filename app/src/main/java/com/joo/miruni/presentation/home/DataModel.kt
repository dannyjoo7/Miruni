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

enum class Importance {
    BLINK_RED,  // 12시간 이내
    RED,        // 24시간 이내
    ORANGE,     // 2일 이내
    YELLOW,     // 3일 이내
    GREEN,      // 7일 이내
    EMERGENCY   // 긴급
    ,
}

enum class DateChange {
    RIGHT,
    LEFT
}