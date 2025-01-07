package com.joo.miruni.presentation.unlock

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.GetScheduleItemsUseCase
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCase
import com.joo.miruni.presentation.home.Schedule
import com.joo.miruni.presentation.home.ThingsTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class UnlockViewModel @Inject constructor(
    private val getTodoItemsForAlarmUseCase: GetTodoItemsForAlarmUseCase,
    private val getScheduleItemsForAlarmUseCase: GetScheduleItemsUseCase,
    private val settingObserveCompletedItemsVisibilityUseCase: SettingObserveCompletedItemsVisibilityUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "UnlockViewModel"
    }

    /*
    * Live Date
    * */

    // 선택된 날짜
    private val _selectDate = MutableLiveData<LocalDate>(LocalDate.now())
    val selectDate: LiveData<LocalDate> get() = _selectDate

    // 현재 시간
    private val _curTime = MutableLiveData<LocalDateTime>(LocalDateTime.now())
    val curTime: LiveData<LocalDateTime> get() = _curTime

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

    // 완료 항목 값
    private val _settingObserveCompleteVisibility = MutableLiveData<Boolean>(false)
    val settingObserveCompleteVisibility: LiveData<Boolean> get() = _settingObserveCompleteVisibility

    // scheduleList 페이징을 위한 마지막 데이터의 startDate
    private var lastStartDate: LocalDate? = null

    // getThings 코루틴
    private var getTodoItemsJob: Job? = null

    init {
        loadTodoItemsForAlarm()
        loadInitialScheduleData()
        loadUserSetting()
        startUpdatingTime()
    }


    /*
    * 할 일
    * */

    // 할 일 load 메소드
    private fun loadTodoItemsForAlarm() {
        getTodoItemsJob?.cancel()
        getTodoItemsJob = viewModelScope.launch {
            _isTodoListLoading.value = true
            runCatching {
                getTodoItemsForAlarmUseCase.invoke(
                    _selectDate.value?.atStartOfDay() ?: LocalDateTime.now()
                )
            }.onSuccess { flow ->
                flow.collect { todoItems ->
                    _thingsTodoItems.value =
                        todoItems.todoEntities.map {
                            ThingsTodo(
                                id = it.id,
                                title = it.title,
                                deadline = it.deadLine ?: LocalDateTime.now(),
                                description = it.details ?: "",
                                isCompleted = it.isComplete,
                                completeDate = it.completeDate,
                                isPinned = it.isPinned
                            )
                        }.distinctBy { it.id }.sortedBy { it.deadline }

                    _isTodoListLoading.value = false
                }
            }.onFailure { exception ->
                exception.printStackTrace()
                _isTodoListLoading.value = false
            }
        }
    }

    /*
    * UI
    * */

    // 날짜 Text 포멧
    fun formatSelectedDate(date: LocalDate): String {
        val today = LocalDate.now()

        return when {
            else -> {
                if (date.year == today.year) {
                    date.format(DateTimeFormatter.ofPattern("M월 d일"))
                } else {
                    date.format(DateTimeFormatter.ofPattern("M월 d일, yyyy"))
                }
            }
        }
    }

    // 시간 업데이트
    private fun startUpdatingTime() {
        viewModelScope.launch {
            while (true) {
                _curTime.postValue(LocalDateTime.now())
                delay(1000)
            }
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
                    _selectDate.value ?: LocalDate.now(),
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
                            daysBefore = when {
                                it.startDate != null && it.startDate.isEqual(LocalDate.now()) -> 0
                                it.startDate != null && it.endDate != null &&
                                        (it.startDate.isBefore(LocalDate.now()) && it.endDate.isAfter(
                                            LocalDate.now()
                                        )) -> 0

                                it.startDate != null && it.startDate.isAfter(LocalDate.now()) -> ChronoUnit.DAYS.between(
                                    LocalDate.now(),
                                    it.startDate
                                ).toInt() // 시작일이 미래인 경우
                                else -> 0
                            },
                            isComplete = it.isComplete,
                            completeDate = it.completeDate,
                            isPinned = it.isPinned
                        )
                    }
                        .sortedWith(compareByDescending<Schedule> { it.isPinned }.thenBy { it.startDate })
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
                    _selectDate.value ?: LocalDate.now(),
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
                                daysBefore = when {
                                    it.startDate != null && it.startDate.isEqual(LocalDate.now()) -> 0
                                    (it.startDate != null && it.endDate != null) && it.startDate.isBefore(
                                        LocalDate.now()
                                    ) && it.endDate.isAfter(
                                        LocalDate.now()
                                    ) -> 0

                                    else -> ChronoUnit.DAYS.between(LocalDate.now(), it.startDate)
                                        .toInt()
                                },
                                isComplete = it.isComplete,
                                completeDate = it.completeDate,
                                isPinned = it.isPinned
                            )
                        }.filterNot { newSchedule ->
                            _scheduleItems.value?.any { existingSchedule ->
                                existingSchedule.id == newSchedule.id
                            } == true
                        })
                            ?.sortedWith(compareByDescending<Schedule> { it.isPinned }.thenBy { it.startDate })
                            ?: emptyList()
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
                }
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load settings", exception)
            }
        }
    }
}




