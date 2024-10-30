package com.joo.miruni.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.R
import com.joo.miruni.domain.usecase.SettingCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCase
import com.joo.miruni.presentation.BottomNavItem
import com.joo.miruni.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingCompletedItemsVisibilityUseCase: SettingCompletedItemsVisibilityUseCase,
    private val settingObserveCompletedItemsVisibilityUseCase: SettingObserveCompletedItemsVisibilityUseCase,
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    /*
    * Live Data
    * */
    // 완료 항목 값
    private val _settingObserveCompleteVisibility = MutableLiveData<Boolean>(false)
    val settingObserveCompleteVisibility: LiveData<Boolean> get() = _settingObserveCompleteVisibility

    init {
        loadUserSetting()
    }

    // 아이콘 리소스 ID만 저장
    val bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem("미루기", R.drawable.ic_clock, Screen.Overdue),
        BottomNavItem("홈", R.drawable.ic_home, Screen.Home),
        BottomNavItem("캘린더", R.drawable.ic_calendar, Screen.Calendar)
    )

    // 완료된 항목 보기 설정
    fun setCompletedItemsVisibility() {
        viewModelScope.launch {
            runCatching {
                settingCompletedItemsVisibilityUseCase.invoke()
            }.onSuccess {
                _settingObserveCompleteVisibility.value =
                    _settingObserveCompleteVisibility.value!!.not()
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load settings", exception)
            }
        }
    }

    // 유저 설정 load
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



