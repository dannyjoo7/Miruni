package com.joo.miruni.service.notification

enum class ReminderType(val label: String) {
    ONE_HOUR_BEFORE("1시간 전"),
    TEN_MINUTES_BEFORE("10분 전"),
    FIVE_MINUTES_BEFORE("5분 전"),
    NOW("지금")
}
