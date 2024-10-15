package com.joo.miruni.presentation.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCase
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCase
import com.joo.miruni.presentation.widget.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val saveAlarmTimeUseCase: SaveAlarmTimeUseCase,
    private val getAlarmTimeUseCase: GetAlarmTimeUseCase,
) : ViewModel() {

    // 선택된 시간
    private val _selectedTime = MutableLiveData<LocalTime>(LocalTime.of(9, 0))
    val selectedTime: LiveData<LocalTime> get() = _selectedTime

    // Bool 시간 선택 진행 유뮤
    private val _showTimePicker = MutableLiveData(false)
    val showTimePicker: LiveData<Boolean> get() = _showTimePicker

    // 초기화 상태
    private val _isInit = MutableLiveData<Boolean>()
    val isInit: LiveData<Boolean> get() = _isInit

    init {
        getAlarmTime()
    }

    // 알람 시간 저장
    fun saveAlarmTime() {
        viewModelScope.launch {
            saveAlarmTimeUseCase(_selectedTime.value ?: LocalTime.of(9, 0))
            _isInit.value = false
        }
    }

    // 알람 시간 있는지 확인
    private fun getAlarmTime() {
        viewModelScope.launch {
            val time = getAlarmTimeUseCase()

            _isInit.value = time == null

            time?.let {
                _selectedTime.value = it
            }
        }
    }

    // TimePicker 표시 on/off
    fun toggleTimePickerBtn() {
        _showTimePicker.value = _showTimePicker.value?.not()
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
}



