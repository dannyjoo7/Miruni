package com.joo.miruni.presentation.home

import androidx.compose.ui.graphics.Color

data class Event(
    val title: String,
    val time: String,
    val color: Color,
    val description: String?,
)