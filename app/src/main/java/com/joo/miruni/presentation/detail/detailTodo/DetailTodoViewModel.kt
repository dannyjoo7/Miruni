package com.joo.miruni.presentation.detail.detailTodo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.GetTodoItemByIDUseCase
import com.joo.miruni.domain.usecase.UpdateTodoItemUseCase
import com.joo.miruni.presentation.addTask.addTodo.AlarmDisplayDuration
import com.joo.miruni.presentation.addTask.addTodo.TodoItem
import com.joo.miruni.presentation.addTask.addTodo.toTodoItem
import com.joo.miruni.presentation.widget.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class DetailTodoViewModel @Inject constructor(
    private val getTodoItemByIDUseCase: GetTodoItemByIDUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val completeTaskItemUseCase: CompleteTaskItemUseCase,
    private val cancelCompleteTaskItemUseCase: CancelCompleteTaskItemUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "DetailTodoViewModel"

        const val MAX_TODO_LENGTH = 20
        const val MAX_DESCRIPTION_LENGTH = 100
    }

    /*
    * 변수
    * */

    // TodoItem
    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem> get() = _todoItem

    // 수정 됐는지 판단 변수
    private val _isModified = MutableLiveData(false)
    val isModified: LiveData<Boolean> get() = _isModified

    // 할 일 텍스트
    private val _todoText = MutableLiveData("")
    val todoText: LiveData<String> get() = _todoText

    // 세부사항 텍스트
    private val _descriptionText = MutableLiveData("")
    val descriptionText: LiveData<String> get() = _descriptionText


    // 선택된 날짜
    private val _selectedDate = MutableLiveData<LocalDate?>()
    val selectedDate: LiveData<LocalDate?> get() = _selectedDate

    // 선택된 시간
    private val _selectedTime = MutableLiveData<LocalTime>()
    val selectedTime: LiveData<LocalTime> get() = _selectedTime

    // 선택된 알람 표시 시간
    private val _selectedAlarmDisplayDate = MutableLiveData<AlarmDisplayDuration>(
        AlarmDisplayDuration(1, "주")
    )
    val selectedAlarmDisplayDate: LiveData<AlarmDisplayDuration> get() = _selectedAlarmDisplayDate


    // Bool 날짜 선택 진행 유뮤
    private val _showDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean> get() = _showDatePicker

    // Bool 시간 선택 진행 유뮤
    private val _showTimePicker = MutableLiveData(false)
    val showTimePicker: LiveData<Boolean> get() = _showTimePicker

    // Bool 알람 표시 시작일 선택 진행 유뮤
    private val _showAlarmDisplayStartDatePicker = MutableLiveData(false)
    val showAlarmDisplayStartDatePicker: LiveData<Boolean> get() = _showAlarmDisplayStartDatePicker


    // TodoTextEmpty 애니매이션
    private val _isTodoTextEmpty = MutableLiveData(false)
    val isTodoTextEmpty: LiveData<Boolean> get() = _isTodoTextEmpty


    // updateTodo 성공 여부
    private val _isTodoUpdate = MutableLiveData<Boolean>(false)
    val isTodoUpdate: LiveData<Boolean> get() = _isTodoUpdate


    /*
    * 메소드
    * */

    // init 메소드
    fun loadTodoDetails(todoId: Long) {
        viewModelScope.launch {
            runCatching {
                getTodoItemByIDUseCase(todoId)
            }.onSuccess { todoModel ->
                // TodoItem으로 변환
                _todoItem.value = todoModel.toTodoItem()

                // 속성 매칭
                _todoText.value = todoModel.title
                _descriptionText.value = todoModel.details
                _selectedDate.value =
                    todoModel.deadLine?.toLocalDate() ?: LocalDate.now().plusDays(1)
                _selectedTime.value = todoModel.deadLine?.toLocalTime() ?: LocalTime.now()

                // 알람 표시 날짜 계산 후 사용 예시
                val (amount, unit) = calculateDistanceOfDateUnit(
                    todoModel.deadLine?.toLocalDate() ?: LocalDate.now(),
                    todoModel.alarmDisplayDate
                )
                _selectedAlarmDisplayDate.value = AlarmDisplayDuration(amount, unit)


            }.onFailure { exception ->
                Log.e(TAG, exception.toString())
            }
        }
    }

    /*
    * UI
    * */

    // 할 일 텍스트 업데이트
    fun updateTodoText(newValue: String) {
        _todoText.value = newValue.take(MAX_TODO_LENGTH)
        isModify()
    }

    // 세부사항 텍스트 업데이트
    fun updateDescriptionText(newValue: String) {
        _descriptionText.value = newValue.take(MAX_DESCRIPTION_LENGTH)
        isModify()
    }

    // DatePicker 가시성 on/off
    fun clickedDatePickerBtn() {
        _showDatePicker.value = _showDatePicker.value?.not()
        _showTimePicker.value = false
        _showAlarmDisplayStartDatePicker.value = false
    }

    // TimePicker 가시성 on/off
    fun clickedTimePickerBtn() {
        _showTimePicker.value = _showTimePicker.value?.not()
        _showDatePicker.value = false
        _showAlarmDisplayStartDatePicker.value = false
    }

    //  AlarmDisplayDatePicker 가시성 on/off
    fun clickedAlarmDisplayStartDateText() {
        _showAlarmDisplayStartDatePicker.value = _showAlarmDisplayStartDatePicker.value?.not()
        _showDatePicker.value = false
        _showTimePicker.value = false
    }

    // 날짜 선택 메소드
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _showDatePicker.value = false
        isModify()
    }

    // 선택된 날짜 업데이트 메소드
    fun updateSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
    }

    // 선택된 알람 표시일 업데이트 메서드
    fun updateSelectedAlarmDisplayDate(amount: Int? = null, durationUnit: String? = null) {
        val currentValue = _selectedAlarmDisplayDate.value ?: AlarmDisplayDuration(1, "주")

        _selectedAlarmDisplayDate.value = AlarmDisplayDuration(
            amount = amount ?: currentValue.amount,
            unit = durationUnit ?: currentValue.unit
        )
        isModify()
    }

    // 애니메이션 종료
    fun finishAnimation() {
        _isTodoTextEmpty.value = false
    }

    /*
    * TimePicker
    * */

    // 시간 포멧
    fun convertLocalTimeToTime(): Time? {
        val selectedTime = _selectedTime.value

        if (selectedTime != null) {
            val hour = selectedTime.hour
            val minute = selectedTime.minute

            val format = if (hour < 12) {
                "오전"
            } else {
                "오후"
            }

            val adjustedHour = if (hour % 12 == 0) 12 else hour % 12

            return Time(adjustedHour, minute, format)
        } else {
            return null
        }
    }

    // 날짜 Text 포멧
    fun formatSelectedDate(date: LocalDate): String {

        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dayAfterTomorrow = today.plusDays(2)

        return when {
            date.isEqual(today) -> "오늘"
            date.isEqual(tomorrow) -> "내일"
            date.isEqual(dayAfterTomorrow) -> "내일 모레"
            else -> date.format(DateTimeFormatter.ofPattern("M월 d일, yyyy"))
                ?: today.format(DateTimeFormatter.ofPattern("M월 d일, yyyy"))
        }
    }

    // 시간 Text 포멧
    fun formatLocalTimeToString(localTime: LocalTime): String {
        val hour = localTime.hour
        val minute = localTime.minute

        val format = if (hour < 12) {
            "오전"
        } else {
            "오후"
        }

        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12

        return "${adjustedHour}:${minute.toString().padStart(2, '0')} $format"
    }

    // 선택된 시간 업데이트 메서드
    fun updateSelectedTime(hour: Int, minute: Int, format: String) {
        val adjustedHour = when {
            format == "오후" && hour != 12 -> hour + 12
            format == "오전" && hour == 12 -> 0
            else -> hour
        }
        val newTime = LocalTime.of(adjustedHour, minute)
        _selectedTime.value = newTime
        isModify()
    }

    /*
    * DatePicker
    * */

    // MM월 yyyy 변환 메소드
    fun formatSelectedDateForCalendar(): String {
        return _selectedDate.value?.let {
            val month = it.monthValue
            val year = it.year
            "${month}월 $year"
        } ?: ""
    }

    // 월 변경 처리
    fun changeMonth(month: Int) {
        _selectedDate.value?.let {
            val newDate = if (month > 12) {
                LocalDate.of(it.year + 1, 1, 1)
            } else {
                LocalDate.of(it.year, month, 1)
            }
            _selectedDate.value = newDate
        }
    }

    /*
    * Top Bar
    * */

    // 수정 버튼 클릭 시
    fun updateTodoItem() {
        viewModelScope.launch {
            // TodoItem ID가 null인 경우
            if (_todoItem.value?.id == null) {
                Log.e(TAG, "Todo item ID is null.")
                return@launch
            }

            // Todo 텍스트가 비어있는 경우
            if (_todoText.value.isNullOrEmpty()) {
                _isTodoTextEmpty.value = true
                delay(600)
                _isTodoTextEmpty.value = false
                return@launch
            }

            // TodoItem 생성
            val todoItem = TodoItem(
                id = _todoItem.value!!.id,
                title = _todoText.value ?: "",
                descriptionText = _descriptionText.value ?: "",
                selectedDate = combineDateAndTime(
                    _selectedDate.value ?: LocalDate.now().plusDays(1),
                    _selectedTime.value ?: LocalTime.now()
                ),
                adjustedDate = calculateAdjustedDate(
                    _selectedDate.value ?: LocalDate.now(),
                    _selectedAlarmDisplayDate.value ?: AlarmDisplayDuration(1, "주")
                ),
                isComplete = _todoItem.value?.isComplete ?: false,
                isPinned = _todoItem.value?.isPinned ?: false
            )

            runCatching {
                updateTodoItemUseCase(todoItem)
            }.onSuccess {
                _isTodoUpdate.value = true
            }.onFailure { exception ->
                _isTodoUpdate.value = false
                Log.e(TAG, exception.message.toString())
            }
        }
    }

    // 알람 표시 시작일 계산 메소드
    private fun calculateAdjustedDate(
        currentDate: LocalDate,
        duration: AlarmDisplayDuration,
    ): LocalDate {
        if (duration.amount == null) {
            return currentDate.minusWeeks(1)
        }
        return when (duration.unit) {
            "일" -> currentDate.minusDays(duration.amount.toLong())
            "주" -> currentDate.minusWeeks(duration.amount.toLong())
            "개월" -> currentDate.minusMonths(duration.amount.toLong())
            "년" -> currentDate.minusYears(duration.amount.toLong())
            else -> currentDate
        }
    }

    // 두 날짜가 얼마나 차이나는지 유닛을 계산 및 반환해주는 메소드
    private fun calculateDistanceOfDateUnit(
        deadLine: LocalDate,
        alarmDate: LocalDate,
    ): Pair<Int, String> {
        val daysDifference = deadLine.toEpochDay() - alarmDate.toEpochDay()

        return when {
            daysDifference == 0L -> Pair(0, "일") // 같은 날
            daysDifference < 0 -> Pair(daysDifference.toInt().absoluteValue, "일") // 과거
            daysDifference < 7 -> Pair(daysDifference.toInt(), "일") // 7일 이내
            daysDifference < 30 -> Pair((daysDifference / 7).toInt(), "주") // 30일 이내
            daysDifference < 365 -> Pair((daysDifference / 30).toInt(), "개월") // 1년 이내
            else -> Pair((daysDifference / 365).toInt(), "년") // 1년 이상
        }
    }


    // 시간과 날짜 합치는 메소드
    private fun combineDateAndTime(date: LocalDate, time: LocalTime): LocalDateTime {
        return date.atTime(time)
    }

    // 항목이 수정됨
    private fun isModify() {
        _isModified.value = true
    }

    /*
   * Bottom Bar
   * */

    // 일정 삭제
    fun deleteTodoItem(todoId: Long) {
        viewModelScope.launch {
            runCatching {
                deleteTaskItemUseCase.invoke(todoId)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // 일정 완료
    fun completeTodoItem(todoId: Long) {
        viewModelScope.launch {
            runCatching {
                completeTaskItemUseCase.invoke(todoId, LocalDateTime.now())
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // 일정 완료 취소
    fun completeCancelTodoItem(todoId: Long) {
        viewModelScope.launch {
            runCatching {
                cancelCompleteTaskItemUseCase.invoke(todoId)
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}


