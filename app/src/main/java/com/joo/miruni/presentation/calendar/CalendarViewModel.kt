package com.joo.miruni.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.domain.usecase.GetTasksForDateRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
open class CalendarViewModel @Inject constructor(
    private val getTasksForDateRange: GetTasksForDateRangeUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "CalendarViewModel"
    }

    // 달력을 움직이기 위한 변수
    private val _curDate = MutableLiveData<LocalDate?>(LocalDate.now())
    val curDate: LiveData<LocalDate?> get() = _curDate

    // 선택된 날짜
    private val _selectedDate = MutableLiveData<LocalDate?>(LocalDate.now())
    val selectedDate: LiveData<LocalDate?> get() = _selectedDate

    // task 존재 여부 리스트
    private val _taskPresenceList = MutableLiveData<List<Boolean>>()
    val taskPresenceList: LiveData<List<Boolean>> get() = _taskPresenceList

    // task List
    private val _taskList = MutableLiveData<List<TaskItem>>()
    val taskList: LiveData<List<TaskItem>> get() = _taskList

    // selected Date task List
    private val _selectedDateTaskList = MutableLiveData<List<TaskItem>>()
    val selectedDateTaskList: LiveData<List<TaskItem>> get() = _selectedDateTaskList

    init {
        loadTaskListForMonth()
    }

    /*
     메소드
    */

    // 날짜 선택 메소드
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        setSelectedDateTaskList()
    }

    fun monthChange(date: LocalDate) {
        _curDate.value = date
        _selectedDate.value = null
        _selectedDateTaskList.value = emptyList()
        loadTaskListForMonth()
    }

    // 해당 월 Task 여부 메소드
    private fun loadTaskListForMonth() {
        viewModelScope.launch {
            val currentDate = _curDate.value ?: LocalDate.now()

            val startDate = LocalDate.of(currentDate.year, currentDate.monthValue, 1)
            val endDate = startDate.plusMonths(1).minusDays(1)

            runCatching {
                getTasksForDateRange.invoke(startDate, endDate)
            }.onSuccess { flow ->
                flow.collect { taskItems ->
                    _taskList.value =
                        taskItems.taskEntities.map {
                            TaskItem(
                                id = it.id,
                                deadline = it.deadLine ?: LocalDateTime.now(),
                                startDate = it.startDate,
                                endDate = it.endDate,
                                title = it.title,
                                details = it.details ?: "",
                                isComplete = it.isComplete,
                                completeDate = it.completeDate,
                                type = it.type
                            )
                        }
                    checkTaskPresenceForMonth()
                    setSelectedDateTaskList()
                }
            }.onFailure { exception ->
                Log.e(TAG, "Error loading task presence for month: ${exception.message}")
            }
        }
    }

    // 해당 날짜에 Task 존재 여부를 확인하는 메소드
    private fun checkTaskPresenceForMonth() {
        val currentDate = _curDate.value ?: LocalDate.now()
        val endDate =
            LocalDate.of(currentDate.year, currentDate.monthValue, 1).plusMonths(1).minusDays(1)

        _taskPresenceList.value = (1..endDate.dayOfMonth).map { day ->
            val dateToCheck =
                LocalDate.of(currentDate.year, currentDate.monthValue, day)

            taskList.value?.any { task ->
                when (task.type) {
                    TaskType.SCHEDULE ->
                        (task.startDate == dateToCheck || task.endDate == dateToCheck ||
                                (task.startDate != null && task.endDate != null &&
                                        dateToCheck.isAfter(task.startDate) && dateToCheck.isBefore(
                                    task.endDate
                                )))

                    TaskType.TODO ->
                        task.deadline?.toLocalDate() == dateToCheck
                }
            } ?: false
        }
    }

    // 선택된 날짜 TaskList 설정
    private fun setSelectedDateTaskList() {
        val date = selectedDate.value
        if (date != null) {
            _selectedDateTaskList.value = _taskList.value?.filter { task ->
                when (task.type) {
                    TaskType.SCHEDULE -> {
                        task.startDate != null && task.endDate != null &&
                                (date.isEqual(task.startDate) ||
                                        date.isEqual(task.endDate) ||
                                        (date.isAfter(task.startDate) && date.isBefore(task.endDate)))
                    }

                    TaskType.TODO -> {
                        task.deadline?.toLocalDate() == date
                    }
                }
            }?.sortedBy { it.type } ?: emptyList()
        }
    }

}