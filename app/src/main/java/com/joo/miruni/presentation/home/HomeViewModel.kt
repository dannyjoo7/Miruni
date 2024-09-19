package com.joo.miruni.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.presentation.home.model.Schedule
import com.joo.miruni.presentation.home.model.ThingsToDo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "HomeViewModel"
    }

    // MutableLiveData로 변경
    private val _thingsToDoItems = MutableLiveData<List<ThingsToDo>>(emptyList())
    val thingsToDoItems: LiveData<List<ThingsToDo>> get() = _thingsToDoItems

    // 새로운 일정 아이템을 위한 MutableLiveData 추가
    private val _scheduleItems = MutableLiveData<List<Schedule>>(emptyList())
    val scheduleItems: LiveData<List<Schedule>> get() = _scheduleItems

    init {
        loadInitialData()
        loadInitialScheduleData()
    }

    private fun loadInitialData(count: Int = 20) {
        _thingsToDoItems.value = List(count) { index ->
            ThingsToDo(
                "할 일 ${index + 1}",
                "2024-09-${20 + index}",
                "할 일 ${index + 1}의 설명",
                index + 1
            )
        }
    }

    private fun loadInitialScheduleData() {
        _scheduleItems.value = listOf(
            Schedule("일정 1", "2024-09-20", "2024-09-20", "일정 1의 설명", 1),
            Schedule("일정 2", "2024-09-21", "2024-09-21", "일정 2의 설명", 2),
            Schedule("일정 3", "2024-09-22", "2024-09-22", "일정 3의 설명", 3),
        )
    }

    fun loadMoreData() {
        viewModelScope.launch {
            val currentList = _thingsToDoItems.value ?: emptyList()
            val newList = currentList + listOf(
                ThingsToDo("추가된 할 일", "2024-09-26", "추가된 할 일", 1),
            )
            _thingsToDoItems.value = newList
        }
    }

    fun loadMoreScheduleData() {
        viewModelScope.launch {
            val currentScheduleList = _scheduleItems.value ?: emptyList()
            val newScheduleList = currentScheduleList + listOf(
                Schedule("일정 4", "2024-09-23", "2024-09-23", "일정 4의 설명", 1),
                Schedule("일정 5", "2024-09-24", "2024-09-24", "일정 5의 설명", 2)
            )
            _scheduleItems.value = newScheduleList
        }
    }
}




