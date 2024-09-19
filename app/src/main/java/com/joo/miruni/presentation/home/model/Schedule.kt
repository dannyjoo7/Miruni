package com.joo.miruni.presentation.home.model

data class Schedule(
    val title: String,
    val startDate: String,
    val endDate: String,
    val description: String?,
    val reminderDaysBefore: Int,
)