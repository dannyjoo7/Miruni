package com.joo.miruni.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DelayTodoItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.GetOverDueTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.GetScheduleItemsUseCase
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodoItemsForAlarmUseCase: GetTodoItemsForAlarmUseCase,
    private val getScheduleItemsForAlarmUseCase: GetScheduleItemsUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val completeTaskItemUseCase: CompleteTaskItemUseCase,
    private val cancelCompleteTaskItemUseCase: CancelCompleteTaskItemUseCase,
    private val delayTodoItemUseCase: DelayTodoItemUseCase,
    private val settingObserveCompletedItemsVisibilityUseCase: SettingObserveCompletedItemsVisibilityUseCase,
    private val getOverDueTodoItemsForAlarmUseCase: GetOverDueTodoItemsForAlarmUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "HomeViewModel"
    }

    /*
    * Live Date
    * */

    // 선택된 날짜
    private val _selectDate = MutableLiveData<LocalDateTime>(LocalDateTime.now())
    val selectDate: LiveData<LocalDateTime> get() = _selectDate

    // 할 일 Item list
    private val _thingsTodoItems = MutableLiveData<List<ThingsTodo>>(emptyList())
    val thingsTodoItems: LiveData<List<ThingsTodo>> get() = _thingsTodoItems

    // 일정 Item list
    private val _scheduleItems = MutableLiveData<List<Schedule>>(emptyList())
    val scheduleItems: LiveData<List<Schedule>> get() = _scheduleItems

    // todoList 로딩 중인지 판단하는 변수
    private val _isTodoListLoading = MutableLiveData(false)
    val isTodoListLoading: LiveData<Boolean> get() = _isTodoListLoading

    // scheduleList 로딩 중인지 판단하는 변수
    private val _isScheduleListLoading = MutableLiveData(false)
    val isScheduleListLoading: LiveData<Boolean> get() = _isScheduleListLoading

    // 확장 여부를 판단하는 변수
    private val _expandedItems = mutableStateOf<Set<Long>>(emptySet())
    val expandedItems: State<Set<Long>> = _expandedItems

    // 삭제됐는지 여부를 판단하는 변수
    private val _deletedItems = MutableLiveData<Set<Long>>(emptySet())
    val deletedItems: LiveData<Set<Long>> get() = _deletedItems

    // 유저 Setting 값

    // 완료 항목 값
    private val _settingObserveCompleteVisibility = MutableLiveData<Boolean>(false)
    val settingObserveCompleteVisibility: LiveData<Boolean> get() = _settingObserveCompleteVisibility

    // todoList 페이징을 위한 마지막 데이터의 deadLine
    private var lastDataDeadLine: LocalDateTime? = null

    // scheduleList 페이징을 위한 마지막 데이터의 startDate
    private var lastStartDate: LocalDate? = null


    init {
        loadTodoItemsForAlarm()
        loadInitialScheduleData()
        loadUserSetting()
    }


    /*
    * 할 일
    * */

    // 기한이 지난 할 일 load
    private fun loadOverdueTasks() {
        viewModelScope.launch {
            _isTodoListLoading.value = true
            runCatching {
                getOverDueTodoItemsForAlarmUseCase.invoke(LocalDateTime.now())
            }.onSuccess { flow ->
                flow.collect { todoItems ->
                    _thingsTodoItems.value = todoItems.todoEntities.map {
                        ThingsTodo(
                            id = it.id,
                            title = it.title,
                            deadline = it.deadLine ?: LocalDateTime.now(),
                            description = it.details ?: "",
                            isCompleted = it.isComplete,
                            completeDate = it.completeDate
                        )
                    }
                    lastDataDeadLine = _thingsTodoItems.value?.lastOrNull()?.deadline
                    _isTodoListLoading.value = false
                }
            }.onFailure { exception ->
                exception.printStackTrace()
                _isTodoListLoading.value = false
            }
        }
    }

    // 할 일 load 메소드
    private fun loadTodoItemsForAlarm() {
        viewModelScope.launch {
            _isTodoListLoading.value = true
            runCatching {
                getTodoItemsForAlarmUseCase.invoke(
                    _selectDate.value ?: LocalDateTime.now(),
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
                            isCompleted = it.isComplete,
                            completeDate = it.completeDate,
                        )
                    }
                    lastDataDeadLine = _thingsTodoItems.value?.lastOrNull()?.deadline
                    _isTodoListLoading.value = false
                }

            }.onFailure { exception ->
                exception.printStackTrace()
                _isTodoListLoading.value = false
            }
        }
    }

    // 할 일 추가 load 메소드
    fun loadMoreTodoItemsForAlarm() {
        viewModelScope.launch {
            _isTodoListLoading.value = true
            val currentDeadLine = lastDataDeadLine

            if (currentDeadLine != null) {
                runCatching {
                    getTodoItemsForAlarmUseCase.invoke(
                        _selectDate.value ?: LocalDateTime.now(),
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
                                        isCompleted = it.isComplete,
                                        completeDate = it.completeDate,
                                    )
                                }
                            _thingsTodoItems.value = updatedItems
                            lastDataDeadLine = updatedItems.lastOrNull()?.deadline
                        }
                        _isTodoListLoading.value = false
                    }
                }.onFailure { exception ->
                    exception.printStackTrace()
                }.also {
                    _isTodoListLoading.value = false
                }
            } else {
                _isTodoListLoading.value = false
            }
        }
    }

    // Task 삭제 메소드
    fun deleteTaskItem(id: Long) {
        viewModelScope.launch {
            _deletedItems.value = _deletedItems.value?.plus(id) ?: setOf(id)
            runCatching {
                // 애니매이션을 위한 딜레이
                delay(1000)
                deleteTaskItemUseCase.invoke(id)
            }.onSuccess {
                _expandedItems.value = _expandedItems.value.filterNot { it == id }.toSet()
            }.onFailure {
                _deletedItems.value = _deletedItems.value?.minus(id)
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

    // Task 완료 취소 시
    fun completeCancelTaskItem(taskId: Long) {
        viewModelScope.launch {
            runCatching {
                cancelCompleteTaskItemUseCase.invoke(taskId)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // 미루기 메소드
    fun delayTodoItem(thingsTodo: ThingsTodo) {
        viewModelScope.launch {
            runCatching {
                // TODO deadLine 미룰 때 기기에 저장된 유저가 설정한 값으로 대체 -> 현재 1에서 userSetting 값으로...
                delayTodoItemUseCase.invoke(thingsTodo.id, thingsTodo.deadline.plusDays(1))
            }.onSuccess {

            }.onFailure {

            }
        }
    }


    /*
    * UI
    * */

    // 날짜 바꾸는 메소드
    fun changeDate(op: String) {
        _thingsTodoItems.value = emptyList()
        _selectDate.value = when (op) {
            ">" -> LocalDateTime.of(
                // 자정으로 설정
                _selectDate.value?.toLocalDate() ?: LocalDate.now(),
                LocalTime.MIDNIGHT
            ).plusDays(1) ?: LocalDateTime.now()

            "<" -> LocalDateTime.of(
                // 자정으로 설정
                _selectDate.value?.toLocalDate() ?: LocalDate.now(),
                LocalTime.MIDNIGHT
            ).minusDays(1) ?: LocalDateTime.now()

            else -> _selectDate.value
        }
        // 바뀐 날짜가 오늘이면 자정이 아닌 현재 시간 적용
        if ((_selectDate.value?.toLocalDate() ?: LocalDate.now()) == LocalDate.now()) {
            _selectDate.value = LocalDateTime.now()
        }
        collapseAllItems()
        loadTodoItemsForAlarm()
        lastDataDeadLine = null
    }

    // TodoTings 확장 토글 메소드
    fun toggleItemExpansion(id: Long) {
        _expandedItems.value = if (_expandedItems.value.contains(id)) {
            emptySet()
        } else {
            setOf(id)
        }
    }

    // TodoTings가 모든 확장 해제 메소드
    fun collapseAllItems() {
        _expandedItems.value = emptySet()
    }

    // 날짜 Text 포멧
    fun formatSelectedDate(date: LocalDateTime): String {
        val selectDate = date.toLocalDate()
        val today = LocalDate.now()

        return when {
            selectDate.isEqual(today.minusDays(1)) -> "어제"
            selectDate.isEqual(today) -> "오늘"
            selectDate.isEqual(today.plusDays(1)) -> "내일"
            else -> selectDate.format(DateTimeFormatter.ofPattern("M월 d일, yyyy"))
        }
    }

    /*
    * 일정
    * */

    // 일정 초기 로드 메소드
    private fun loadInitialScheduleData() {
        viewModelScope.launch {
            _isScheduleListLoading.value = true
            runCatching {
                getScheduleItemsForAlarmUseCase.invoke(
                    _selectDate.value?.toLocalDate() ?: LocalDate.now(),
                    null
                )
            }.onSuccess { flow ->
                flow.collect { scheduleItems ->
                    _scheduleItems.value = scheduleItems.scheduleEntities.map {
                        Schedule(
                            id = it.id,
                            title = it.title,
                            startDate = it.startDate,
                            endDate = it.endDate,
                            description = it.details,
                            daysBefore = ChronoUnit.DAYS.between(LocalDate.now(), it.startDate)
                                .toInt(),
                            isComplete = it.isComplete,
                            completeDate = it.completeDate,
                        )
                    }.sortedBy { it.startDate }
                    lastStartDate = _scheduleItems.value?.lastOrNull()?.startDate
                    _isScheduleListLoading.value = false
                }

            }.onFailure { exception ->
                exception.printStackTrace()
                _isScheduleListLoading.value = false
            }
        }
    }

    // 일정 more load 메소드
    fun loadMoreScheduleData() {
        viewModelScope.launch {
            _isScheduleListLoading.value = true
            runCatching {
                getScheduleItemsForAlarmUseCase.invoke(
                    _selectDate.value?.toLocalDate() ?: LocalDate.now(),
                    lastStartDate
                )
            }.onSuccess { flow ->
                flow.collect { scheduleItems ->
                    _scheduleItems.value =
                        _scheduleItems.value?.plus(scheduleItems.scheduleEntities.map {
                            Schedule(
                                id = it.id,
                                title = it.title,
                                startDate = it.startDate,
                                endDate = it.endDate,
                                description = it.details,
                                daysBefore = ChronoUnit.DAYS.between(LocalDate.now(), it.startDate)
                                    .toInt(),
                                isComplete = it.isComplete,
                                completeDate = it.completeDate,
                            )
                        }.filterNot { newSchedule ->
                            _scheduleItems.value?.any { existingSchedule ->
                                existingSchedule.id == newSchedule.id
                            } == true
                        })?.sortedBy { it.startDate } ?: emptyList()
                    lastStartDate = _scheduleItems.value?.lastOrNull()?.startDate
                    _isScheduleListLoading.value = false
                }
            }.onFailure { exception ->
                exception.printStackTrace()
                _isScheduleListLoading.value = false
            }
        }
    }


    /*
    * 유저 설정
    * */

    private fun loadUserSetting() {
        viewModelScope.launch {
            runCatching {
                settingObserveCompletedItemsVisibilityUseCase.invoke()
            }.onSuccess { flow ->
                flow.collect { visibility ->
                    _settingObserveCompleteVisibility.value = visibility
                    Log.d(TAG, "value : ${_settingObserveCompleteVisibility.value}")
                }
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load settings", exception)
            }
        }
    }
}




