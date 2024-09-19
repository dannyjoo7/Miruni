package com.joo.miruni.presentation.home.model

data class ThingsToDo(
    val title: String,
    val deadline: String,
    val description: String?,
    val reminderDaysBefore: Int,
)