package com.joo.miruni.presentation.setup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCase
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val saveAlarmTimeUseCase: SaveAlarmTimeUseCase,
    private val getAlarmTimeUseCase: GetAlarmTimeUseCase,
) : ViewModel() {

    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String> get() = _selectedTime

    // 초기화 상태
    private val _isInit = MutableLiveData<Boolean>()
    val isInit: LiveData<Boolean> get() = _isInit

    init {
        getAlarmTime()
    }

    fun saveAlarmTime() {
        viewModelScope.launch {
            saveAlarmTimeUseCase(_selectedTime.value ?: "")
            _isInit.value = false // 초기화 상태 false
        }
    }

    private fun getAlarmTime() {
        viewModelScope.launch {
            val time = getAlarmTimeUseCase()
            _selectedTime.value = time
            _isInit.value = time.isEmpty()
        }
    }

    fun updateSelectedTime(newTime: String) {
        _selectedTime.value = newTime
    }

}



