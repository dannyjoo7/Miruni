package com.joo.miruni.presentation.addTask.addSchedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(

) : ViewModel() {
    companion object {
        const val TAG = "AddScheduleViewModel"

        const val MAX_TODO_LENGTH = 20
        const val MAX_DESCRIPTION_LENGTH = 100
    }

    init {

    }

    /*
    * 변수
    * */

    // 할 일 텍스트
    private val _titleText = MutableLiveData("")
    val titleText: LiveData<String> get() = _titleText

    // 세부사항 텍스트
    private val _descriptionText = MutableLiveData("")
    val descriptionText: LiveData<String> get() = _descriptionText


    // 선택된 시작 날짜
    private val _selectedStartDate = MutableLiveData<LocalDate?>(null)
    val selectedStartDate: LiveData<LocalDate?> get() = _selectedStartDate

    // 선택된 종료 날짜
    private val _selectedEndDate = MutableLiveData<LocalDate?>(null)
    val selectedEndDate: LiveData<LocalDate?> get() = _selectedEndDate

    // 선택된 알람 표시 시간
    private val _selectedAlarmDisplayDate = MutableLiveData<AlarmDisplayDuration>(
        AlarmDisplayDuration(1, "주")
    )
    val selectedAlarmDisplayDate: LiveData<AlarmDisplayDuration> get() = _selectedAlarmDisplayDate


    // Bool 시작 날짜 선택 진행 유뮤
    private val _showStartDatePicker = MutableLiveData(false)
    val showStartDatePicker: LiveData<Boolean> get() = _showStartDatePicker

    // Bool 종료 날짜 선택 진행 유뮤
    private val _showEndDatePicker = MutableLiveData(false)
    val showEndDatePicker: LiveData<Boolean> get() = _showEndDatePicker

    // Bool 알람 표시 시작일 선택 진행 유뮤
    private val _showAlarmDisplayStartDatePicker = MutableLiveData(false)
    val showAlarmDisplayStartDatePicker: LiveData<Boolean> get() = _showAlarmDisplayStartDatePicker


    // TodoTextEmpty 애니매이션
    private val _isTitleTextEmpty = MutableLiveData(false)
    val isTitleTextEmpty: LiveData<Boolean> get() = _isTitleTextEmpty


    // AddSchedule 성공 여부
    private val _isScheduleAdded = MutableLiveData<Boolean>(false)
    val isScheduleAdded: LiveData<Boolean> get() = _isScheduleAdded

    /*
    * UI
    * */

    // 할 일 텍스트 업데이트
    fun updateTitleText(newValue: String) {
        _titleText.value = newValue.take(MAX_TODO_LENGTH)
    }

    // 세부사항 텍스트 업데이트
    fun updateDescriptionText(newValue: String) {
        _descriptionText.value = newValue.take(MAX_DESCRIPTION_LENGTH)
    }

    // StartDatePicker 가시성 on/off
    fun clickedStartDateRangePickerBtn() {
        _showStartDatePicker.value = _showStartDatePicker.value?.not()
        _showEndDatePicker.value = false
        _showAlarmDisplayStartDatePicker.value = false
    }

    // EndDatePicker 가시성 on/off
    fun clickedEndDatePickerBtn() {
        _showEndDatePicker.value = _showEndDatePicker.value?.not()
        _showStartDatePicker.value = false
        _showAlarmDisplayStartDatePicker.value = false
    }

    // AlarmDisplayDatePicker 가시성 on/off
    fun clickedAlarmDisplayStartDateText() {
        _showAlarmDisplayStartDatePicker.value = _showAlarmDisplayStartDatePicker.value?.not()
        _showStartDatePicker.value = false
        _showEndDatePicker.value = false
    }

    // 선택된 알람 표시일 업데이트 메서드
    fun updateSelectedAlarmDisplayDate(amount: Int? = null, durationUnit: String? = null) {
        val currentValue = _selectedAlarmDisplayDate.value ?: AlarmDisplayDuration(1, "주")
        _selectedAlarmDisplayDate.value = AlarmDisplayDuration(
            amount = amount ?: currentValue.amount,
            unit = durationUnit ?: currentValue.unit
        )
    }

    /*
    * DatePicker
    * */

    // 날짜 선택 초기화
    fun initSelectedDate() {
        _selectedStartDate.value = null
        _selectedEndDate.value = null
    }


    // 시작 날짜 선택 메소드
    fun selectStartDate(date: LocalDate) {
        // 종료일이 선택되지 않은 경우
        if (_selectedEndDate.value == null) {
            _selectedStartDate.value = date
        }
        // 종료일이 선택된 경우
        else {
            // 종료일이 시작일보다 이전인 경우 종료일을 초기화
            if (_selectedEndDate.value?.isBefore(date) == true) {
                _selectedEndDate.value = null
            } else {
                // 정상적으로 시작일 설정
                _selectedStartDate.value = date
            }
        }
    }

    // 종료 날짜 선택 메소드
    fun selectEndDate(date: LocalDate) {
        // 시작일이 선택되지 않은 경우
        if (_selectedStartDate.value == null) {
            return
        }
        // 종료일이 시작일보다 이전인 경우
        else if (_selectedStartDate.value!!.isAfter(date)) {
            // 종료일을 선택할 수 없으므로 아무 것도 하지 않음
            return
        } else {
            // 정상적으로 종료일 설정
            _selectedEndDate.value = date
        }
    }

    // date MM월 yyyy 변환 메소드
    fun formatSelectedDateForCalendar(selectDate: LocalDate?): String {
        return selectDate?.let {
            val month = it.monthValue
            val year = it.year
            val date = it.dayOfMonth
            "${month}월 ${date}일 $year"
        } ?: "날짜 선택"
    }

    // 월 변경 처리
    fun changeStartDateMonth(month: Int) {
        _selectedStartDate.value = null
    }


    // 연도 변경 처리
    fun changeYear(year: Int) {
        _selectedStartDate.value = _selectedStartDate.value?.withYear(year)
    }


    /*
    * Top Bar
    * */

    // 추가 버튼 클릭 시
    fun addScheduleItem() {
        viewModelScope.launch {
            if (_titleText.value.isNullOrEmpty()) {
                _isTitleTextEmpty.value = true
                delay(600)
                _isTitleTextEmpty.value = false
                return@launch
            }

            val scheduleItem = ScheduleItem(
                title = _titleText.value ?: "",
                descriptionText = _descriptionText.value ?: "",
                startDate = combineDateAndTime(
                    _selectedStartDate.value ?: LocalDate.now().plusDays(1),
                    LocalTime.of(0, 0)
                ),
                endDate = combineDateAndTime(
                    _selectedEndDate.value ?: LocalDate.now().plusDays(1),
                    LocalTime.of(0, 0)
                ),
                adjustedDate = calculateAdjustedDate(
                    _selectedStartDate.value ?: LocalDate.now(),
                    _selectedAlarmDisplayDate.value ?: AlarmDisplayDuration(1, "주")
                )
            )

            runCatching {

            }.onSuccess {
                _isScheduleAdded.value = true
            }.onFailure { exception ->
                _isScheduleAdded.value = false
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

    // 시간과 날짜 합치는 메소드
    private fun combineDateAndTime(date: LocalDate, time: LocalTime): LocalDateTime {
        return date.atTime(time)
    }


}


