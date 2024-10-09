package com.joo.miruni.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodoItemsForAlarmUseCase: GetTodoItemsForAlarmUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val completeTaskItemUseCase: CompleteTaskItemUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "HomeViewModel"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    /*
    * Live Date
    * */

    // 할 일 Item list
    private val _thingsTodoItems = MutableLiveData<List<ThingsTodo>>(emptyList())
    val thingsTodoItems: LiveData<List<ThingsTodo>> get() = _thingsTodoItems

    // 일정 Item list
    private val _scheduleItems = MutableLiveData<List<Schedule>>(emptyList())
    val scheduleItems: LiveData<List<Schedule>> get() = _scheduleItems

    // 로딩 중인지 판단하는 변수
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 확장 여부를 판단하는 변수
    private val _expandedItems = mutableStateOf<Set<Long>>(emptySet())
    val expandedItems: State<Set<Long>> = _expandedItems


    // 페이징을 위한 마지막 데이터의 deadLine
    private var lastDataDeadLine: LocalDateTime? = null


    init {
        loadTodoItemsForAlarm()
        loadInitialScheduleData()
    }


    // 할 일 load 메소드
    private fun loadTodoItemsForAlarm() {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                getTodoItemsForAlarmUseCase.invoke(
                    LocalDateTime.now(),
                    null
                )
            }.onSuccess { flow ->
                flow.collect { todoItems ->
                    _thingsTodoItems.value = todoItems.todoEntities.map {
                        ThingsTodo(
                            id = it.id,
                            title = it.title,
                            deadline = it.deadLine ?: LocalDateTime.now(),
                            description = it.details ?: "",
                            isCompleted = it.isComplete
                        )
                    }
                    lastDataDeadLine = _thingsTodoItems.value?.lastOrNull()?.deadline
                    _isLoading.value = false
                }

            }.onFailure { exception ->
                exception.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    // 할 일 추가 load 메소드
    fun loadMoreTodoItemsForAlarm() {
        viewModelScope.launch {
            _isLoading.value = true
            val currentDeadLine = lastDataDeadLine

            if (currentDeadLine != null) {
                runCatching {
                    getTodoItemsForAlarmUseCase.invoke(
                        LocalDateTime.now(),
                        currentDeadLine
                    )
                }.onSuccess { flow ->
                    flow.collect { todoItems ->
                        if (todoItems.todoEntities.isNotEmpty()) {
                            val updatedItems =
                                _thingsTodoItems.value.orEmpty() + todoItems.todoEntities.map {
                                    ThingsTodo(
                                        id = it.id,
                                        title = it.title,
                                        deadline = it.deadLine ?: LocalDateTime.now(),
                                        description = it.details ?: "",
                                        isCompleted = it.isComplete
                                    )
                                }
                            _thingsTodoItems.value = updatedItems
                            lastDataDeadLine = updatedItems.lastOrNull()?.deadline
                        }
                        _isLoading.value = false
                    }
                }.onFailure { exception ->
                    exception.printStackTrace()
                }.also {
                    _isLoading.value = false
                }
            } else {
                _isLoading.value = false
            }
        }
    }

    // Task 삭제 메소드
    fun deleteTaskItem(id: Long) {
        viewModelScope.launch {
            runCatching {
                deleteTaskItemUseCase.invoke(id)
                _expandedItems.value -= id
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // Task 완료 시
    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            runCatching {
                completeTaskItemUseCase.invoke(taskId, LocalDateTime.now())
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // TodoTings 확장 토글 메소드
    fun toggleItemExpansion(id: Long) {
        _expandedItems.value = if (_expandedItems.value.contains(id)) {
            emptySet()
        } else {
            setOf(id)
        }
    }

    // TodoTings가 확장 되어있는지 판단 메소드
    fun isItemExpanded(id: Long): Boolean {
        return _expandedItems.value.contains(id)
    }


    // TODO 임시...
    private fun loadInitialScheduleData() {
        _scheduleItems.value = listOf(
            Schedule(
                "일정 1",
                LocalDateTime.parse("2024-09-20 09:00", dateTimeFormatter),
                LocalDateTime.parse("2024-09-20 10:00", dateTimeFormatter),
                "일정 1의 설명",
                1
            ),
            Schedule(
                "일정 2",
                LocalDateTime.parse("2024-09-21 11:00", dateTimeFormatter),
                LocalDateTime.parse("2024-09-21 12:00", dateTimeFormatter),
                "일정 2의 설명",
                2
            ),
            Schedule(
                "일정 3",
                LocalDateTime.parse("2024-09-22 13:00", dateTimeFormatter),
                LocalDateTime.parse("2024-09-22 14:00", dateTimeFormatter),
                "일정 3의 설명",
                3
            ),
        )
    }

    // 일정 load 메소드
    fun loadMoreScheduleData() {
        viewModelScope.launch {
            // Todo
        }
    }

    // 남은 시간 포맷 메소드
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

    // 남은 시간에 따른 색 결정 메소드
    fun getColorForRemainingTime(deadline: LocalDateTime): Importance {
        val now = LocalDateTime.now()
        val minutesRemaining = ChronoUnit.MINUTES.between(now, deadline)

        return when {
            minutesRemaining < 0 -> Importance.RED            // 기한 만료
            minutesRemaining < 720 -> Importance.RED                 // 12시간 이내
            minutesRemaining < 1440 -> Importance.ORANGE        // 주황색
            minutesRemaining < 4320 -> Importance.YELLOW             // 노란색
            else -> Importance.GREEN                             // 초록색
        }
    }
}




