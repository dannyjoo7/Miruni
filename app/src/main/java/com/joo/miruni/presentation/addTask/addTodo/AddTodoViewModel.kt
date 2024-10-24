package com.joo.miruni.presentation.addTask.addTodo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.AddTodoItemUseCase
import com.joo.miruni.presentation.widget.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val addTodoItemUseCase: AddTodoItemUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "AddTodoViewModel"

        const val MAX_TODO_LENGTH = 20
        const val MAX_DESCRIPTION_LENGTH = 100
    }

    /*
    * 변수
    * */

    // 할 일 텍스트
    private val _todoText = MutableLiveData("")
    val todoText: LiveData<String> get() = _todoText

    // 세부사항 텍스트
    private val _descriptionText = MutableLiveData("")
    val descriptionText: LiveData<String> get() = _descriptionText


    // 선택된 날짜
    private val _selectedDate = MutableLiveData<LocalDate?>(LocalDate.now().plusDays(1))
    val selectedDate: LiveData<LocalDate?> get() = _selectedDate

    // 선택된 시간
    private val _selectedTime = MutableLiveData<LocalTime>(getCurrentTimeIn5MinIntervals())
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


    // AddTodo 성공 여부
    private val _isTodoAdded = MutableLiveData<Boolean>(false)
    val isTodoAdded: LiveData<Boolean> get() = _isTodoAdded

    /*
    * 메소드
    * */

    /*
    * UI
    * */

    // 할 일 텍스트 업데이트
    fun updateTodoText(newValue: String) {
        _todoText.value = newValue.take(MAX_TODO_LENGTH)
    }

    // 세부사항 텍스트 업데이트
    fun updateDescriptionText(newValue: String) {
        _descriptionText.value = newValue.take(MAX_DESCRIPTION_LENGTH)
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

    // AlarmDisplayDatePicker 가시성 on/off
    fun clickedAlarmDisplayStartDateText() {
        _showAlarmDisplayStartDatePicker.value = _showAlarmDisplayStartDatePicker.value?.not()
        _showDatePicker.value = false
        _showTimePicker.value = false
    }

    // 날짜 선택 메소드
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _showDatePicker.value = false
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
    }

    // 애니메이션 종료
    fun finishAnimation() {
        _isTodoTextEmpty.value = false
    }

    /*
    * TimePicker
    * */

    // 시간 포멧
    fun convertLocalTimeToTime(localTime: LocalTime): Time {
        val hour = localTime.hour
        val minute = localTime.minute

        val format = if (hour < 12) {
            "오전"
        } else {
            "오후"
        }

        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12

        return Time(adjustedHour, minute, format)
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
    }

    // 현재 시간을 5분 단위로 조정
    private fun getCurrentTimeIn5MinIntervals(): LocalTime {
        val now = LocalTime.now()
        val adjustedMinute = (now.minute / 5) * 5
        return LocalTime.of(now.hour, adjustedMinute)
    }


    /*
    * DatePicker
    * */

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

    // 연도 변경 처리
    fun changeYear(year: Int) {
        _selectedDate.value = _selectedDate.value?.withYear(year)
    }


    /*
    * Top Bar
    * */

    // 추가 버튼 클릭 시
    fun addTodoItem() {
        if (validateTodoItem()) {
            viewModelScope.launch {
                val todoItem = TodoItem(
                    todoText = _todoText.value ?: "",
                    descriptionText = _descriptionText.value ?: "",
                    selectedDate = combineDateAndTime(
                        _selectedDate.value ?: LocalDate.now().plusDays(1),
                        _selectedTime.value ?: LocalTime.now()
                    ),
                    adjustedDate = calculateAdjustedDate(
                        _selectedDate.value ?: LocalDate.now(),
                        _selectedAlarmDisplayDate.value ?: AlarmDisplayDuration(1, "주")
                    )
                )

                runCatching {
                    addTodoItemUseCase(todoItem)
                }.onSuccess {
                    _isTodoAdded.value = true
                }.onFailure { exception ->
                    _isTodoAdded.value = false
                    Log.e(TAG, exception.message.toString())
                }
            }
        } else {
            return
        }
    }

    // 무결성 검사
    private fun validateTodoItem(): Boolean {
        // 제목이 비어있는지 체크
        if (_todoText.value.isNullOrEmpty()) {
            _isTodoTextEmpty.value = true
            return false
        }
        return true
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

    // 시간과 날짜 합치는 메소드
    private fun combineDateAndTime(date: LocalDate, time: LocalTime): LocalDateTime {
        return date.atTime(time)
    }


}


