package com.joo.miruni.presentation.addTodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joo.miruni.presentation.widget.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "AddTodoViewModel"

        const val MAX_TODO_LENGTH = 10
        const val MAX_DESCRIPTION_LENGTH = 20
    }

    init {

    }

    // 할 일 텍스트
    private val _todoText = MutableLiveData("")
    val todoText: LiveData<String> get() = _todoText

    // 세부사항 텍스트
    private val _descriptionText = MutableLiveData("")
    val descriptionText: LiveData<String> get() = _descriptionText

    // 선택된 날짜
    private val _selectedDate = MutableLiveData<LocalDate?>(LocalDate.now())
    val selectedDate: LiveData<LocalDate?> get() = _selectedDate

    // 선택된 시간
    private val _selectedTime = MutableLiveData<LocalTime>(LocalTime.now())
    val selectedTime: LiveData<LocalTime> get() = _selectedTime

    // Bool 날짜 선택 진행 유뮤
    private val _showDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean> get() = _showDatePicker

    // Bool 시간 선택 진행 유뮤
    private val _showTimePicker = MutableLiveData(false)
    val showTimePicker: LiveData<Boolean> get() = _showTimePicker


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
    }

    // TimePicker 가시성 on/off
    fun clickedTimePickerBtn() {
        _showTimePicker.value = _showTimePicker.value?.not()
        _showDatePicker.value = false
    }

    // 날짜 선택 메소드
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _showDatePicker.value = false // 날짜 선택 후 닫기
    }

    // 선택된 날짜 업데이트 메소드
    fun updateSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
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

    // 연도 변경 처리
    fun changeYear(year: Int) {
        _selectedDate.value = _selectedDate.value?.withYear(year)
    }

}

