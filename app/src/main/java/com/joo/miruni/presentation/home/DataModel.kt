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

enum class DialogMod {
    DELETE,         // 삭제
    COMPLETE,       // 완료
    CANCEL_COMPLETE // 완료 취소
}