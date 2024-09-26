package com.joo.miruni.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "HomeViewModel"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    private val _thingsTodoItems = MutableLiveData<List<ThingsTodo>>(emptyList())
    val thingsTodoItems: LiveData<List<ThingsTodo>> get() = _thingsTodoItems

    private val _scheduleItems = MutableLiveData<List<Schedule>>(emptyList())
    val scheduleItems: LiveData<List<Schedule>> get() = _scheduleItems

    init {
        loadInitialData()
        loadInitialScheduleData()
    }

    private fun loadInitialData(count: Int = 20) {
        _thingsTodoItems.value = List(count) { index ->
            val day = 20 + index
            val validDay = if (day > 30) 30 else day
            ThingsTodo(
                title = "할 일 ${index + 1}",
                deadline = LocalDateTime.parse("2024-09-$validDay 10:00", dateTimeFormatter),
                description = "할 일 ${index + 1}의 설명",
                reminderDaysBefore = index + 1,
                isCompleted = false // 기본값 설정
            )
        }
    }


    private fun loadInitialScheduleData() {
        _scheduleItems.value = listOf(
            Schedule("일정 1", LocalDateTime.parse("2024-09-20 09:00", dateTimeFormatter), LocalDateTime.parse("2024-09-20 10:00", dateTimeFormatter), "일정 1의 설명", 1),
            Schedule("일정 2", LocalDateTime.parse("2024-09-21 11:00", dateTimeFormatter), LocalDateTime.parse("2024-09-21 12:00", dateTimeFormatter), "일정 2의 설명", 2),
            Schedule("일정 3", LocalDateTime.parse("2024-09-22 13:00", dateTimeFormatter), LocalDateTime.parse("2024-09-22 14:00", dateTimeFormatter), "일정 3의 설명", 3),
        )
    }

    fun loadMoreData() {
        viewModelScope.launch {
            val currentList = _thingsTodoItems.value ?: emptyList()
            val newList = currentList + listOf(
                ThingsTodo(
                    title = "추가된 할 일",
                    deadline = LocalDateTime.parse("2024-09-26 15:00", dateTimeFormatter),
                    description = "추가된 할 일",
                    reminderDaysBefore = 1,
                    isCompleted = false
                )
            )
            _thingsTodoItems.value = newList
        }
    }

    fun loadMoreScheduleData() {
        viewModelScope.launch {
            val currentScheduleList = _scheduleItems.value ?: emptyList()
            val newScheduleList = currentScheduleList + listOf(
                Schedule(
                    title = "일정 4",
                    startDate = LocalDateTime.parse("2024-09-23 16:00", dateTimeFormatter),
                    endDate = LocalDateTime.parse("2024-09-23 17:00", dateTimeFormatter),
                    description = "일정 4의 설명",
                    reminderDaysBefore = 1
                ),
                Schedule(
                    title = "일정 5",
                    startDate = LocalDateTime.parse("2024-09-24 18:00", dateTimeFormatter),
                    endDate = LocalDateTime.parse("2024-09-24 19:00", dateTimeFormatter),
                    description = "일정 5의 설명",
                    reminderDaysBefore = 2
                )
            )
            _scheduleItems.value = newScheduleList
        }
    }

    fun formatTimeRemaining(deadline: LocalDateTime): String {
        val now = LocalDateTime.now()
        val minutesRemaining = ChronoUnit.MINUTES.between(now, deadline)
        val hoursRemaining = ChronoUnit.HOURS.between(now, deadline)
        val daysRemaining = ChronoUnit.DAYS.between(now, deadline)

        return when {
            minutesRemaining < 0 -> "기한 만료"
            minutesRemaining < 60 -> "${minutesRemaining}분 후"
            hoursRemaining < 24 -> "${hoursRemaining}시간 후"
            daysRemaining < 7 -> "${daysRemaining}일 후"
            else -> deadline.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }
    }

}


