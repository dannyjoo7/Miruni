package com.joo.miruni.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.presentation.home.model.ThingsToDo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "HomeViewModel"
    }

    // 임시 데이터 리스트
    private val _thingsToDoItems = mutableStateListOf<ThingsToDo>()
    val thingsToDoItems: List<ThingsToDo> get() = _thingsToDoItems

    init {
        // 초기 데이터 로드
        loadInitialData()
    }

    private fun loadInitialData() {
        // 여기에 임시 데이터 추가
        _thingsToDoItems.addAll(
            listOf(
                ThingsToDo("할 일 1", "2024-09-20", "할 일 1의 설명", 1),
                ThingsToDo("할 일 2", "2024-09-21", "할 일 2의 설명", 2),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
                ThingsToDo("할 일 3", "2024-09-22", "할 일 3의 설명", 3),
            )
        )
    }

    fun loadMoreData() {
        // 여기에 더 많은 임시 데이터 추가
        viewModelScope.launch {
            _thingsToDoItems.addAll(
                listOf(
                    ThingsToDo("할 일 4", "2024-09-23", "할 일 4의 설명", 1),
                    ThingsToDo("할 일 5", "2024-09-24", "할 일 5의 설명", 2)
                )
            )
        }
    }
}




