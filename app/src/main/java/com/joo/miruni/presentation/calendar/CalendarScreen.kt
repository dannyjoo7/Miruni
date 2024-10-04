package com.joo.miruni.presentation.calendar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {
    Text("캘린더 화면")
}